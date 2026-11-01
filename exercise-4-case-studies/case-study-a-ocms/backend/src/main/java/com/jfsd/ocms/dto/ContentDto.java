package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.ContentType;
import com.jfsd.ocms.model.CourseContent;

public record ContentDto(Long id, ContentType type, String title, String url) {

    public static ContentDto from(CourseContent c) {
        return new ContentDto(c.getId(), c.getType(), c.getTitle(), c.getUrl());
    }
}
