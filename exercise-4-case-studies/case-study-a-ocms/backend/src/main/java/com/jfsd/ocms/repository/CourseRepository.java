package com.jfsd.ocms.repository;

import com.jfsd.ocms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByOrderByIdDesc();

    List<Course> findByInstructorIdOrderByIdDesc(Long instructorId);
}
