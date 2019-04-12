package aphelion.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidPublisherTypeException;

@ApiModel("Blog post publisher type")
public enum BlogPostPublisherType {
    @ApiModelProperty("Blog post published by blog")
    BLOG,
    @ApiModelProperty("Blog post published by blog post author")
    BLOG_POST_AUTHOR;

    @JsonCreator
    public static BlogPostPublisherType fromString(String string) {
       switch (string.toLowerCase()) {
           case "blog":
               return BLOG;
           case "blog_post_author":
               return BLOG_POST_AUTHOR;
           case "blogpostauthor":
               return BLOG_POST_AUTHOR;
           default:
               throw new InvalidPublisherTypeException("Invalid publisher type, expected \"blog\" " +
                       "or \"blog_post_author\", got " + string);
       }
    }
}
