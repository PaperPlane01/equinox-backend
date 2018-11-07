package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.annotation.NotifySubscribers;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogBlockingNotFoundException;
import org.equinox.exception.UserNotFoundException;
import org.equinox.mapper.BlogBlockingToBlogBlockingDTOMapper;
import org.equinox.mapper.CreateBlogBlockingDTOToBlogBlockingMapper;
import org.equinox.model.domain.NotificationType;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogBlockingDTO;
import org.equinox.model.dto.CreateBlogBlockingDTO;
import org.equinox.model.dto.UpdateBlogBlockingDTO;
import org.equinox.service.BlogBlockingService;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.BlogBlocking;
import org.equinox.repository.BlogBlockingRepository;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.UserRepository;
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
                                            @PageSize(max = 50) int pageSize) throws BlogNotFoundException {
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
                                                    @PageSize(max = 50) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlogAndEndDateGreaterThan(blog, Date.from(Instant.now()), pageRequest)
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogBlockingDTO> findByBlogAndBlockedUserDisplayedUsernameContains(Long blogId, String username,
                                                                                   @Page int page,
                                                                                   @PageSize(max = 50) int pageSize) throws BlogNotFoundException {
        Blog blog = findBlogById(blogId);
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        return blogBlockingRepository.findByBlogAndBlockedUserDisplayedNameContains(blog, username, pageRequest)
                .stream()
                .map(blogBlockingToBlogBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogBlockingDTO> findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(Long blogId, String username, int page, int pageSize) throws BlogNotFoundException {
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