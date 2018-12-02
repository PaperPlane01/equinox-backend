package aphelion.security;

import aphelion.model.domain.User;

public interface AuthenticationFacade {
    User getCurrentUser();
    boolean isUserAuthenticated();
}
