package aphelion.controller;

import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.CreateBlogBlockingDTO;
import aphelion.model.dto.UpdateBlogBlockingDTO;
import aphelion.service.BlogBlockingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blog-blockings")
@RequiredArgsConstructor
@Api(value = "Blog blocking controller", description = "Operations over blog blockings")
public class BlogBlockingController {
    private final BlogBlockingService blogBlockingService;

    @ApiOperation("Creates blog blocking")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Blog blocking successfully created", response = BlogBlockingDTO.class),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this action")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('USER') and " +
            "@blogBlockingPermissionResolver.canBlockUsersInBlog(#createBlogBlockingDTO.blogId)")
    @PostMapping
    public BlogBlockingDTO save(
            @ApiParam(value = "Blog blocking request", required = true)
            @RequestBody @Valid CreateBlogBlockingDTO createBlogBlockingDTO) {
        return blogBlockingService.save(createBlogBlockingDTO);
    }

    @ApiOperation("Updates blog blocking with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully updated blog blocking",
                    response = BlogBlockingDTO.class
            ),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog blocking with such id is not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canUpdateBlocking(#id)")
    @PutMapping("/{id}")
    public BlogBlockingDTO update(
            @ApiParam(value = "Id of blog blocking", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Updated data", required = true)
            @RequestBody @Valid UpdateBlogBlockingDTO updateBlogBlockingDTO) {
        return blogBlockingService.update(id, updateBlogBlockingDTO);
    }

    @ApiOperation(
            value = "Deletes blog blocking with specified id",
            notes = "Requires current user to be either owner or manager of blog"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 204,
                    message = "Successfully deleted blog blocking with specified id",
                    response = Void.class
            ),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog blocking with such id is not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canUnblockUser(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Id of blog blocking to be deleted", required = true)
            @PathVariable("id") Long id) {
        blogBlockingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "Retrieves blog blocking with specified id",
            notes = "Requires access token. Current user must be either owner of blog " +
                    "or manager of blog to perform this operation"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog blocking with specified id",
                    response = BlogBlockingDTO.class
            ),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog blocking with such id is not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canViewBlogBlocking(#id)")
    @GetMapping("/{id}")
    public BlogBlockingDTO findById(
            @ApiParam(value = "Id of blog blocking", required = true)
            @PathVariable("id") Long id) {
        return blogBlockingService.findById(id);
    }
}
