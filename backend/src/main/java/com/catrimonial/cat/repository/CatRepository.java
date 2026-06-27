package com.catrimonial.cat.repository;

import com.catrimonial.cat.entity.Cat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatRepository extends JpaRepository<Cat, UUID>, JpaSpecificationExecutor<Cat> {

    Page<Cat> findByOwnerIdAndActiveTrue(UUID ownerId, Pageable pageable);

    List<Cat> findByOwnerIdAndActiveTrue(UUID ownerId);

    @Query("SELECT c FROM Cat c WHERE c.active = true AND c.verificationStatus = 'VERIFIED' " +
            "AND c.city = :city AND c.id != :excludeId")
    Page<Cat> findNearbyCats(@Param("city") String city, @Param("excludeId") UUID excludeId, Pageable pageable);

    @Query("SELECT c FROM Cat c WHERE c.active = true AND c.verificationStatus = 'VERIFIED' " +
            "AND c.breed = :breed AND c.gender != :gender AND c.id != :excludeId")
    Page<Cat> findCompatibleCats(@Param("breed") String breed, @Param("gender") Cat.Gender gender,
                                  @Param("excludeId") UUID excludeId, Pageable pageable);

    @Query("SELECT c FROM Cat c WHERE c.active = true AND c.verificationStatus = 'VERIFIED' " +
            "AND (:breed IS NULL OR c.breed = :breed) " +
            "AND (:gender IS NULL OR c.gender = :gender) " +
            "AND (:city IS NULL OR c.city = :city) " +
            "AND (:color IS NULL OR c.color = :color)")
    Page<Cat> searchCats(@Param("breed") String breed, @Param("gender") Cat.Gender gender,
                          @Param("city") String city, @Param("color") String color, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Cat c WHERE c.active = true")
    long countActiveCats();

    @Query("SELECT COUNT(c) FROM Cat c WHERE c.verificationStatus = 'VERIFIED'")
    long countVerifiedCats();

    @Query("SELECT DISTINCT c.breed FROM Cat c WHERE c.active = true ORDER BY c.breed")
    List<String> findAllBreeds();
}
