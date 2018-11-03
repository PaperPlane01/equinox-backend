package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BlogPostLikeDTO {
    private Long id;
    private Long blogPostId;
    private UserDTO user;
}
