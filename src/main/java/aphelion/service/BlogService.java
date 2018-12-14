package aphelion.service;

import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.CreateBlogDTO;
import aphelion.model.dto.UpdateBlogDTO;
import aphelion.model.dto.UserDTO;

import java.util.List;

public interface BlogService {
    BlogDTO findById(Long id);
    BlogMinifiedDTO findMinifiedById(Long id);
    BlogDTO save(CreateBlogDTO createBlogDTO);
    BlogDTO update(Long id, UpdateBlogDTO updateBlogDTO);
    void delete(Long id);
    BlogDTO restore(Long id);
    UserDTO getUserWhoDeletedBlog(Long blogId);
    List<BlogDTO> findByOwner(Long ownerId);
    List<BlogMinifiedDTO> findMinifiedByOwner(Long ownerId);
    List<BlogMinifiedDTO> findMinifiedByCurrentUser();
}
