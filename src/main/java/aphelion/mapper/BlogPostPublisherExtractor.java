package aphelion.mapper;

import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostPublisherType;
import aphelion.model.dto.BlogPostPublisher;
import org.springframework.stereotype.Component;

@Component
public class BlogPostPublisherExtractor {
    public BlogPostPublisher getPublisher(BlogPost blogPost) {
        BlogPostPublisher blogPostPublisher = new BlogPostPublisher();

        if (blogPost.getPublishedBy().equals(BlogPostPublisherType.BLOG_POST_AUTHOR)) {
            blogPostPublisher.setId(blogPost.getAuthor().getId());
            blogPostPublisher.setAvatarUri(blogPost.getAuthor().getAvatarUri());
            blogPostPublisher.setLetterAvatarColor(blogPost.getAuthor().getLetterAvatarColor());
            blogPostPublisher.setDisplayedName(blogPost.getAuthor().getDisplayedName());
            blogPostPublisher.setType(blogPost.getPublishedBy());
        } else {
            blogPostPublisher.setId(blogPost.getBlog().getId());
            blogPostPublisher.setDisplayedName(blogPost.getBlog().getName());
            blogPostPublisher.setAvatarUri(blogPost.getBlog().getAvatarUri());
            blogPostPublisher.setLetterAvatarColor(blogPost.getBlog().getLetterAvatarColor());
            blogPostPublisher.setType(blogPost.getPublishedBy());
        }

        return blogPostPublisher;
    }
}
