package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import aphelion.exception.EntityNotFoundExceptionFactory;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentReport;
import aphelion.model.domain.ReportStatus;
import aphelion.model.dto.CreateCommentReportDTO;
import aphelion.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateCommentReportDTOToCommentReportMapper {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityNotFoundExceptionFactory entityNotFoundExceptionFactory;

    @BeanMapping(resultType = CommentReport.class)
    @Mapping(source = "description", target = "content")
    public abstract CommentReport map(CreateCommentReportDTO createCommentReportDTO);

    @BeforeMapping
    protected void setFields(CreateCommentReportDTO createCommentReportDTO,
                             @MappingTarget CommentReport commentReport) {
        commentReport.setStatus(ReportStatus.NOT_VIEWED);
        commentReport.setComment(commentRepository.findById(createCommentReportDTO.getCommentId())
                .orElseThrow(() -> entityNotFoundExceptionFactory.create(Comment.class,
                        createCommentReportDTO.getCommentId())));
    }
}
