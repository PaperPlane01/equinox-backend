package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CreateGlobalBlockingDTO;
import org.equinox.model.dto.GlobalBlockingDTO;
import org.equinox.model.dto.UpdateGlobalBlockingDTO;
import org.equinox.service.GlobalBlockingService;
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
    @PutMapping("/{id}")
    public GlobalBlockingDTO update(@PathVariable("id") Long id,
                                    @RequestBody @Valid UpdateGlobalBlockingDTO updateGlobalBlockingDTO) {
        return globalBlockingService.update(id, updateGlobalBlockingDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        globalBlockingService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"userId", "nonExpired=true"})
    public List<GlobalBlockingDTO> findNonExpiredByUser(@RequestParam("userId") Long userId) {
        return globalBlockingService.findNonExpiredByUser(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"userId", "nonExpired=false"})
    public List<GlobalBlockingDTO> findAllByUser(@RequestParam("userId") Long userId,
                                                 @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("pageSize") Optional<Integer> pageSize,
                                                 @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                 @RequestParam("sortBy") Optional<String> sortBy) {
        return globalBlockingService.findAllByUser(userId, page.orElse(0), pageSize.orElse(100),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }
}
