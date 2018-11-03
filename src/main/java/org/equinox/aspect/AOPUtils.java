package org.equinox.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * This class contains utility methods for support of AOP.
 */
class AOPUtils {
    /**
     * Extracts parameter annotation from proceeding joint point.
     * @param proceedingJoinPoint Proceeding joint point to be processed.
     * @return Parameter annotations of the proceeding joint point.
     */
    static Annotation[][] extractParameterAnnotations(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getParameterAnnotations();
    }

    /**
     * Extracts arguments from proceeding joint point.
     * @param proceedingJoinPoint Proceeding joint point to be processed.
     * @return Arguments of the proceeding joint point.
     */
    static Object[] extractArguments(ProceedingJoinPoint proceedingJoinPoint) {
       return proceedingJoinPoint.getArgs();
    }
}
