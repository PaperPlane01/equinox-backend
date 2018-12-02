package aphelion.service;

import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.CreateBlogBlockingDTO;
import aphelion.model.dto.UpdateBlogBlockingDTO;
import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.exception.BlogNotFoundException;

import java.util.List;

/**
 * Contains methods for operating over <code>BlogBlocking</code> objects and associated DTOs.
 */
public interface BlogBlockingService {
    /**
     * Returns blog blocking with given id.
     * @param id Id of blog blocking.
     * @return Blog blocking with given id.
     * @throws BlogBlockingNotFoundException Thrown if there is no blog blocking with such id.
     */
    BlogBlockingDTO findById(Long id) throws BlogBlockingNotFoundException;

    /**
     * Saves blog blocking and returns saved object.
     * @param createBlogBlockingDTO {@link CreateBlogBlockingDTO} instance.
     * @return Saved blog blocking.
     * @throws BlogNotFoundException Thrown if there is no blog with id specified in
     * {@link CreateBlogBlockingDTO#blogId}
     * @throws UserNotFoundException Thrown if there is no user with id specified in
     * {@link CreateBlogBlockingDTO#blockedUserId}
     */
    BlogBlockingDTO save(CreateBlogBlockingDTO createBlogBlockingDTO) throws BlogNotFoundException, UserNotFoundException;

    /**
     * Updates blog blocking and returns updated object.
     * @param id Id of blog blocking to be updated.
     * @param updateBlogBlockingDTO {@link UpdateBlogBlockingDTO instance}
     * @return Id of blog blocking to be updated.
     * @throws BlogBlockingNotFoundException Thrown if there is no blog blocking with specified id.
     */
    BlogBlockingDTO update(Long id, UpdateBlogBlockingDTO updateBlogBlockingDTO) throws BlogBlockingNotFoundException;

    /**
     * Returns not ended blog blockings of user with given id.
     * @param userId Id of user.
     * @return Not ended blog blockings of user with given id.
     * @throws UserNotFoundException Thrown if there is no user with such id.
     */
    List<BlogBlockingDTO> findNotEndedByBlockedUser(Long userId) throws UserNotFoundException;

    /**
     * Deletes blog blocking with specified id.
     * @param id Id of blog blocking to be deleted.
     * @throws BlogBlockingNotFoundException Thrown if there is no blog blocking with such id.
     */
    void delete(Long id) throws BlogBlockingNotFoundException;

    /**
     * Allows to know whether user is blocked in blog or not.
     * @param userId Id of user.
     * @param blogId Id of blog.
     * @return <Code>True</Code> if the specified user blocked in the specified blog,
     * <Code>false</Code> if not.
     * @throws UserNotFoundException Thrown if there is no user with such id.
     * @throws BlogNotFoundException Thrown if there is no blog with such id.
     */
    boolean isUserBlockedInBlog(Long userId, Long blogId) throws UserNotFoundException, BlogNotFoundException;

    /**
     * Returns list of blog blockings belonging to a blog with the specified id.
     * @param blogId Id of blog.
     * @param page Page number.
     * @param pageSize Page size.
     * @return List of blog blockings belonging to a blog with the specified id.
     * @throws BlogNotFoundException Thrown if there is no blog with such id.
     */
    List<BlogBlockingDTO> findByBlog(Long blogId, int page, int pageSize) throws BlogNotFoundException;

    /**
     * Returns list of not ended blog blockings belonging to a blog with the specified id.
     * @param blogId Id of blog.
     * @param page Page number.
     * @param pageSize Page size.
     * @return List of not ended blog blockings belonging to a blog with the specified id.
     * @throws BlogNotFoundException Thrown if there is no blog with such id.
     */
    List<BlogBlockingDTO> findNotEndedByBlog(Long blogId, int page, int pageSize) throws BlogNotFoundException;

    /**
     * Returns list of blog blockings belonging to a blog with the specified id and blocked user's
     * displayed username containing specified string.
     * @param blogId Id of blog.
     * @param username Page number.
     * @param page Page.
     * @param pageSize Page size.
     * @return List of blog blockings belonging to a blog with the specified id and blocked user's
     * displayed username containing specified string.
     * @throws BlogNotFoundException Thrown if there is no blog with such id.
     */
    List<BlogBlockingDTO> findByBlogAndBlockedUserDisplayedUsernameContains(Long blogId, String username,
                                                                            int page, int pageSize)
            throws BlogNotFoundException;

    List<BlogBlockingDTO> findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(Long blogId, String username,
                                                                                    int page, int pageSize)
            throws BlogNotFoundException;
}
