package com.jfsd.ocms.repository;

import com.jfsd.ocms.model.Enrollment;
import com.jfsd.ocms.model.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentIdOrderByEnrolledOnDescIdDesc(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    void deleteByCourseId(Long courseId);
}
