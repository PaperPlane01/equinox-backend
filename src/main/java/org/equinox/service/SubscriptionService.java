package org.equinox.service;

import org.equinox.model.domain.Blog;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CreateSubscriptionDTO;
import org.equinox.model.dto.SubscriptionDTO;
import org.equinox.model.dto.SubscriptionWithBlogDTO;
import org.equinox.model.dto.SubscriptionWithUserDTO;
import org.equinox.model.dto.UserMinifiedDTO;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDTO save(CreateSubscriptionDTO createSubscriptionDTO);
    void delete(Long id);
    SubscriptionDTO findById(Long id);
    SubscriptionWithBlogDTO findByUserAndBlog(Long userId, Long blogId);
    boolean isUserSubscribedToBlog(User user, Blog blog);
    boolean isUserSubscribedToBlog(Long userId, Long blogId);
    List<SubscriptionWithUserDTO> findByBlog(Long blogId, int page, int pageSize, String sortingDirection, String sortBy);
    List<SubscriptionWithBlogDTO> findByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy);
    List<SubscriptionWithBlogDTO> findByCurrentUser(int page, int pageSize, String sortingDirection, String sortBy);
    List<UserMinifiedDTO> findSubscribersByBlog(Long blogId, int page, int pageSize, String sortBy);
}
