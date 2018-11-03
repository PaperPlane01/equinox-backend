package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.BlogManagerNotFoundException;
import org.equinox.exception.UserNotFoundException;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.mapper.BlogManagerToManagedBlogDTOMapper;
import org.equinox.mapper.BlogManagerToManagedBlogWithBlogDTOMapper;
import org.equinox.mapper.BlogManagerToManagedBlogWithUserDTOMapper;
import org.equinox.mapper.CreateBlogManagerDTOToBlogManagerMapper;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.User;
import org.equinox.model.dto.ManagedBlogWithBlogDTO;
import org.equinox.model.dto.ManagedBlogWithUserDTO;
import org.equinox.model.dto.UpdateBlogManagerDTO;
import org.equinox.model.domain.BlogManager;
import org.equinox.model.dto.CreateBlogManagerDTO;
import org.equinox.model.dto.ManagedBlogDTO;
import org.equinox.repository.BlogManagerRepository;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.UserRepository;
import org.equinox.service.BlogManagerService;
import org.equinox.util.SortingDirectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogManagerServiceImpl implements BlogManagerService {
    private final BlogManagerRepository blogManagerRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CreateBlogManagerDTOToBlogManagerMapper createBlogManagerDTOToBlogManagerMapper;
    private final BlogManagerToManagedBlogDTOMapper blogManagerToManagedBlogDTOMapper;
    private final BlogManagerToManagedBlogWithBlogDTOMapper blogManagerToManagedBlogWithBlogDTOMapper;
    private final BlogManagerToManagedBlogWithUserDTOMapper blogManagerToManagedBlogWithUserDTOMapper;

    @Override
    public ManagedBlogDTO save(CreateBlogManagerDTO createBlogManagerDTO) {
        BlogManager blogManager = createBlogManagerDTOToBlogManagerMapper.map(createBlogManagerDTO);
        blogManager = blogManagerRepository.save(blogManager);
        return blogManagerToManagedBlogDTOMapper.map(blogManager);
    }

    @Override
    public ManagedBlogDTO update(Long id, UpdateBlogManagerDTO updateBlogManagerDTO) {
        BlogManager blogManager = findBlogManagerById(id);
        blogManager.setBlogRole(updateBlogManagerDTO.getBlogRole());
        return blogManagerToManagedBlogDTOMapper.map(blogManager);
    }

    @Override
    public void delete(Long id) {
        BlogManager blogManager = findBlogManagerById(id);
        blogManagerRepository.delete(blogManager);
    }

    @Override
    public ManagedBlogDTO findById(Long id) {
        return blogManagerToManagedBlogDTOMapper.map(findBlogManagerById(id));
    }

    @Override
    @ValidatePaginationParameters
    public List<ManagedBlogWithBlogDTO> findByUser(Long id,
                                                   @Page int page,
                                                   @PageSize(max = 50) int pageSize,
                                                   @SortingDirection String sortingDirection,
                                                   @SortBy(allowed = "id") String sortBy) {
        User user = findUserById(id);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogManagerRepository.findByUser(user, pageRequest)
                .stream()
                .map(blogManagerToManagedBlogWithBlogDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<ManagedBlogWithUserDTO> findByBlog(Long id,
                                                   @Page int page,
                                                   @PageSize(max = 50) int pageSize,
                                                   @SortingDirection String sortingDirection,
                                                   @SortBy(allowed = "id") String sortBy) {
        Blog blog = findBlogById(id);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogManagerRepository.findByBlog(blog, pageRequest)
                .stream()
                .map(blogManagerToManagedBlogWithUserDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ManagedBlogDTO> findByBlogAndUser(Long blogId, Long userId) {
        Blog blog = findBlogById(blogId);
        User user = findUserById(userId);
        return blogManagerRepository.findByUserAndBlog(user, blog)
                .stream()
                .map(blogManagerToManagedBlogDTOMapper::map)
                .collect(Collectors.toList());
    }

    private BlogManager findBlogManagerById(Long id) {
        return blogManagerRepository.findById(id)
                .orElseThrow(() -> new BlogManagerNotFoundException("Could not find blog manager " +
                        "with id " + id));
    }

    private Blog findBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new BlogNotFoundException("Could not find blog with id " +
                        id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id " + id));
    }
}
