package aphelion.service.impl;

import aphelion.exception.BlogNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.BlogToBlogDTOMapper;
import aphelion.mapper.BlogToBlogMinifiedDTOMapper;
import aphelion.mapper.CreateBlogDTOToBlogMapper;
import aphelion.mapper.UserToUserDTOMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.CreateBlogDTO;
import aphelion.model.dto.UpdateBlogDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.BlogRepository;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogService;
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
    private final AuthenticationFacade authenticationFacade;

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

    @Override
    public List<BlogMinifiedDTO> findMinifiedByOwner(Long ownerId) {
        User user = findUserById(ownerId);
        return blogRepository.findByOwner(user)
                .stream()
                .map(blogToBlogMinifiedDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogMinifiedDTO> findMinifiedByCurrentUser() {
        User user = authenticationFacade.getCurrentUser();
        return blogRepository.findByOwner(user)
                .stream()
                .map(blogToBlogMinifiedDTOMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));

    }
}