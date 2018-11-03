package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.BlogPostView;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.BlogPostViewRepository;
import org.equinox.service.BlogPostViewService;
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
