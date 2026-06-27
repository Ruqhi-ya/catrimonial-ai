package com.catrimonial.auth.repository;

import com.catrimonial.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByResetPasswordToken(String token);

    @Query("SELECT u FROM User u WHERE u.city = :city AND u.id != :userId")
    Page<User> findNearbyUsers(@Param("city") String city, @Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.verified = true")
    long countVerifiedUsers();
}
