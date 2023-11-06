package by.nata.applicationloggingstarter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Slf4j
@Aspect
public class ControllersLogging {

    @Pointcut("@annotation(by.nata.applicationloggingstarter.annotation.MethodLog)")
    private void annotationPointcut() {
    }

    @Before("by.nata.applicationloggingstarter.aspect.ControllersLogging.annotationPointcut()")
    public void logMethodBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String[] parameterNames = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        String requestDescription = buildRequestDescription(methodName, parameterNames, arguments);

        log.info(requestDescription);
    }

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
