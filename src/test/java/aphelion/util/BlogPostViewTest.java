package aphelion.util;

import org.junit.Test;
import aphelion.model.domain.BlogPostView;
import aphelion.util.BlogPostViewsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlogPostViewTest {
    private final int APPROXIMATE_NUMBER_OF_VIEWS_RECORDED = 7;
    private final int NUMBER_OF_VIEWS = 100;

    private List<BlogPostView> blogPostViews() {
        List<BlogPostView> blogPostViews = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_VIEWS; i++) {
            blogPostViews.add(createBlogPostView());
        }

        return blogPostViews;
    }

    private BlogPostView createBlogPostView() {
        BlogPostView blogPostView = new BlogPostView();
        double ratio = BlogPostViewsUtils.generateBlogPostViewRatio(0, 1);
        System.out.println(ratio);

        if (BlogPostViewsUtils.shouldPersistBlogPostView(ratio, APPROXIMATE_NUMBER_OF_VIEWS_RECORDED)) {
            blogPostView.setRatio(ratio);
            return blogPostView;
        } else {
            return null;
        }
    }

    @Test
    public void testViewsCounter() {
        List<BlogPostView> blogPostViews = blogPostViews()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        int numberOfViews = BlogPostViewsUtils
                .countNumberOfViews(blogPostViews, APPROXIMATE_NUMBER_OF_VIEWS_RECORDED);
        System.out.println(numberOfViews);
    }
}
