package by.nata.applicationloggingstarter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;

@SpringBootTest(classes = ControllersLogging.class)
class ControllersLoggingTest {

    @SpyBean
    private ControllersLogging controllersLogging;

    public static final String METHOD_NAME = "methodName";

    @Test
    void logMethodBefore() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);

        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn(METHOD_NAME);
        String[] parameterNames = {"param1", "param2"};
        Object[] arguments = {1, "value"};
        Mockito.when(signature.getParameterNames()).thenReturn(parameterNames);
        Mockito.when(joinPoint.getArgs()).thenReturn(arguments);

        controllersLogging.logMethodBefore(joinPoint);

        Mockito.verify(controllersLogging, times(1))
                .logMethodBefore(joinPoint);
    }

    @Test
    void logMethodAfter() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);

        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn(METHOD_NAME);

        Object response = "Some Response";

        controllersLogging.logMethodAfter(joinPoint, response);

        Mockito.verify(controllersLogging, times(1))
                .logMethodAfter(joinPoint, response);
    }
}