package com.catrimonial.admin.service;

import com.catrimonial.admin.dto.DashboardStats;
import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.cat.entity.Cat;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.cat.repository.MatchRequestRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.community.entity.CommunityPost;
import com.catrimonial.community.repository.CommunityPostRepository;
import com.catrimonial.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final CatRepository catRepository;
    private final MatchRequestRepository matchRequestRepository;
    private final MessageRepository messageRepository;
    private final CommunityPostRepository communityPostRepository;

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .verifiedUsers(userRepository.countVerifiedUsers())
                .totalCats(catRepository.count())
                .verifiedCats(catRepository.countVerifiedCats())
                .totalMatchRequests(matchRequestRepository.count())
                .totalMessages(messageRepository.count())
                .totalPosts(communityPostRepository.countActivePosts())
                .build();
    }

    @Transactional
    public void updateCatVerification(UUID catId, String status) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat", "id", catId));
        cat.setVerificationStatus(Cat.VerificationStatus.valueOf(status.toUpperCase()));
        catRepository.save(cat);
        log.info("Cat {} verification updated to: {}", catId, status);
    }

    @Transactional
    public void verifyUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setVerified(true);
        userRepository.save(user);
        log.info("User {} verified", userId);
    }

    @Transactional
    public void disableUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setEnabled(false);
        userRepository.save(user);
        log.info("User {} disabled", userId);
    }

    @Transactional
    public void moderatePost(UUID postId, boolean active) {
        CommunityPost post = communityPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        post.setActive(active);
        communityPostRepository.save(post);
        log.info("Post {} moderation: active={}", postId, active);
    }
}
