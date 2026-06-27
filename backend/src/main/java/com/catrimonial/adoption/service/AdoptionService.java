package com.catrimonial.adoption.service;

import com.catrimonial.adoption.dto.AdoptionPostRequest;
import com.catrimonial.adoption.entity.AdoptionPost;
import com.catrimonial.adoption.repository.AdoptionPostRepository;
import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.cat.entity.Cat;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdoptionService {

    private final AdoptionPostRepository adoptionPostRepository;
    private final UserRepository userRepository;
    private final CatRepository catRepository;

    @Transactional
    public AdoptionPost createListing(UUID userId, AdoptionPostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cat cat = null;
        if (request.getCatId() != null) {
            cat = catRepository.findById(request.getCatId()).orElse(null);
        }

        AdoptionPost post = AdoptionPost.builder()
                .user(user)
                .cat(cat)
                .title(request.getTitle())
                .description(request.getDescription())
                .catName(request.getCatName())
                .breed(request.getBreed())
                .ageMonths(request.getAgeMonths())
                .gender(request.getGender())
                .vaccinated(request.getVaccinated() != null ? request.getVaccinated() : false)
                .neutered(request.getNeutered() != null ? request.getNeutered() : false)
                .specialNeeds(request.getSpecialNeeds())
                .requirements(request.getRequirements())
                .adoptionFee(request.getAdoptionFee())
                .images(request.getImages())
                .location(request.getLocation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        post = adoptionPostRepository.save(post);
        log.info("Adoption listing created: {} by user: {}", post.getId(), userId);
        return post;
    }

    @Transactional(readOnly = true)
    public Page<AdoptionPost> getAvailableListings(String location, Pageable pageable) {
        if (location != null && !location.isBlank()) {
            return adoptionPostRepository.findByStatusAndLocationContainingIgnoreCaseOrderByCreatedAtDesc(
                    AdoptionPost.AdoptionStatus.AVAILABLE, location, pageable);
        }
        return adoptionPostRepository.findByStatusOrderByCreatedAtDesc(AdoptionPost.AdoptionStatus.AVAILABLE, pageable);
    }

    @Transactional(readOnly = true)
    public AdoptionPost getListing(UUID postId) {
        return adoptionPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption post", "id", postId));
    }

    @Transactional(readOnly = true)
    public Page<AdoptionPost> getUserListings(UUID userId, Pageable pageable) {
        return adoptionPostRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional
    public AdoptionPost updateStatus(UUID postId, UUID userId, String status) {
        AdoptionPost post = adoptionPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption post", "id", postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own listings");
        }

        post.setStatus(AdoptionPost.AdoptionStatus.valueOf(status.toUpperCase()));
        return adoptionPostRepository.save(post);
    }
}
