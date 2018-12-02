package aphelion.exception;

public class PersonalInformationNotFoundException extends EntityNotFoundException {
    public PersonalInformationNotFoundException() {
    }

    public PersonalInformationNotFoundException(String message) {
        super(message);
    }

    public PersonalInformationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
