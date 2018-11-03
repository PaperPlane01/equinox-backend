package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.BlogRole;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBlogManagerDTO {
    private BlogRole blogRole;
}
