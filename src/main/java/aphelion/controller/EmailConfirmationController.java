package aphelion.controller;

import aphelion.service.EmailConfirmationService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/email-confirmations")
@RequiredArgsConstructor
public class EmailConfirmationController {
    private final EmailConfirmationService emailConfirmationService;

    @PreAuthorize("hasRole('USER') and @emailPermissionResolver.canActivateEmailConfirmation(#confirmationId)")
    @PostMapping("/{confirmationId}")
    public ResponseEntity<?> confirmEmail(@PathVariable String confirmationId) {
        emailConfirmationService.markEmailConfirmationAsActivated(confirmationId);
        Map<String, Object> response = ImmutableMap.of("success", true);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> sendEmailConfirmationToCurrentUser(@RequestParam(value = "language", required = false) String language) {
        emailConfirmationService.sendEmailConfirmationToCurrentUser(language);
        Map<String, Object> response = ImmutableMap.<String, Object>builder()
                .put("success", true)
                .put("message", "Confirmation email has been sent")
                .build();
        return ResponseEntity.ok(response);
    }
}
