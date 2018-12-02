package aphelion.aspect;

import aphelion.async.executor.AsyncExecutor;
import aphelion.model.dto.BlogPostDTO;
import aphelion.util.BlogPostViewsUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import aphelion.service.BlogPostViewService;
import aphelion.annotation.IncreaseNumberOfViews;
import aphelion.exception.BlogPostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Aspect
@Component
@PropertySource("application.properties")
public class IncreaseBlogPostViewsCountAspect {
    @Autowired
    private BlogPostViewService blogPostViewService;

    @Autowired
    private AsyncExecutor asyncExecutor;

    @Value("${blogpostview.min_ratio}")
    private double minRatio;

    @Value("${blogpostview.max_ratio}")
    private double maxRatio;

    @Value("${blogpostview.approximate_number_of_views_counted_per_item}")
    private int approximateNumberOfViewsCountedPerItem;

    @AfterReturning(pointcut = "@annotation(increaseNumberOfViews)", returning = "result")
    public void increaseNumberOfViews(IncreaseNumberOfViews increaseNumberOfViews,
                                      Object result) {
        asyncExecutor.execute(() -> {
            switch (increaseNumberOfViews.value()) {
                case SINGLE_BLOG_POST:
                    BlogPostDTO blogPost = (BlogPostDTO) result;
                    increaseNumberOfViewsForSingleBlogPost(blogPost);
                    break;
                case MULTIPLE_BLOG_POSTS:
                    List<BlogPostDTO> blogPosts = (List<BlogPostDTO>) result;
                    increaseNumberOfViewsForMultipleBlogPosts(blogPosts);
                    break;
                default:
                    break;
            }
        });

    }

    private void increaseNumberOfViewsForMultipleBlogPosts(Collection<BlogPostDTO> blogPostDTOs) {
        if (!blogPostDTOs.isEmpty()) {
            blogPostDTOs.forEach(this::increaseNumberOfViewsForSingleBlogPost);
        }
    }

    private void increaseNumberOfViewsForSingleBlogPost(BlogPostDTO blogPostDTO) {
        if (blogPostDTO == null) {
            return;
        }

        double ratio = BlogPostViewsUtils.generateBlogPostViewRatio(minRatio, maxRatio);

        if (BlogPostViewsUtils.shouldPersistBlogPostView(ratio, approximateNumberOfViewsCountedPerItem)) {
            try {
                blogPostViewService.save(blogPostDTO.getId(), ratio);
            } catch (BlogPostNotFoundException e) {
                //Blog post probably has been deleted — can't do anything about that.
            }
        }
    }
}