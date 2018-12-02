package aphelion.service.impl;

import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogNotFoundException;
import aphelion.exception.SubscriptionNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.SubscriptionToSubscriptionDTOMapper;
import aphelion.mapper.SubscriptionToSubscriptionWithBlogDTOMapper;
import aphelion.mapper.UserToUserMinifiedDTOMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.Subscription;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateSubscriptionDTO;
import aphelion.model.dto.SubscriptionDTO;
import aphelion.model.dto.SubscriptionWithBlogDTO;
import aphelion.model.dto.SubscriptionWithUserDTO;
import aphelion.model.dto.UserMinifiedDTO;
import aphelion.repository.BlogRepository;
import aphelion.repository.SubscriptionRepository;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import aphelion.mapper.CreateSubscriptionDTOToSubscriptionMapper;
import aphelion.mapper.SubscriptionToSubscriptionWithUserDTOMapper;
import aphelion.service.SubscriptionService;
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

    @Override
    @ValidatePaginationParameters
    public List<SubscriptionWithUserDTO> findByBlogAndUsername(Long blogId,
                                                               String username,
                                                               @Page int page,
                                                               @PageSize(max = 150) int pageSize,
                                                               @SortingDirection String sortingDirection,
                                                               @SortBy(allowed = {"id"}) String sortBy) {
        Blog blog = findBlogById(blogId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);

        return subscriptionRepository.findByBlogAndUserDisplayedNameContainsIgnoreCase(blog, username, pageRequest)
                .stream()
                .map(subscriptionToSubscriptionWithUserDTOMapper::map)
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