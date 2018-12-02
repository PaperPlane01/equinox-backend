package aphelion.model.domain;

import org.codehaus.jackson.annotate.JsonCreator;
import aphelion.exception.InvalidPublisherTypeException;

public enum BlogPostPublisherType {
    BLOG,
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
