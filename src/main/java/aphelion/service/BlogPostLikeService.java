package aphelion.service;

import aphelion.exception.BlogPostLikeNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogPostLikeDTO;
import aphelion.model.dto.CreateBlogPostLikeDTO;
import aphelion.model.dto.UpdatedNumberOfBlogPostLikesDTO;

import java.util.List;

/**
 * Contains methods for operating over blog post likes and associated DTOs.
 */
public interface BlogPostLikeService {
    /**
     * Saves blog post like and returns
     * {@link UpdatedNumberOfBlogPostLikesDTO} object.
     * @param createBlogPostLikeDTO {@link CreateBlogPostLikeDTO}
     * instance.
     * @return Object which contains updated number of likes and id of saved blog post like.
     * @throws BlogPostNotFoundException Thrown if there is no blog post with id specified in
     * {@link CreateBlogPostLikeDTO#blogPostId}
     */
    UpdatedNumberOfBlogPostLikesDTO save(CreateBlogPostLikeDTO createBlogPostLikeDTO) throws BlogPostNotFoundException;

    /**
     * Deletes blog post like and returns
     * {@link UpdatedNumberOfBlogPostLikesDTO} object.
     * @param id Id of blog post like which is to be deleted.
     * @return Object which contains updated number of likes.
     * @throws BlogPostLikeNotFoundException Thrown if there is no blog post like with specified id.
     */
    UpdatedNumberOfBlogPostLikesDTO delete(Long id) throws BlogPostLikeNotFoundException;

    /**
     * Returns likes of blog post with specified id.
     * @param blogPostId Id of blog post.
     * @param page Page of likes.
     * @param pageSize Size of the page.
     * @param sortingDirection Direction of the sorting. Allowed values: asc, desc.
     * @param sortBy Property which blog post likes will be sorted by. Allowed value: id.
     * @return Likes of blog post.
     * @throws BlogPostNotFoundException Thrown if there is no blog post with the specified id.
     */
    List<BlogPostLikeDTO> findByBlogPost(Long blogPostId, int page, int pageSize, String sortingDirection, String sortBy) throws BlogPostNotFoundException;

    /**
     * Returns blog post likes of user with specified id.
     * @param userId Id of user.
     * @param page Page of likes.
     * @param pageSize Size of the page.
     * @param sortingDirection Direction of the sorting. Allowed values: asc, desc.
     * @param sortBy Property which blog post likes will be sorted by. Allowed value: id.
     * @return Blog post likes of user.
     * @throws UserNotFoundException Thrown if there is no user with such id.
     */
    List<BlogPostLikeDTO> findByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy) throws UserNotFoundException;
    BlogPostLikeDTO findById(Long id) throws BlogPostLikeNotFoundException;
    int countByBlogPost(Long blogPostId) throws BlogPostNotFoundException;

    /**
     *
     * @param blogPost
     * @param user
     * @return
     */
    @Deprecated
    boolean isBlogPostLikedByUser(BlogPost blogPost, User user);
}
