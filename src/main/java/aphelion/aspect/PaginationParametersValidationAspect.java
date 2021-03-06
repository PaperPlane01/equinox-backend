package aphelion.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import aphelion.exception.InvalidPageNumberException;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.InvalidPageSizeException;
import aphelion.exception.InvalidSortByException;
import aphelion.exception.InvalidSortingDirectionException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Component
@Aspect
public class PaginationParametersValidationAspect {

    @Around("@annotation(validatePaginationParameters)")
    public Object validatePaginationParameters(
            ProceedingJoinPoint proceedingJoinPoint,
            ValidatePaginationParameters validatePaginationParameters
    ) throws Throwable {
        Annotation[][] parameterAnnotations = AOPUtils.extractParameterAnnotations(proceedingJoinPoint);
        Object[] arguments = AOPUtils.extractArguments(proceedingJoinPoint);

        for (int index = 0; index < arguments.length; index++) {
            Annotation[] annotations = parameterAnnotations[index];
            processArgument(arguments[index], annotations);
        }

        return proceedingJoinPoint.proceed();
    }

    private void processArgument(Object argument, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Page) {
                validatePage((Integer) argument);
            }

            if (annotation instanceof PageSize) {
                validatePageSize((Integer) argument, (PageSize) annotation);
            }

            if (annotation instanceof SortBy) {
                validateSortBy((String) argument, (SortBy) annotation);
            }

            if (annotation instanceof SortingDirection) {
                validateSortingDirection((String) argument, (SortingDirection) annotation);
            }
        }
    }

    private void validatePage(int page) {
        if (page < 0) {
            throw new InvalidPageNumberException("Page number must be >= 0");
        }
    }

    private void validatePageSize(int pageSize, PageSize annotation) {
        if (pageSize <= 0) {
            throw new InvalidPageSizeException("Page size must be > 0");
        }

        if (pageSize > annotation.max()) {
            throw new InvalidPageSizeException("Page size is too large, should be <= " + annotation.max());
        }
    }

    private void validateSortBy(String sortBy, SortBy annotation) {
        if (Arrays.stream(annotation.allowed()).noneMatch(sortBy::equals)) {
            throw new InvalidSortByException("sortBy parameter is invalid, must be one of the following: "
                    + Arrays.toString(annotation.allowed()));
        }
    }

    private void validateSortingDirection(String sortingDirection, SortingDirection annotation) {
        if (Arrays.stream(annotation.allowed()).noneMatch(sortingDirection::equalsIgnoreCase)) {
            throw new InvalidSortingDirectionException("sortingDirection parameter is invalid, " +
                    "must be one of the following: "
                    + Arrays.toString(annotation.allowed()));
        }
    }
}