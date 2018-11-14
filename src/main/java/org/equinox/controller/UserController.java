package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CreateStandardUserDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.CurrentUserFullProfileDTO;
import org.equinox.model.dto.GlobalBlockingDTO;
import org.equinox.model.dto.SubscriptionWithBlogDTO;
import org.equinox.model.dto.UpdateUserDTO;
import org.equinox.model.dto.UserDTO;
import org.equinox.service.GlobalBlockingService;
import org.equinox.service.SubscriptionService;
import org.equinox.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final GlobalBlockingService globalBlockingService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/current")
    public CurrentUserDTO currentUser() {
        return userService.getCurrentUser();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/current/full-profile")
    public CurrentUserFullProfileDTO getCurrentUserFullProfile() {
        return userService.getCurrentUserFullProfile();
    }

    @GetMapping("/{userId}/subscriptions")
    public List<SubscriptionWithBlogDTO> findSubscriptions(@PathVariable("userId") Long userId,
                                                           @RequestParam("page") Optional<Integer> page,
                                                           @RequestParam("pageSize") Optional<Integer> pageSize,
                                                           @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                           @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findByUser(userId, page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @GetMapping("/current/subscriptions")
    public List<SubscriptionWithBlogDTO> findSubscriptionsOfCurrentUser(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findByCurrentUser(page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @GetMapping(params = {"displayedNameContains"})
    public List<UserDTO> findByDisplayedNameContains(@RequestParam("displayedNameContains") String displayedNameContains,
                                                     @RequestParam("page") Optional<Integer> page,
                                                     @RequestParam("pageSize") Optional<Integer> pageSize,
                                                     @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                     @RequestParam("sortBy") Optional<String> sortBy) {
        return userService.findByDisplayedNameContains(displayedNameContains, page.orElse(0), pageSize.orElse(10),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/current")
    public CurrentUserFullProfileDTO updateCurrentUser(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        return userService.updateCurrentUser(updateUserDTO);
    }

    @GetMapping("/username/{username}/is-available")
    public ResponseEntity<?> assertThatUsernameIsNotInUse(@PathVariable("username") @NotBlank String username) {
        userService.assertThatUsernameIsNotInUse(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping(params = "authType=usernameAndPassword")
    public UserDTO signUp(@RequestBody @Valid CreateStandardUserDTO createStandardUserDTO) {
        return userService.saveStandardUser(createStandardUserDTO);
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "|| @globalBlockingPermissionResolver.canViewGlobalBlockingsOfBlockedUser(#blockedUserId)")
    @GetMapping("/{blockedUserId}/global-blockings")
    public List<GlobalBlockingDTO> findAllGlobalBlockingsByBlockedUser(
            @PathVariable("blockedUserId") Long blockedUserId,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAllByBlockedUser(blockedUserId,
                page.orElse(0), pageSize.orElse(100), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "|| @globalBlockingPermissionResolver.canViewGlobalBlockingsOfBlockedUser(#blockedUserId)")
    @GetMapping("/{blockedUserId}/global-blockings/not-ended")
    public List<GlobalBlockingDTO> findNotEndedGlobalBlockingsByBlockedUser(
            @PathVariable("blockedUserId") Long blockedUserId) {
        return globalBlockingService.findNotEndedByBlockedUser(blockedUserId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/global-blockings/created")
    public List<GlobalBlockingDTO> findGlobalBlockingsCreatedByUser(
            @PathVariable("userId") Long userId,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAllCreatedByUser(userId, page.orElse(0),
                pageSize.orElse(50), sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/global-blockings/created/not-ended")
    public List<GlobalBlockingDTO> findNotEndedGlobalBlockingsCreatedByUser(
            @PathVariable("userId") Long userId) {
        return globalBlockingService.findNotEndedAndCreatedByUser(userId);
    }
}