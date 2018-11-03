package org.equinox.service;

import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.CreateBlogDTO;
import org.equinox.model.dto.UpdateBlogDTO;
import org.equinox.model.dto.UserDTO;

import java.util.List;

public interface BlogService {
    BlogDTO findById(Long id);
    BlogDTO save(CreateBlogDTO createBlogDTO);
    BlogDTO update(Long id, UpdateBlogDTO updateBlogDTO);
    void delete(Long id);
    BlogDTO restore(Long id);
    UserDTO getUserWhoDeletedBlog(Long blogId);
    List<BlogDTO> findByOwner(Long ownerId);
}
