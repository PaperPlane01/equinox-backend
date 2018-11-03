package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.CommentReport;
import org.equinox.model.dto.CommentReportDTO;

@Mapper(uses = CommentToCommentMinifiedDTOMapper.class)
public interface CommentReportToCommentReportDTOMapper {
    @BeanMapping(resultType = CommentReportDTO.class)
    CommentReportDTO map(CommentReport commentReport);
}
