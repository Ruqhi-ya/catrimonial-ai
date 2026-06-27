package com.catrimonial.health.repository;

import com.catrimonial.health.entity.Vaccination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, UUID> {

    Page<Vaccination> findByCatIdOrderByDateGivenDesc(UUID catId, Pageable pageable);

    List<Vaccination> findByCatId(UUID catId);

    @Query("SELECT v FROM Vaccination v WHERE v.nextDueDate <= :date AND v.nextDueDate >= :today")
    List<Vaccination> findUpcomingVaccinations(@Param("today") LocalDate today, @Param("date") LocalDate date);

    @Query("SELECT v FROM Vaccination v WHERE v.cat.owner.id = :ownerId AND v.nextDueDate <= :date AND v.nextDueDate >= :today")
    List<Vaccination> findUpcomingVaccinationsForOwner(@Param("ownerId") UUID ownerId,
                                                       @Param("today") LocalDate today,
                                                       @Param("date") LocalDate date);
}
