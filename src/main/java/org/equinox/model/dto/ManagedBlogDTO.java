package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.BlogRole;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ManagedBlogDTO {
    private Long blogId;
    private Long userId;
    private BlogRole blogRole;
    private Long blogManagerId;
}
