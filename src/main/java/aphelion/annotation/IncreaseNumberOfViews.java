package aphelion.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IncreaseNumberOfViews {
    For value();

    enum For {
        SINGLE_BLOG_POST,
        MULTIPLE_BLOG_POSTS,
    }
}