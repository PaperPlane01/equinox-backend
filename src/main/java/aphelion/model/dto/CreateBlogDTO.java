package aphelion.model.dto;

import aphelion.model.domain.BlogManagersVisibilityLevel;
import aphelion.model.domain.BlogPostPublisherType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogDTO {
    @NotNull
    @Size(max = 50)
    private String name;

    @Nullable
    @Size(max = 100)
    private String description;

    private String avatarUri;

    @NotNull
    private BlogPostPublisherType defaultPublisherType;

    @NotNull
    private BlogManagersVisibilityLevel blogManagersVisibilityLevel;
}
