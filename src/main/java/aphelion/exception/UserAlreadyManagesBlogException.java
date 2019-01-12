package aphelion.exception;

import aphelion.model.dto.BlogManagerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyManagesBlogException extends RuntimeException {
    private BlogManagerDTO blogManager;

    public UserAlreadyManagesBlogException() {
    }

    public UserAlreadyManagesBlogException(String message) {
        super(message);
    }

    public UserAlreadyManagesBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyManagesBlogException(String message, BlogManagerDTO blogManager) {
        super(message);
        this.blogManager = blogManager;
    }
}
