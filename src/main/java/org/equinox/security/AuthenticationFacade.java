package org.equinox.security;

import org.equinox.model.domain.User;

public interface AuthenticationFacade {
    User getCurrentUser();
    boolean isUserAuthenticated();
}
