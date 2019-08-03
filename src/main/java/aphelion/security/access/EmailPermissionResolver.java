package aphelion.security.access;

import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.EmailConfirmationDTO;
import aphelion.service.EmailConfirmationService;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailPermissionResolver {
    private final UserService userService;
    private final EmailConfirmationService emailConfirmationService;

    public boolean canActivateEmailConfirmation(String confirmationId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        EmailConfirmationDTO emailConfirmation = emailConfirmationService.findById(confirmationId);
        return Objects.equals(emailConfirmation.getUserId(), currentUser.getId());
    }
}
