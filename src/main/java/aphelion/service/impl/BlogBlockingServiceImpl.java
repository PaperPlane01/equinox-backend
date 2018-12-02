package aphelion.service.impl;

import aphelion.annotation.NotifySubscribers;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.exception.BlogNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.BlogBlockingToBlogBlockingDTOMapper;
import aphelion.mapper.CreateBlogBlockingDTOToBlogBlockingMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.CreateBlogBlockingDTO;
import aphelion.model.dto.UpdateBlogBlockingDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.UserRepository;
import aphelion.service.BlogBlockingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogBlockingServiceImpl implements BlogBlockingService {
    private final BlogBlockingRepository blogBlockingRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final BlogBlockingToBlogBlockingDTOMapper blogBlockingToBlogBlockingDTOMapper;
    private final CreateBlogBlockingDTOToBlogBlockingMapper createBlogBlockingDTOToBlogBlockingMapper;
    
    @Override
    public BlogBlockingDTO findById(Long id) throws BlogBlockingNotFoundException {
        return blogBlockingToBlogBlockingDTOMapper.map(findBlogBlockingById(id));
    }

    private BlogBlocking findBlogBlockingById(Long id) {
        return blogBlockingRepository.findById(id).orElseThrow(BlogBlockingNotFoundException::new);
    }

    @NotifySubscribers(type = NotificationType.BLOG_BLOCKING)
    @Override
    public BlogBlockingDTO save(CreateBlogBlockingDTO createBlogBlockingDTO) throws BlogNotFoundException, UserNotFoundException {
        BlogBlocking blogBlocking = createBlogBlockingDTOToBlogBlockingMapper.map(createBlogBlockingDTO);
        blogBlocking = blogBlockingRepository.save(blogBlocking);
        return blogBlockingToBlogBlockingDTOMapper.map(blogBlocking);
    }

    @Override
    public BlogBlockingDTO update(Long id, UpdateBlogBlockingDTO updateBlogBlockingDTO) throws BlogBlockingNotFoundException {
        BlogBlocking blogBlocking = findBlogBlockingById(id);
        blogBlocking.setReason(updateBlogBlockingDTO.getReason());
        blogBlocking.setEndDate(updateBlogBlockingDTO.getEndDate());

        blogBlocking = blogBlockingRepository.save(blogBlocking);

        return blogBlockingToBlogBlockingDTOMapper.map(blogBlocking);
    }

    @Override
    public List<BlogBlockingDTO> findNotEndedByBlockedUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
        return blogBlockingRepository.findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now()))
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        BlogBlocking blogBlocking = findBlogBlockingById(id);
        blogBlockingRepository.delete(blogBlocking);
    }

    @Override
    public boolean isUserBlockedInBlog(Long userId, Long blogId) {
        User user = findUserById(userId);
        Blog blog = findBlogById(blogId);

        return blogBlockingRepository.findAllByBlockedUserAndBlogAndEndDateGreaterThan(user, blog,
                Date.from(Instant.now())).isEmpty();
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogBlockingDTO> findByBlog(Long blogId,
                                            @Page int page,
                                            @PageSize(max = 100) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlog(blog, pageRequest)
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogBlockingDTO> findNotEndedByBlog(Long blogId,
                                                    @Page int page,
                                                    @PageSize(max = 100) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlogAndEndDateGreaterThan(blog, Date.from(Instant.now()), pageRequest)
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogBlockingDTO> findByBlogAndBlockedUserDisplayedUsernameContains(
            Long blogId,
            String username,
            @Page int page,
            @PageSize(max = 100) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlogAndBlockedUserDisplayedNameContains(blog, username, pageRequest)
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogBlockingDTO> findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(
            Long blogId,
            String username,
            @Page int page,
            @PageSize(max = 100) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlogAndBlockedUserDisplayedNameContainsAndEndDateGreaterThan(
                blog, username, Date.from(Instant.now()), pageRequest
        ).stream().map(blogBlockingToBlogBlockingDTOMapper::map).collect(Collectors.toList());
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with given id " +
                        id + " could not be found."));
    }

    private Blog findBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog with given id " +
                        id + " could not be found."));
    }
}