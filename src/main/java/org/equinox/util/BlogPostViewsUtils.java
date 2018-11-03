package org.equinox.util;

import org.equinox.model.domain.BlogPostView;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class BlogPostViewsUtils {
    public static int countNumberOfViews(Collection<BlogPostView> blogPostViews,
                                         int approximateNumberOfViewsCountedPerItem) {
        return (int) Math.round(blogPostViews.stream()
                .map(BlogPostView::getRatio)
                .map(ratio -> (1 / (1 - ratio))
                        * (approximateNumberOfViewsCountedPerItem
                        - ratio * approximateNumberOfViewsCountedPerItem))
                .mapToDouble(numberOfViewsNotRounded -> numberOfViewsNotRounded)
                .sum());
    }

    public static double generateBlogPostViewRatio(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static boolean shouldPersistBlogPostView(double blogPostViewRatio,
                                                    int approximateNumberOfViewsCountedPerItem) {
        return blogPostViewRatio < 1.0 / approximateNumberOfViewsCountedPerItem;
    }
}
