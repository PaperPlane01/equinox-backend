package aphelion.security.access;

import aphelion.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GlobalBlockingPermissionResolver {
    private final AuthenticationFacade authenticationFacade;

    public boolean canViewGlobalBlockingsOfBlockedUser(Long blockedUserId) {
        return authenticationFacade.isUserAuthenticated()
                && Objects.equals(authenticationFacade.getCurrentUser().getId(), blockedUserId);
    }
}
