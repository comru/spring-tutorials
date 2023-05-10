package com.sbu.dj.domain.comment;

import com.sbu.dj.domain.comment.dto.CommentResponse;
import com.sbu.dj.domain.user.UserMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface CommentMapper {
    CommentResponse toDto(Comment comment);
}