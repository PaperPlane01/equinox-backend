package aphelion.service.impl;

import aphelion.exception.EmailConfirmationExpiredException;
import aphelion.exception.EmailConfirmationHasAlreadyBeenActivatedException;
import aphelion.exception.EmailConfirmationNotFoundException;
import aphelion.exception.EmailNotSpecifiedException;
import aphelion.exception.EmailSendingException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.EmailConfirmationToEmailConfirmationDTOMapper;
import aphelion.model.domain.EmailConfirmation;
import aphelion.model.domain.User;
import aphelion.model.dto.EmailConfirmationDTO;
import aphelion.repository.EmailConfirmationRepository;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.EmailConfirmationService;
import aphelion.service.TimeStampProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:email.properties")
public class EmailConfirmationServiceImpl implements EmailConfirmationService {
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final TemplateEngine templateEngine;
    private final TimeStampProvider timeStampProvider;
    private final JavaMailSender javaMailSender;
    private final EmailConfirmationToEmailConfirmationDTOMapper emailConfirmationToEmailConfirmationDTOMapper;
    @Value("${mail.fullAddress}")
    private String aphelionEmailAddress;

    @Override
    public void sendEmailConfirmationEmail(Long userId, String language) {
        User user = findUserById(userId);
        sendEmailConfirmationEmail(user, language);
    }

    @Override
    public void sendEmailConfirmationToCurrentUser(String language) {
        User currentUser = authenticationFacade.getCurrentUser();
        sendEmailConfirmationEmail(currentUser, language);
    }

    @Override
    public void sendEmailConfirmationEmail(User user, String language) {
        boolean isRussianLanguage = "ru".equalsIgnoreCase(language);
        String email = user.getPersonalInformation().getEmail();
        LocalDateTime now = timeStampProvider.now();
        LocalDateTime expirationDate = now.plusDays(5);

        if (email == null) {
            throw new EmailNotSpecifiedException("User " + user.getDisplayedName() + " does not " +
                    "have e-mail to confirm");
        }

        EmailConfirmation emailConfirmation = EmailConfirmation.builder()
                .activated(false)
                .user(user)
                .activated(false)
                .creationDate(Date.from(now.toInstant(ZoneOffset.UTC)))
                .expirationDate(Date.from(expirationDate.toInstant(ZoneOffset.UTC)))
                .user(user)
                .email(email)
                .build();

        emailConfirmation = emailConfirmationRepository.save(emailConfirmation);

        Context context = new Context();
        context.setVariable("expirationDate", Date.from(expirationDate.toInstant(ZoneOffset.UTC)));
        context.setVariable("confirmationId", emailConfirmation.getId());

        String emailText;

        if ("ru".equalsIgnoreCase(language)) {
            emailText = templateEngine.process("emailConfirmationTemplate_ru.html", context);
        } else {
            emailText = templateEngine.process("emailConfirmationTemplate_en.html", context);
        }

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setText("", emailText);
            mimeMessageHelper.setFrom(aphelionEmailAddress);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(isRussianLanguage ? "Подтверждение e-mail" : "Confirm your email");
            mimeMessageHelper.setText(emailText, true);
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException exception) {
            throw new EmailSendingException("Could not send e-mail to specified address", exception);
        }
    }

    @Override
    public void markEmailConfirmationAsActivated(String confirmationId) {
        EmailConfirmation emailConfirmation = findEmailConfirmationById(confirmationId);
        if (emailConfirmation.getExpirationDate().before(Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC)))) {
            throw new EmailConfirmationExpiredException("Email confirmation has expired");
        }

        if (emailConfirmation.isActivated()) {
            throw new EmailConfirmationHasAlreadyBeenActivatedException("Email confirmation has already been " +
                    "activated");
        }

        emailConfirmation.setActivated(true);
    }

    @Override
    public EmailConfirmationDTO findById(String id) {
        return emailConfirmationToEmailConfirmationDTOMapper.map(findEmailConfirmationById(id));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id " + userId));
    }

    private EmailConfirmation findEmailConfirmationById(String id) {
        return emailConfirmationRepository.findById(id)
                .orElseThrow(() -> new EmailConfirmationNotFoundException("Could not find email " +
                        "confirmation with id " + id));
    }
}
