package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.UserNotFoundException;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.exception.SubscriptionNotFoundException;
import org.equinox.mapper.CreateSubscriptionDTOToSubscriptionMapper;
import org.equinox.mapper.SubscriptionToSubscriptionDTOMapper;
import org.equinox.mapper.SubscriptionToSubscriptionWithBlogDTOMapper;
import org.equinox.mapper.SubscriptionToSubscriptionWithUserDTOMapper;
import org.equinox.mapper.UserToUserMinifiedDTOMapper;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.Subscription;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CreateSubscriptionDTO;
import org.equinox.model.dto.SubscriptionDTO;
import org.equinox.model.dto.SubscriptionWithBlogDTO;
import org.equinox.model.dto.SubscriptionWithUserDTO;
import org.equinox.model.dto.UserMinifiedDTO;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.SubscriptionRepository;
import org.equinox.repository.UserRepository;
import org.equinox.security.AuthenticationFacade;
import org.equinox.service.SubscriptionService;
import org.equinox.util.SortingDirectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final SubscriptionToSubscriptionDTOMapper subscriptionToSubscriptionDTOMapper;
    private final SubscriptionToSubscriptionWithBlogDTOMapper subscriptionToSubscriptionWithBlogDTOMapper;
    private final SubscriptionToSubscriptionWithUserDTOMapper subscriptionToSubscriptionWithUserDTOMapper;
    private final CreateSubscriptionDTOToSubscriptionMapper createSubscriptionDTOToSubscriptionMapper;
    private final UserToUserMinifiedDTOMapper userToUserMinifiedDTOMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public SubscriptionDTO save(CreateSubscriptionDTO createSubscriptionDTO) {
        Subscription subscription = createSubscriptionDTOToSubscriptionMapper.map(createSubscriptionDTO);
        subscription = subscriptionRepository.save(subscription);
        return subscriptionToSubscriptionDTOMapper.map(subscription);
    }

    @Override
    public void delete(Long id) {
        Subscription subscription = findSubscriptionById(id);
        subscriptionRepository.delete(subscription);
    }

    @Override
    public SubscriptionDTO findById(Long id) {
        Subscription subscription = findSubscriptionById(id);
        return subscriptionToSubscriptionDTOMapper.map(subscription);
    }

    @Override
    public SubscriptionWithBlogDTO findByUserAndBlog(Long userId, Long blogId) {
        User user = findUserById(userId);
        Blog blog = findBlogById(blogId);

        return subscriptionToSubscriptionWithBlogDTOMapper.map(subscriptionRepository.findByBlogAndUser(blog, user)
                .orElseThrow(() -> new SubscriptionNotFoundException("User with id " + userId
                        + " is not subscribed to blog with id " + blogId)));
    }

    private Subscription findSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription with given id "
                        + id + " could not be found."));
    }

    @Override
    public boolean isUserSubscribedToBlog(User user, Blog blog) {
        return subscriptionRepository.findByBlogAndUser(blog, user).isPresent();
    }

    @Override
    public boolean isUserSubscribedToBlog(Long userId, Long blogId) {
        Blog blog = findBlogById(blogId);
        User user = findUserById(userId);
        return isUserSubscribedToBlog(user, blog);
    }

    @Override
    @ValidatePaginationParameters
    public List<SubscriptionWithUserDTO> findByBlog(Long blogId,
                                                    @Page int page,
                                                    @PageSize(max = 150) int pageSize,
                                                    @SortingDirection String sortingDirection,
                                                    @SortBy(allowed = "id") String sortBy) {
        Blog blog = findBlogById(blogId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return subscriptionRepository.findByBlog(blog, pageRequest)
                .stream()
                .map(subscriptionToSubscriptionWithUserDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<SubscriptionWithBlogDTO> findByUser(Long userId,
                                                    @Page int page,
                                                    @PageSize(max = 150) int pageSize,
                                                    @SortingDirection String sortingDirection,
                                                    @SortBy(allowed = "id") String sortBy) {
        User user = findUserById(userId);
        return findSubscriptionsByUser(user, page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(subscriptionToSubscriptionWithBlogDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<SubscriptionWithBlogDTO> findByCurrentUser(@Page int page,
                                                           @PageSize(max = 150) int pageSize,
                                                           @SortingDirection String sortingDirection,
                                                           @SortBy(allowed = "id") String sortBy) {
        User currentUser = authenticationFacade.getCurrentUser();
        return findSubscriptionsByUser(currentUser, page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(subscriptionToSubscriptionWithBlogDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<UserMinifiedDTO> findSubscribersByBlog(Long blogId,
                                                       @Page int page,
                                                       @PageSize(max = 150) int pageSize,
                                                       @SortBy(allowed = {"id", "random"}) String sortBy) {
        Blog blog = findBlogById(blogId);

        if (sortBy.equalsIgnoreCase("random")) {
            int maxPage = blog.getSubscriptions().size() / pageSize;
            if (maxPage == 0) {
                page = 1;
            } else {
                page = ThreadLocalRandom.current().nextInt(1, maxPage);
            }
        }

        List<Subscription> subscriptions = subscriptionRepository.findByBlog(blog, PageRequest.of(page, pageSize));

        return subscriptions.stream()
                .map(subscription -> userToUserMinifiedDTOMapper.map(subscription.getUser()))
                .collect(Collectors.toList());
    }

    private List<Subscription> findSubscriptionsByUser(User user, int page, int pageSize, String sortingDirection,
                                                       String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return subscriptionRepository.findByUser(user, pageRequest);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
    }

    private Blog findBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with given id "
                        + blogId + " could not be found."));
    }
}