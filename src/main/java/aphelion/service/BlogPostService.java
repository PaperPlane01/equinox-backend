package aphelion.service;

import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.CreateBlogPostDTO;
import aphelion.model.dto.UpdateBlogPostDTO;
import aphelion.model.dto.UserDTO;
import aphelion.model.dto.BlogPostMinifiedDTO;

import java.util.Date;
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
    UserDTO findAuthorOfBlogPost(Long blogPostId);
    List<BlogPostDTO> getFeed(int page, int pageSize);
    List<BlogPostDTO> getMostPopularForWeek(int page, int pageSize);
    List<BlogPostDTO> getMostPopularForMonth(int page, int pageSize);
    List<BlogPostDTO> getMostPopularForYear(int page, int pageSize);
    List<BlogPostDTO> getMostPopularForPeriod(Date from, Date to, int page, int pageSize);
    List<BlogPostDTO> findPinnedByBlog(Long blogId);
    BlogPostDTO pin(Long blogPostId);
    BlogPostDTO unpin(Long blogPostId);
}
