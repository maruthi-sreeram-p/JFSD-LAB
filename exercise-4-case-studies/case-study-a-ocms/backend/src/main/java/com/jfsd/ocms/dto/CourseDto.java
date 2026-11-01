package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.Course;

import java.util.List;

public record CourseDto(Long id, String title, String description, String category,
                        Long instructorId, String instructorName, List<ContentDto> contents) {

    public static CourseDto from(Course c) {
        return new CourseDto(c.getId(), c.getTitle(), c.getDescription(), c.getCategory(),
                c.getInstructor().getId(), c.getInstructor().getName(),
                c.getContents().stream().map(ContentDto::from).toList());
    }
}
