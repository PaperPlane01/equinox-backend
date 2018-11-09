package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.UserNotFoundException;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.mapper.BlogToBlogDTOMapper;
import org.equinox.mapper.BlogToBlogMinifiedDTOMapper;
import org.equinox.mapper.CreateBlogDTOToBlogMapper;
import org.equinox.mapper.UserToUserDTOMapper;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.BlogMinifiedDTO;
import org.equinox.model.dto.CreateBlogDTO;
import org.equinox.model.dto.UpdateBlogDTO;
import org.equinox.model.dto.UserDTO;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.UserRepository;
import org.equinox.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogToBlogDTOMapper blogToBlogDTOMapper;
    private final BlogToBlogMinifiedDTOMapper blogToBlogMinifiedDTOMapper;
    private final CreateBlogDTOToBlogMapper createBlogDTOToBlogMapper;
    private final UserToUserDTOMapper userToUserDTOMapper;

    @Override
    public BlogDTO findById(Long id) {
        return blogToBlogDTOMapper.map(findBlogById(id));
    }

    @Override
    public BlogMinifiedDTO findMinifiedById(Long id) {
        return blogToBlogMinifiedDTOMapper.map(findBlogById(id));
    }

    private Blog findBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Blog with given id "
                        + id + " could not be found."));
    }

    private Blog findDeletedBlogById(Long id) {
        return blogRepository.findDeletedById(id)
                .orElseThrow(() -> new BlogNotFoundException("Could not find deleted blog with " +
                        "id " + id + ". The blog is either not deleted, or do not exists."));
    }

    @Override
    public BlogDTO save(CreateBlogDTO createBlogDTO) {
        Blog blog = createBlogDTOToBlogMapper.map(createBlogDTO);
        blog = blogRepository.save(blog);
        return blogToBlogDTOMapper.map(blog);
    }

    @Override
    public BlogDTO update(Long id, UpdateBlogDTO updateBlogDTO) {
        Blog blog = findBlogById(id);
        blog.setDescription(updateBlogDTO.getDescription());
        blog.setName(updateBlogDTO.getName());
        blog.setBlogManagersVisibilityLevel(updateBlogDTO.getBlogManagersVisibilityLevel());
        blog.setDefaultPublisherType(updateBlogDTO.getDefaultPublisherType());
        blog = blogRepository.save(blog);
        return blogToBlogDTOMapper.map(blog);
    }

    @Override
    public void delete(Long id) {
        Blog blog = findBlogById(id);
        blogRepository.delete(blog);
    }

    @Override
    public BlogDTO restore(Long id) {
        Blog blog = findDeletedBlogById(id);
        blog.setDeleted(false);
        blog.setDeletedAt(null);
        blog.setDeletedBy(null);
        blog = blogRepository.save(blog);
        return blogToBlogDTOMapper.map(blog);
    }

    @Override
    public UserDTO getUserWhoDeletedBlog(Long blogId) {
        Blog blog = findDeletedBlogById(blogId);
        return userToUserDTOMapper.map(blog.getDeletedBy());
    }

    @Override
    public List<BlogDTO> findByOwner(Long ownerId) {
        User user = findUserById(ownerId);
        return blogRepository.findByOwner(user)
                .stream()
                .map(blogToBlogDTOMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));

    }
}