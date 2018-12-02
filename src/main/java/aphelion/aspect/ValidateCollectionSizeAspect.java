package aphelion.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import aphelion.annotation.ValidateCollectionSize;
import aphelion.annotation.CollectionArgument;
import aphelion.exception.InvalidCollectionSizeException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

@Aspect
@Component
public class ValidateCollectionSizeAspect {

    @Around("@annotation(validateCollectionSize)")
    public Object validateCollectionSize(ProceedingJoinPoint proceedingJoinPoint,
                                         ValidateCollectionSize validateCollectionSize) throws Throwable {
        Annotation[][] parameterAnnotations = AOPUtils
                .extractParameterAnnotations(proceedingJoinPoint);
        Object[] arguments = AOPUtils.extractArguments(proceedingJoinPoint);

        for (int index = 0; index < arguments.length; index++) {
            Annotation[] annotations = parameterAnnotations[index];
            processArgument(arguments[index], annotations);
        }

        return proceedingJoinPoint.proceed();
    }

    private void processArgument(Object argument, Annotation[] annotations) {
        Arrays.stream(annotations).forEach(annotation -> {
            if (annotation instanceof CollectionArgument) {
                CollectionArgument collectionArgument = (CollectionArgument) annotation;
                int minSize = collectionArgument.minSize();
                int maxSize = collectionArgument.maxSize();
                Collection collection = (Collection) argument;

                if (collection.size() < minSize) {
                    throw new InvalidCollectionSizeException("Invalid collection size: collection " +
                            "is too small. Minimum size is: " + minSize);
                }

                if (collection.size() > maxSize) {
                    throw new InvalidCollectionSizeException("Invalid collection size: collection " +
                            "is too large. Maximum size is " + maxSize);
                }
            }
        });
    }
}
