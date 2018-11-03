package org.equinox.exceptionhandler;

import org.equinox.exception.InvalidPageNumberException;
import org.equinox.exception.InvalidReportReasonException;
import org.equinox.model.dto.ErrorDTO;
import org.equinox.exception.InvalidBlogRoleException;
import org.equinox.exception.InvalidCommentsDisplayModeException;
import org.equinox.exception.InvalidPageSizeException;
import org.equinox.exception.InvalidPublisherTypeException;
import org.equinox.exception.InvalidReportStatusException;
import org.equinox.exception.InvalidSortByException;
import org.equinox.exception.InvalidSortingDirectionException;
import org.equinox.exception.LoginUsernameIsAlreadyInUseException;
import org.equinox.exception.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
            InvalidCommentsDisplayModeException.class
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

    private ErrorDTO createErrorDTO(Integer status, RuntimeException exception) {
        return new ErrorDTO(status, exception.getClass().getSimpleName(), exception.getMessage());
    }
}
