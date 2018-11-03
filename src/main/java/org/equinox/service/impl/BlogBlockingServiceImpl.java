package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.BlogBlockingNotFoundException;
import org.equinox.exception.UserNotFoundException;
import org.equinox.mapper.BlogBlockingToBlogBlockingDTOMapper;
import org.equinox.mapper.CreateBlogBlockingDTOToBlogBlockingMapper;
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
    public List<BlogBlockingDTO> findNotEndedByUser(Long userId) {
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