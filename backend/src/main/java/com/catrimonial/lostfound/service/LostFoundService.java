package com.catrimonial.lostfound.service;

import com.catrimonial.auth.entity.User;
import com.catrimonial.auth.repository.UserRepository;
import com.catrimonial.cat.entity.Cat;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.lostfound.dto.LostFoundRequest;
import com.catrimonial.lostfound.entity.LostFound;
import com.catrimonial.lostfound.repository.LostFoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LostFoundService {

    private final LostFoundRepository lostFoundRepository;
    private final UserRepository userRepository;
    private final CatRepository catRepository;

    @Transactional
    public LostFound createReport(UUID userId, LostFoundRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cat cat = null;
        if (request.getCatId() != null) {
            cat = catRepository.findById(request.getCatId()).orElse(null);
        }

        LostFound report = LostFound.builder()
                .user(user)
                .cat(cat)
                .catName(request.getCatName())
                .reportType(request.getReportType())
                .description(request.getDescription())
                .breed(request.getBreed())
                .color(request.getColor())
                .gender(request.getGender())
                .lastSeenLocation(request.getLastSeenLocation())
                .lastSeenDate(request.getLastSeenDate())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .images(request.getImages())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .rewardOffered(request.getRewardOffered() != null ? request.getRewardOffered() : false)
                .rewardAmount(request.getRewardAmount())
                .build();

        report = lostFoundRepository.save(report);
        log.info("Lost/Found report created: {} by user: {}", report.getId(), userId);
        return report;
    }

    @Transactional(readOnly = true)
    public Page<LostFound> getActiveReports(String type, Pageable pageable) {
        if (type != null && !type.isBlank()) {
            LostFound.ReportType reportType = LostFound.ReportType.valueOf(type.toUpperCase());
            return lostFoundRepository.findByReportTypeAndStatusOrderByCreatedAtDesc(reportType, LostFound.ReportStatus.ACTIVE, pageable);
        }
        return lostFoundRepository.findByStatusOrderByCreatedAtDesc(LostFound.ReportStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public LostFound getReport(UUID reportId) {
        return lostFoundRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
    }

    @Transactional(readOnly = true)
    public Page<LostFound> getUserReports(UUID userId, Pageable pageable) {
        return lostFoundRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional
    public LostFound resolveReport(UUID reportId, UUID userId) {
        LostFound report = lostFoundRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));

        if (!report.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only resolve your own reports");
        }

        report.setStatus(LostFound.ReportStatus.RESOLVED);
        report.setResolvedAt(LocalDateTime.now());
        return lostFoundRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Page<LostFound> searchNearby(String location, Pageable pageable) {
        return lostFoundRepository.findNearbyReports(location, pageable);
    }
}
