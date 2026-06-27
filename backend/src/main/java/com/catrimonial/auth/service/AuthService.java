package com.catrimonial.auth.service;

import com.catrimonial.auth.dto.*;
import com.catrimonial.auth.entity.RefreshToken;
import com.catrimonial.auth.entity.Role;
import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.RefreshTokenRepository;
import com.catrimonial.auth.repository.RoleRepository;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.auth.security.JwtTokenProvider;
import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.common.exception.BadRequestException;
import com.catrimonial.common.exception.DuplicateResourceException;
import com.catrimonial.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .verificationToken(verificationToken)
                .enabled(true) // Allow login immediately, but mark email as unverified
                .emailVerified(false)
                .profileCompletionScore(calculateProfileCompletion(request))
                .build();

        user.addRole(userRole);
        user = userRepository.save(user);

        log.info("New user registered: {}", user.getEmail());

        // Send verification email asynchronously
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationToken);

        // Generate tokens
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = createRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
        RefreshToken refreshToken = createRefreshToken(user);

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user, accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BadRequestException("Refresh token has expired. Please login again.");
        }

        User user = refreshToken.getUser();
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newAccessToken = tokenProvider.generateAccessToken(userPrincipal);

        // Rotate refresh token
        refreshTokenRepository.delete(refreshToken);
        RefreshToken newRefreshToken = createRefreshToken(user);

        return buildAuthResponse(user, newAccessToken, newRefreshToken.getToken());
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        log.info("Email verified for user: {}", user.getEmail());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElse(null);

        // Always return success to prevent email enumeration
        if (user == null) {
            log.warn("Forgot password requested for non-existent email: {}", request.getEmail());
            return;
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpires(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), resetToken);
        log.info("Password reset email sent to: {}", user.getEmail());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (user.getResetPasswordExpires().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired. Please request a new one.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);
        userRepository.save(user);

        // Invalidate all refresh tokens
        refreshTokenRepository.deleteByUser(user);

        log.info("Password reset successful for user: {}", user.getEmail());
    }

    @Transactional
    public void logout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        refreshTokenRepository.deleteByUser(user);
        log.info("User logged out: {}", user.getEmail());
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenProvider.generateRefreshTokenValue())
                .expiresAt(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getAccessTokenExpiration())
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .profileImage(user.getProfileImage())
                        .verified(user.getVerified())
                        .roles(roles)
                        .build())
                .build();
    }

    private int calculateProfileCompletion(RegisterRequest request) {
        int score = 20; // email + password = base
        if (request.getName() != null && !request.getName().isBlank()) score += 20;
        if (request.getPhone() != null && !request.getPhone().isBlank()) score += 15;
        if (request.getCity() != null && !request.getCity().isBlank()) score += 15;
        if (request.getState() != null && !request.getState().isBlank()) score += 10;
        if (request.getCountry() != null && !request.getCountry().isBlank()) score += 10;
        return Math.min(score, 100);
    }
}
