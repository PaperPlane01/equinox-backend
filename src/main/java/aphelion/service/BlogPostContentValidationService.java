package aphelion.service;

import aphelion.exception.BlogPostIsTooLongException;
import aphelion.exception.InvalidBlogPostContentException;

import java.util.Map;

public interface BlogPostContentValidationService {
    String validateAndGetPlainText(Map<Object, Object> blogPostContent)
            throws InvalidBlogPostContentException, BlogPostIsTooLongException;
}
