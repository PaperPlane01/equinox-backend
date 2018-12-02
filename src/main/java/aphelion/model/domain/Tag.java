package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.dto.TagDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tagSequenceGenerator")
    @SequenceGenerator(
            name = "tagSequenceGenerator",
            sequenceName = "tagSequence",
            allocationSize = 1
    )
    private long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public Tag(TagDTO tagDTO) {
        this.name = tagDTO.getName();
    }
}
