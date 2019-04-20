package aphelion.model.dto;

import aphelion.model.domain.BlogPostPublisherType;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Request to create new blog post")
public class CreateBlogPostDTO {
    @ApiModelProperty("Title of blog post")
    private String title;
    @ApiModelProperty(value = "Id of blog", required = true)
    @NotNull
    private Long blogId;
    @ApiModelProperty(value = "Content of blog", required = true)
    @NotNull
    private Map<Object, Object> content;
    @ApiModelProperty("Tags of blog")
    private List<String> tags;
    @ApiModelProperty("Publisher type of blog")
    private BlogPostPublisherType publishedBy;
}
