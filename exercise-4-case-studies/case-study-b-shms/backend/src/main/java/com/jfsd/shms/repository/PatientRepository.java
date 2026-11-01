package com.jfsd.shms.repository;

import com.jfsd.shms.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findAllByOrderByNameAsc();

    List<Patient> findByDoctorIdOrderByNameAsc(Long doctorId);

    long countByDoctorId(Long doctorId);
}
