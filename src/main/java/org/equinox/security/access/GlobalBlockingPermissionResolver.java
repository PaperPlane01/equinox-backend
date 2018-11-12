package org.equinox.security.access;

import org.equinox.security.AuthenticationFacade;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GlobalBlockingPermissionResolver {
    private AuthenticationFacade authenticationFacade;

    public boolean canViewGlobalBlockingsOfBlockedUser(Long blockedUserId) {
        return authenticationFacade.isUserAuthenticated()
                && Objects.equals(authenticationFacade.getCurrentUser().getId(), blockedUserId);
    }
}
