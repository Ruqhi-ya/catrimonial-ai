package com.catrimonial.lostfound.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.lostfound.dto.LostFoundRequest;
import com.catrimonial.lostfound.entity.LostFound;
import com.catrimonial.lostfound.service.LostFoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/lost-found")
@RequiredArgsConstructor
@Tag(name = "Lost & Found", description = "Missing and found cat reports")
public class LostFoundController {

    private final LostFoundService lostFoundService;

    @PostMapping
    @Operation(summary = "Create a lost/found report")
    public ResponseEntity<LostFound> createReport(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody LostFoundRequest request) {
        LostFound report = lostFoundService.createReport(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping
    @Operation(summary = "Get active lost/found reports")
    public ResponseEntity<Page<LostFound>> getReports(
            @RequestParam(required = false) String type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lostFoundService.getActiveReports(type, pageable));
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "Get a single report")
    public ResponseEntity<LostFound> getReport(@PathVariable UUID reportId) {
        return ResponseEntity.ok(lostFoundService.getReport(reportId));
    }

    @GetMapping("/my-reports")
    @Operation(summary = "Get user's reports")
    public ResponseEntity<Page<LostFound>> getMyReports(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lostFoundService.getUserReports(user.getId(), pageable));
    }

    @PatchMapping("/{reportId}/resolve")
    @Operation(summary = "Mark a report as resolved")
    public ResponseEntity<LostFound> resolveReport(
            @PathVariable UUID reportId,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(lostFoundService.resolveReport(reportId, user.getId()));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Search nearby lost/found reports")
    public ResponseEntity<Page<LostFound>> searchNearby(
            @RequestParam String location,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lostFoundService.searchNearby(location, pageable));
    }
}
