package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.CommentReport;
import aphelion.model.dto.CommentReportDTO;

@Mapper(uses = CommentToCommentMinifiedDTOMapper.class)
public interface CommentReportToCommentReportDTOMapper {
    @BeanMapping(resultType = CommentReportDTO.class)
    CommentReportDTO map(CommentReport commentReport);
}
