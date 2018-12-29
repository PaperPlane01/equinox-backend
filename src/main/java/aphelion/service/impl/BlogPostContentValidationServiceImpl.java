package aphelion.service.impl;

import aphelion.exception.BlogPostIsTooLongException;
import aphelion.exception.InvalidBlogPostContentException;
import aphelion.model.dto.BlogPostContentValidationResponse;
import aphelion.service.BlogPostContentValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@PropertySource("application.properties")
public class BlogPostContentValidationServiceImpl implements BlogPostContentValidationService {
    @Value("${node.js.server_uri}")
    private String nodeJsServerUri;

    private final String INVALID_CONTENT = "INVALID_CONTENT";
    private final String CONTENT_IS_TOO_LONG = "CONTENT_IS_TOO_LONG";

    @Override
    public String validateAndGetPlainText(Map<Object, Object> blogPostContent)
            throws InvalidBlogPostContentException, BlogPostIsTooLongException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BlogPostContentValidationResponse> response = restTemplate
                .postForEntity(nodeJsServerUri.concat("/blog-post/validation"),
                        blogPostContent,
                        BlogPostContentValidationResponse.class
                );

        if (CONTENT_IS_TOO_LONG.equals(response.getBody().getMessage())) {
            throw new BlogPostIsTooLongException("Blog post is too long");
        }

        if (INVALID_CONTENT.equals(response.getBody().getMessage())) {
            throw new InvalidBlogPostContentException("Invalid blog post content");
        }

        return response.getBody().getPlainText();
    }
}
