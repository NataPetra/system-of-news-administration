package by.nata.applicationloggingstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * The `ControllersLogging` class is an Aspect class that captures and logs method
 * invocations in controllers that are annotated with the `@MethodLog` annotation.
 * It provides before and after returning advice to log request and response details.
 * <p>
 * It uses AspectJ annotations and Lombok's `@Slf4j` for logging.
 * <p>
 * Usage:
 * - Annotate your controller methods with `@MethodLog` to enable logging.
 * <p>
 * Example:
 * <pre>
 * {@literal @}Controller
 * public class MyController {
 *
 *     {@literal @}MethodLog
 *     public String handleRequest() {
 *         // Controller method implementation
 *     }
 * }
 * </pre>
 * <p>
 * This aspect captures method details and request parameters before the method
 * execution and logs the method's response after it returns.
 *
 * @see by.nata.applicationloggingstarter.annotation.MethodLog
 */
@Slf4j
@Aspect
public class ControllersLogging {

    @Pointcut("@annotation(by.nata.applicationloggingstarter.annotation.MethodLog)")
    private void annotationPointcut() {
    }

    /**
     * Logs method details and request parameters before method execution.
     *
     * @param joinPoint The JoinPoint representing the method invocation.
     */
    @Before("by.nata.applicationloggingstarter.aspect.ControllersLogging.annotationPointcut()")
    public void logMethodBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        String requestDescription = buildRequestDescription(methodName, parameterNames, arguments);

        log.info(requestDescription);
    }

    /**
     * Logs method response details after method execution.
     *
     * @param joinPoint The JoinPoint representing the method invocation.
     * @param response The method's return value.
     */
    @AfterReturning(pointcut = "by.nata.applicationloggingstarter.aspect.ControllersLogging.annotationPointcut()", returning = "response")
    public void logMethodAfter(JoinPoint joinPoint, Object response) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();

        log.info("Description of the response: method = {}, returning = {}",
                methodName, response);
    }

    private String buildRequestDescription(String methodName, String[] parameterNames, Object[] arguments) {
        StringBuilder requestDescription = new StringBuilder("Description of the request: method: " + methodName);

        for (int i = 0; i < parameterNames.length; i++) {
            requestDescription.append(", ").append(parameterNames[i]).append(" = ").append(arguments[i]);
        }

        return requestDescription.toString();
    }
}
