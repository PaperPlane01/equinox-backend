package aphelion.service;

import aphelion.model.domain.User;
import aphelion.model.dto.EmailConfirmationDTO;

public interface EmailConfirmationService {
    void sendEmailConfirmationEmail(Long userId, String language);
    void sendEmailConfirmationEmail(User user, String language);
    void sendEmailConfirmationToCurrentUser(String language);
    void markEmailConfirmationAsActivated(String confirmationId);
    EmailConfirmationDTO findById(String id);
}
