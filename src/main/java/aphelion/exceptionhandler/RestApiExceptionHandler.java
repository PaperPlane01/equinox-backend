package aphelion.exceptionhandler;

import aphelion.exception.BlogPostIsTooLongException;
import aphelion.exception.BlogPostValidationException;
import aphelion.exception.GoogleLoginException;
import aphelion.exception.InvalidBlogPostContentException;
import aphelion.exception.UserAlreadyManagesBlogException;
import aphelion.exception.InvalidPageNumberException;
import aphelion.exception.InvalidReportReasonException;
import aphelion.exception.InvalidBlogRoleException;
import aphelion.exception.InvalidCommentsDisplayModeException;
import aphelion.exception.InvalidPageSizeException;
import aphelion.exception.InvalidPublisherTypeException;
import aphelion.exception.InvalidReportStatusException;
import aphelion.exception.InvalidSortByException;
import aphelion.exception.InvalidSortingDirectionException;
import aphelion.exception.LoginUsernameIsAlreadyInUseException;
import aphelion.exception.EntityNotFoundException;
import aphelion.model.dto.ErrorDTO;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Component
@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(RuntimeException exception,
                                                                   WebRequest webRequest) {
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.NOT_FOUND.value(), exception);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.NOT_FOUND,
                webRequest);
    }

    @ExceptionHandler(value = {
            InvalidBlogRoleException.class,
            InvalidPageNumberException.class,
            InvalidPageSizeException.class,
            InvalidPublisherTypeException.class,
            InvalidReportReasonException.class,
            InvalidReportStatusException.class,
            InvalidSortByException.class,
            InvalidSortingDirectionException.class,
            InvalidCommentsDisplayModeException.class,
            InvalidBlogPostContentException.class,
            BlogPostIsTooLongException.class
    })
    protected ResponseEntity<Object> handleInvalidDataException(RuntimeException exception,
                                                                WebRequest webRequest) {
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.BAD_REQUEST.value(), exception);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST,
                webRequest);
    }

    @ExceptionHandler(value = LoginUsernameIsAlreadyInUseException.class)
    protected ResponseEntity<Object> handleLoginUsernameIsAlreadyInUseException(RuntimeException exception,
                                                                                WebRequest webRequest) {
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.CONFLICT.value(), exception);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.CONFLICT,
                webRequest);
    }

    @ExceptionHandler(value = GoogleLoginException.class)
    protected ResponseEntity<?> handleGoogleLoginException(RuntimeException exception,
                                                           WebRequest webRequest) {
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    @ExceptionHandler(value = BlogPostValidationException.class)
    protected ResponseEntity<?> handleBlogPostValidationException(RuntimeException exception,
                                                                  WebRequest webRequest) {
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                webRequest);
    }

    @ExceptionHandler(value = UserAlreadyManagesBlogException.class)
    protected ResponseEntity<?> handleUserAlreadyManagesBlogException(UserAlreadyManagesBlogException exception,
                                                                      WebRequest webRequest) {
        Map<Object, Object> additionalInformation = ImmutableMap
                .builder()
                .put("blogManager", exception.getBlogManager())
                .build();
        ErrorDTO errorDTO = createErrorDTO(HttpStatus.CONFLICT.value(), exception, additionalInformation);
        return handleExceptionInternal(exception, errorDTO, new HttpHeaders(), HttpStatus.CONFLICT,
                webRequest);
    }

    private ErrorDTO createErrorDTO(Integer status, RuntimeException exception) {
        return new ErrorDTO(status, exception.getClass().getSimpleName(), exception.getMessage(), null);
    }

    private ErrorDTO createErrorDTO(Integer status, RuntimeException exception, Map<Object, Object> additionalInformation) {
        return new ErrorDTO(status, exception.getClass().getSimpleName(), exception.getMessage(), additionalInformation);
    }
}
