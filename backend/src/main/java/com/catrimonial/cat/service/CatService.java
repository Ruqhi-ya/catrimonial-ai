package com.catrimonial.cat.service;

import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.cat.dto.CatRequest;
import com.catrimonial.cat.dto.CatResponse;
import com.catrimonial.cat.entity.Cat;
import com.catrimonial.cat.entity.CatImage;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.common.exception.BadRequestException;
import com.catrimonial.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatService {

    private final CatRepository catRepository;
    private final UserRepository userRepository;

    @Transactional
    public CatResponse createCat(UUID ownerId, CatRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", ownerId));

        Cat cat = Cat.builder()
                .owner(owner)
                .name(request.getName())
                .breed(request.getBreed())
                .gender(Cat.Gender.valueOf(request.getGender().toUpperCase()))
                .ageMonths(request.getAgeMonths())
                .weightKg(request.getWeightKg())
                .color(request.getColor())
                .vaccinated(request.getVaccinated() != null ? request.getVaccinated() : false)
                .neutered(request.getNeutered() != null ? request.getNeutered() : false)
                .temperament(request.getTemperament())
                .healthIssues(request.getHealthIssues())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .build();

        cat = catRepository.save(cat);
        log.info("Cat created: {} by owner: {}", cat.getId(), ownerId);
        return mapToResponse(cat);
    }

    @Transactional
    public CatResponse updateCat(UUID catId, UUID ownerId, CatRequest request) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat", "id", catId));

        if (!cat.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("You can only update your own cats");
        }

        cat.setName(request.getName());
        cat.setBreed(request.getBreed());
        cat.setGender(Cat.Gender.valueOf(request.getGender().toUpperCase()));
        cat.setAgeMonths(request.getAgeMonths());
        cat.setWeightKg(request.getWeightKg());
        cat.setColor(request.getColor());
        cat.setVaccinated(request.getVaccinated());
        cat.setNeutered(request.getNeutered());
        cat.setTemperament(request.getTemperament());
        cat.setHealthIssues(request.getHealthIssues());
        cat.setDescription(request.getDescription());
        cat.setLatitude(request.getLatitude());
        cat.setLongitude(request.getLongitude());
        cat.setCity(request.getCity());
        cat.setState(request.getState());
        cat.setCountry(request.getCountry());

        cat = catRepository.save(cat);
        log.info("Cat updated: {}", catId);
        return mapToResponse(cat);
    }

    @Transactional
    public void deleteCat(UUID catId, UUID ownerId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat", "id", catId));

        if (!cat.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("You can only delete your own cats");
        }

        cat.setActive(false);
        catRepository.save(cat);
        log.info("Cat soft-deleted: {}", catId);
    }

    @Transactional(readOnly = true)
    public CatResponse getCat(UUID catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat", "id", catId));
        return mapToResponse(cat);
    }

    @Transactional(readOnly = true)
    public Page<CatResponse> getMyCats(UUID ownerId, Pageable pageable) {
        return catRepository.findByOwnerIdAndActiveTrue(ownerId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CatResponse> searchCats(String breed, String gender, String city, String color, Pageable pageable) {
        Cat.Gender genderEnum = gender != null ? Cat.Gender.valueOf(gender.toUpperCase()) : null;
        return catRepository.searchCats(breed, genderEnum, city, color, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CatResponse> getNearbyCats(String city, UUID excludeId, Pageable pageable) {
        return catRepository.findNearbyCats(city, excludeId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public List<String> getAllBreeds() {
        return catRepository.findAllBreeds();
    }

    private CatResponse mapToResponse(Cat cat) {
        List<CatResponse.CatImageResponse> images = cat.getImages() != null ?
                cat.getImages().stream()
                        .map(img -> CatResponse.CatImageResponse.builder()
                                .id(img.getId())
                                .url(img.getUrl())
                                .thumbnailUrl(img.getThumbnailUrl())
                                .isPrimary(img.getIsPrimary())
                                .aiBreedDetection(img.getAiBreedDetection())
                                .build())
                        .collect(Collectors.toList()) : List.of();

        User owner = cat.getOwner();

        return CatResponse.builder()
                .id(cat.getId())
                .name(cat.getName())
                .breed(cat.getBreed())
                .gender(cat.getGender().name())
                .ageMonths(cat.getAgeMonths())
                .weightKg(cat.getWeightKg())
                .color(cat.getColor())
                .vaccinated(cat.getVaccinated())
                .neutered(cat.getNeutered())
                .temperament(cat.getTemperament())
                .healthIssues(cat.getHealthIssues())
                .description(cat.getDescription())
                .latitude(cat.getLatitude())
                .longitude(cat.getLongitude())
                .city(cat.getCity())
                .state(cat.getState())
                .country(cat.getCountry())
                .verificationStatus(cat.getVerificationStatus().name())
                .active(cat.getActive())
                .images(images)
                .owner(CatResponse.OwnerInfo.builder()
                        .id(owner.getId())
                        .name(owner.getName())
                        .profileImage(owner.getProfileImage())
                        .city(owner.getCity())
                        .verified(owner.getVerified())
                        .build())
                .createdAt(cat.getCreatedAt())
                .build();
    }
}
