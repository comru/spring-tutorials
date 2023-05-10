package com.sbu.dj.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sbu.dj.domain.comment.Comment;
import com.sbu.dj.domain.user.UserDto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

/**
 * DTO for {@link Comment}
 */
public record CommentResponse(Long id,
                              @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
                              UserDto.Profile author,
                              Instant createdAt,
                              Instant updatedAt,
                              String body) implements Serializable {

    public record Single(CommentResponse comment) {}

    public record Multiple(Collection<CommentResponse> comments) {}
}