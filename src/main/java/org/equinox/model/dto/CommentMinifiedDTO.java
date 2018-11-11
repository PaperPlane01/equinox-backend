package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentMinifiedDTO {
    private Long id;
    private UserDTO author;
    private Long blogPostId;
    private Date createdAt;
    private String content;
}
