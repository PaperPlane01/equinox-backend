package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.Tag;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TagDTO {
    private long id;
    private String name;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
