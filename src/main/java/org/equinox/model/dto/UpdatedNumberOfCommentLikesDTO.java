package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatedNumberOfCommentLikesDTO {
    private Long commentLikeId;
    private Integer updatedNumberOfLikes;
}
