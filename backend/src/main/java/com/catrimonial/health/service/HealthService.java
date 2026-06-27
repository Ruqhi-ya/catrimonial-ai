package com.catrimonial.health.service;

import com.catrimonial.cat.entity.Cat;
import com.catrimonial.cat.repository.CatRepository;
import com.catrimonial.common.exception.ResourceNotFoundException;
import com.catrimonial.health.dto.*;
import com.catrimonial.health.entity.Appointment;
import com.catrimonial.health.entity.HealthRecord;
import com.catrimonial.health.entity.Vaccination;
import com.catrimonial.health.repository.AppointmentRepository;
import com.catrimonial.health.repository.HealthRecordRepository;
import com.catrimonial.health.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthService {

    private final HealthRecordRepository healthRecordRepository;
    private final VaccinationRepository vaccinationRepository;
    private final AppointmentRepository appointmentRepository;
    private final CatRepository catRepository;

    // ====== VACCINATION METHODS ======

    @Transactional
    public VaccinationResponse addVaccination(UUID userId, VaccinationRequest request) {
        Cat cat = getCatAndValidateOwnership(request.getCatId(), userId);

        Vaccination vaccination = Vaccination.builder()
                .cat(cat)
                .vaccineName(request.getVaccineName())
                .dateGiven(request.getDateGiven())
                .nextDueDate(request.getNextDueDate())
                .vetName(request.getVetName())
                .vetClinic(request.getVetClinic())
                .batchNumber(request.getBatchNumber())
                .notes(request.getNotes())
                .documentUrl(request.getDocumentUrl())
                .build();

        vaccination = vaccinationRepository.save(vaccination);

        // Update cat vaccination status
        cat.setVaccinated(true);
        catRepository.save(cat);

        log.info("Vaccination added for cat: {}", cat.getId());
        return mapVaccinationToResponse(vaccination);
    }

    @Transactional(readOnly = true)
    public Page<VaccinationResponse> getVaccinationsForCat(UUID catId, Pageable pageable) {
        return vaccinationRepository.findByCatIdOrderByDateGivenDesc(catId, pageable)
                .map(this::mapVaccinationToResponse);
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getUpcomingVaccinations(UUID userId) {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        return vaccinationRepository.findUpcomingVaccinationsForOwner(userId, today, thirtyDaysLater)
                .stream()
                .map(this::mapVaccinationToResponse)
                .collect(Collectors.toList());
    }

    // ====== HEALTH RECORD METHODS ======

    @Transactional
    public HealthRecordResponse addHealthRecord(UUID userId, HealthRecordRequest request) {
        Cat cat = getCatAndValidateOwnership(request.getCatId(), userId);

        HealthRecord record = HealthRecord.builder()
                .cat(cat)
                .recordType(request.getRecordType())
                .title(request.getTitle())
                .description(request.getDescription())
                .recordDate(request.getRecordDate())
                .vetName(request.getVetName())
                .vetClinic(request.getVetClinic())
                .attachments(request.getAttachments())
                .build();

        record = healthRecordRepository.save(record);
        log.info("Health record added for cat: {}", cat.getId());
        return mapHealthRecordToResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<HealthRecordResponse> getHealthRecordsForCat(UUID catId, String type, Pageable pageable) {
        if (type != null && !type.isBlank()) {
            HealthRecord.RecordType recordType = HealthRecord.RecordType.valueOf(type.toUpperCase());
            return healthRecordRepository.findByCatIdAndRecordTypeOrderByRecordDateDesc(catId, recordType, pageable)
                    .map(this::mapHealthRecordToResponse);
        }
        return healthRecordRepository.findByCatIdOrderByRecordDateDesc(catId, pageable)
                .map(this::mapHealthRecordToResponse);
    }

    // ====== APPOINTMENT METHODS ======

    @Transactional
    public Appointment createAppointment(UUID userId, AppointmentRequest request) {
        Cat cat = getCatAndValidateOwnership(request.getCatId(), userId);

        Appointment appointment = Appointment.builder()
                .user(cat.getOwner())
                .cat(cat)
                .title(request.getTitle())
                .description(request.getDescription())
                .appointmentDate(request.getAppointmentDate())
                .vetName(request.getVetName())
                .vetClinic(request.getVetClinic())
                .location(request.getLocation())
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);
        log.info("Appointment created for cat: {} by user: {}", cat.getId(), userId);
        return appointment;
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAppointmentsForUser(UUID userId, Pageable pageable) {
        return appointmentRepository.findByUserIdOrderByAppointmentDateDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getUpcomingAppointments(UUID userId) {
        return appointmentRepository.findUpcomingAppointments(userId, LocalDateTime.now());
    }

    @Transactional
    public Appointment updateAppointmentStatus(UUID appointmentId, UUID userId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        if (!appointment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only update your own appointments");
        }

        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status.toUpperCase()));
        return appointmentRepository.save(appointment);
    }

    // ====== HELPER METHODS ======

    private Cat getCatAndValidateOwnership(UUID catId, UUID userId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Cat", "id", catId));

        if (!cat.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You can only manage health records for your own cats");
        }
        return cat;
    }

    private VaccinationResponse mapVaccinationToResponse(Vaccination v) {
        boolean isOverdue = v.getNextDueDate() != null && v.getNextDueDate().isBefore(LocalDate.now());
        return VaccinationResponse.builder()
                .id(v.getId())
                .catId(v.getCat().getId())
                .catName(v.getCat().getName())
                .vaccineName(v.getVaccineName())
                .dateGiven(v.getDateGiven())
                .nextDueDate(v.getNextDueDate())
                .vetName(v.getVetName())
                .vetClinic(v.getVetClinic())
                .batchNumber(v.getBatchNumber())
                .notes(v.getNotes())
                .documentUrl(v.getDocumentUrl())
                .isOverdue(isOverdue)
                .createdAt(v.getCreatedAt())
                .build();
    }

    private HealthRecordResponse mapHealthRecordToResponse(HealthRecord r) {
        return HealthRecordResponse.builder()
                .id(r.getId())
                .catId(r.getCat().getId())
                .catName(r.getCat().getName())
                .recordType(r.getRecordType().name())
                .title(r.getTitle())
                .description(r.getDescription())
                .recordDate(r.getRecordDate())
                .vetName(r.getVetName())
                .vetClinic(r.getVetClinic())
                .attachments(r.getAttachments())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
