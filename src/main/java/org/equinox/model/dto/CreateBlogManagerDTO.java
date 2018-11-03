package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.BlogRole;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBlogManagerDTO {
    @NotNull
    private Long userId;
    private Long blogId;

    @NotNull
    private BlogRole blogRole;
}
