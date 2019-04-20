package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Update blog post request")
public class UpdateBlogPostDTO {
    @ApiModelProperty("New title")
    private String title;
    @ApiModelProperty(value = "New content", required = true)
    @NotNull
    private Map<Object, Object> content;
    private List<String> tags;
}
