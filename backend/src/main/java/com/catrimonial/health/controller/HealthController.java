package com.catrimonial.health.controller;

import com.catrimonial.auth.security.UserPrincipal;
import com.catrimonial.health.dto.*;
import com.catrimonial.health.entity.Appointment;
import com.catrimonial.health.service.HealthService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health Management", description = "Cat health records, vaccinations, and appointments")
public class HealthController {

    private final HealthService healthService;

    // ====== VACCINATION ENDPOINTS ======

    @PostMapping("/vaccinations")
    @Operation(summary = "Add a vaccination record")
    public ResponseEntity<VaccinationResponse> addVaccination(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody VaccinationRequest request) {
        VaccinationResponse response = healthService.addVaccination(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/vaccinations/cat/{catId}")
    @Operation(summary = "Get all vaccinations for a cat")
    public ResponseEntity<Page<VaccinationResponse>> getVaccinations(
            @PathVariable UUID catId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(healthService.getVaccinationsForCat(catId, pageable));
    }

    @GetMapping("/vaccinations/upcoming")
    @Operation(summary = "Get upcoming vaccinations for user's cats")
    public ResponseEntity<List<VaccinationResponse>> getUpcomingVaccinations(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(healthService.getUpcomingVaccinations(user.getId()));
    }

    // ====== HEALTH RECORD ENDPOINTS ======

    @PostMapping("/records")
    @Operation(summary = "Add a health record")
    public ResponseEntity<HealthRecordResponse> addHealthRecord(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody HealthRecordRequest request) {
        HealthRecordResponse response = healthService.addHealthRecord(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/records/cat/{catId}")
    @Operation(summary = "Get health records for a cat")
    public ResponseEntity<Page<HealthRecordResponse>> getHealthRecords(
            @PathVariable UUID catId,
            @RequestParam(required = false) String type,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(healthService.getHealthRecordsForCat(catId, type, pageable));
    }

    // ====== APPOINTMENT ENDPOINTS ======

    @PostMapping("/appointments")
    @Operation(summary = "Create an appointment")
    public ResponseEntity<Appointment> createAppointment(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = healthService.createAppointment(user.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @GetMapping("/appointments")
    @Operation(summary = "Get all appointments for the user")
    public ResponseEntity<Page<Appointment>> getAppointments(
            @AuthenticationPrincipal UserPrincipal user,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(healthService.getAppointmentsForUser(user.getId(), pageable));
    }

    @GetMapping("/appointments/upcoming")
    @Operation(summary = "Get upcoming appointments")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(healthService.getUpcomingAppointments(user.getId()));
    }

    @PatchMapping("/appointments/{appointmentId}/status")
    @Operation(summary = "Update appointment status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable UUID appointmentId,
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam String status) {
        Appointment appointment = healthService.updateAppointmentStatus(appointmentId, user.getId(), status);
        return ResponseEntity.ok(appointment);
    }
}
