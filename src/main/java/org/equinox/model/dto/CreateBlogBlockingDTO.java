package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogBlockingDTO {
    @NotNull
    private Long blockedUserId;
    @NotNull
    private Long blogId;
    @NotNull
    @Future
    private Date endDate;
    private String reason;
}
