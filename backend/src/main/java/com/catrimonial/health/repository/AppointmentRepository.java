package com.catrimonial.health.repository;

import com.catrimonial.health.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Page<Appointment> findByUserIdOrderByAppointmentDateDesc(UUID userId, Pageable pageable);

    Page<Appointment> findByUserIdAndStatusOrderByAppointmentDateAsc(UUID userId, Appointment.AppointmentStatus status, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId AND a.appointmentDate >= :now AND a.status = 'SCHEDULED' ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointments(@Param("userId") UUID userId, @Param("now") LocalDateTime now);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :start AND :end AND a.reminderSent = false AND a.status = 'SCHEDULED'")
    List<Appointment> findAppointmentsNeedingReminder(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
