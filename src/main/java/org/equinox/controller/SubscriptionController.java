package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CreateSubscriptionDTO;
import org.equinox.model.dto.SubscriptionDTO;
import org.equinox.model.dto.SubscriptionWithBlogDTO;
import org.equinox.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PreAuthorize("hasRole('USER') and " +
            "@subscriptionPermissionResolver.canSaveSubscription(#createSubscriptionDTO.blogId)")
    @PostMapping
    public SubscriptionDTO save(@RequestBody @Valid CreateSubscriptionDTO createSubscriptionDTO) {
        return subscriptionService.save(createSubscriptionDTO);
    }

    @GetMapping(params = {"userId", "blogId"})
    public SubscriptionWithBlogDTO findByUserAndBlog(@RequestParam("userId") Long userId, @RequestParam("blogId") Long blogId) {
        return subscriptionService.findByUserAndBlog(userId, blogId);
    }

    @PreAuthorize("hasRole('USER') and @subscriptionPermissionResolver.canDeleteSubscription(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
