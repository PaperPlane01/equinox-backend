package aphelion.controller;

import aphelion.model.dto.CreateBlogPostLikeDTO;
import aphelion.model.dto.UpdatedNumberOfBlogPostLikesDTO;
import aphelion.service.BlogPostLikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blog-post-likes")
@RequiredArgsConstructor
@Api(value = "Blog post like controller", description = "Operations over blog post likes")
public class BlogPostLikeController {
    private final BlogPostLikeService blogPostLikeService;

    @ApiOperation(
            value = "Creates blog post like",
            notes = "Requires current user to be authenticated and not liked blog post " +
                    "specified in <code>blogPostId</code> property of request body"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Successfully created new blog post like",
                    response = UpdatedNumberOfBlogPostLikesDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(
                    code = 404,
                    message = "Blog post with id specified in " +
                    "<code>blogPostId</code> property of request body not found"
            )
    })
    @PreAuthorize("hasRole('USER') " +
            "and @blogPostLikePermissionResolver.canSaveBlogPostLike(#createBlogPostLikeDTO.blogPostId)")
    @PostMapping
    public UpdatedNumberOfBlogPostLikesDTO save(
            @ApiParam(
                    value = "Create blog post like request",
                    name = "Create blog post like request",
                    required = true
            )
            @RequestBody @Valid CreateBlogPostLikeDTO createBlogPostLikeDTO) {
        return blogPostLikeService.save(createBlogPostLikeDTO);
    }

    @ApiOperation(
            value = "Deletes blog post like with specified id",
            notes = "Requires current user to be authenticated and be the same user who left " +
                    "blog post like with specified id"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully deleted blog post like like",
                    response = UpdatedNumberOfBlogPostLikesDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(
                    code = 404,
                    message = "Blog post like with specified id could not be found"
            )
    })
    @PreAuthorize("hasRole('USER') and @blogPostLikePermissionResolver.canDeleteBlogPostLike(#id)")
    @DeleteMapping("/{id}")
    public UpdatedNumberOfBlogPostLikesDTO delete(
            @ApiParam(value = "Id of blog post like to be deleted", required = true)
            @PathVariable("id") Long id) {
        return blogPostLikeService.delete(id);
    }
}
