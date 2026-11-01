package com.jfsd.shms.repository;

import com.jfsd.shms.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByOrderByCreatedAtDescIdDesc();

    List<Alert> findByPatientIdOrderByCreatedAtDescIdDesc(Long patientId);

    List<Alert> findByPatientDoctorIdOrderByCreatedAtDescIdDesc(Long doctorId);

    long countByPatientIdAndAcknowledgedFalse(Long patientId);
}
