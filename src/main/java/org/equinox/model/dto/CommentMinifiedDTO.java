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
    private Date createdAt;
    private String content;
}
