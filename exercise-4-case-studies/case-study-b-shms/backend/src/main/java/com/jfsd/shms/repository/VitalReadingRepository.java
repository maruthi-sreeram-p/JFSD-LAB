package com.jfsd.shms.repository;

import com.jfsd.shms.model.VitalReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VitalReadingRepository extends JpaRepository<VitalReading, Long> {

    List<VitalReading> findByPatientIdOrderByRecordedAtDescIdDesc(Long patientId, Pageable pageable);

    Optional<VitalReading> findFirstByPatientIdOrderByRecordedAtDescIdDesc(Long patientId);
}
