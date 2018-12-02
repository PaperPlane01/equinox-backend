package aphelion.service.impl;

import aphelion.exception.BlogPostNotFoundException;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostView;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.BlogPostViewRepository;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostViewServiceImpl implements BlogPostViewService {
    private final BlogPostRepository blogPostRepository;
    private final BlogPostViewRepository blogPostViewRepository;

    @Override
    public void save(Long blogPostId, Double ratio) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(BlogPostNotFoundException::new);
        BlogPostView blogPostView = new BlogPostView();
        blogPostView.setBlogPost(blogPost);
        blogPostView.setRatio(ratio);
        blogPostView.setDate(Date.from(Instant.now()));
        blogPostViewRepository.save(blogPostView);
    }
}
