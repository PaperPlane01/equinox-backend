package aphelion.service;

import aphelion.model.domain.Blog;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateSubscriptionDTO;
import aphelion.model.dto.SubscriptionDTO;
import aphelion.model.dto.SubscriptionWithBlogDTO;
import aphelion.model.dto.SubscriptionWithUserDTO;
import aphelion.model.dto.UserMinifiedDTO;

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
    List<SubscriptionWithUserDTO> findByBlogAndUsername(Long blogId, String username, int page, int pageSize, String sortingDirection, String sortBy);
}
