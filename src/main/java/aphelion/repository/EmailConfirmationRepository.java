package aphelion.repository;

import aphelion.model.domain.EmailConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmation, String> {
    EmailConfirmation save(EmailConfirmation emailConfirmation);
    Optional<EmailConfirmation> findById(String id);
}
