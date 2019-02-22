package aphelion.controller;

import aphelion.model.dto.CreateGlobalBlockingDTO;
import aphelion.model.dto.GlobalBlockingDTO;
import aphelion.model.dto.UpdateGlobalBlockingDTO;
import aphelion.service.GlobalBlockingService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/global-blockings")
@RequiredArgsConstructor
public class GlobalBlockingController {
    private final GlobalBlockingService globalBlockingService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public GlobalBlockingDTO save(@RequestBody @Valid CreateGlobalBlockingDTO createGlobalBlockingDTO) {
        return globalBlockingService.save(createGlobalBlockingDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public GlobalBlockingDTO findById(@PathVariable("id") Long id) {
        return globalBlockingService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public GlobalBlockingDTO update(@PathVariable("id") Long id,
                                    @RequestBody @Valid UpdateGlobalBlockingDTO updateGlobalBlockingDTO) {
        return globalBlockingService.update(id, updateGlobalBlockingDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        globalBlockingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "|| @globalBlockingPermissionResolver.canViewGlobalBlockingsOfBlockedUser(#userId)")
    @GetMapping(params = {"blockedUserId", "notEndedOnly=true"})
    public List<GlobalBlockingDTO> findNotEndedByUser(@RequestParam("blockedUserId") Long userId) {
        return globalBlockingService.findNotEndedByBlockedUser(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"notEndedOnly=false"})
    public List<GlobalBlockingDTO> findAll(@RequestParam("page") Optional<Integer> page,
                                           @RequestParam("pageSize") Optional<Integer> pageSize,
                                           @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                           @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAll(page.orElse(0), pageSize.orElse(20),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"notEndedOnly=true"})
    public List<GlobalBlockingDTO> findAllNotEnded(@RequestParam("page") Optional<Integer> page,
                                                   @RequestParam("pageSize") Optional<Integer> pageSize,
                                                   @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                   @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAllNotEnded(page.orElse(0), pageSize.orElse(20),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "|| @globalBlockingPermissionResolver.canViewGlobalBlockingsOfBlockedUser(#userId)")
    @GetMapping(params = {"blockedUserId", "notEndedOnly=false"})
    public List<GlobalBlockingDTO> findAllByUser(@RequestParam("userId") Long userId,
                                                 @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("pageSize") Optional<Integer> pageSize,
                                                 @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                 @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAllByBlockedUser(userId, page.orElse(0),
                pageSize.orElse(10), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"notEndedOnly=true", "username"})
    public List<GlobalBlockingDTO> findNotEndedAndBlockedUserUsernameContains(
            @RequestParam("username") String username,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy
    ) {
        return globalBlockingService.findNotEndedAndBlockedUserUsernameContains(username,
                page.orElse(0), pageSize.orElse(10), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"notEndedOnly=false", "username"})
    public List<GlobalBlockingDTO> findAllByBlockedUserUsernameContains(
            @RequestParam("username") String username,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy
    ) {
        return globalBlockingService.findAllByBlockedUserUsernameContains(username,
                page.orElse(0), pageSize.orElse(10), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/multiple")
    public List<GlobalBlockingDTO> saveMultiple(
            @RequestBody @Valid List<CreateGlobalBlockingDTO> createGlobalBlockingDTOs) {
        return globalBlockingService.saveMultiple(createGlobalBlockingDTOs);
    }
}
