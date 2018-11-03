package org.equinox.service;

import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostMinifiedDTO;
import org.equinox.model.dto.CreateBlogPostDTO;
import org.equinox.model.dto.UpdateBlogPostDTO;

import java.util.List;

public interface BlogPostService {
    BlogPostDTO save(CreateBlogPostDTO createBlogPostDTO);
    BlogPostDTO update(Long id, UpdateBlogPostDTO updateBlogPostDTO);
    BlogPostDTO findById(Long id);
    BlogPostDTO findDeletedById(Long id);
    List<BlogPostMinifiedDTO> findDeletedByCurrentUserInBlog(Long blogId, int page, int pageSize, String sortingDirection, String sortBy);
    void delete(Long id);
    BlogPostDTO restore(Long id);
    List<BlogPostDTO> findByBlog(Long blogId, int page, int pageSize, String sortingDirection, String sortBy);
}
