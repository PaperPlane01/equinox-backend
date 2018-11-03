package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.equinox.exception.EntityNotFoundExceptionFactory;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentReport;
import org.equinox.model.domain.ReportStatus;
import org.equinox.model.dto.CreateCommentReportDTO;
import org.equinox.repository.CommentRepository;
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
