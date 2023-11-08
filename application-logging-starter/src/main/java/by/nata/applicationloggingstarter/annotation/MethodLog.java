package by.nata.applicationloggingstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The `MethodLog` annotation is a custom annotation used to mark methods that
 * should be logged. Methods marked with this annotation will be automatically
 * tracked and recorded in the event log when called.
 * <p>
 * Usage example:
 * <pre>
 * {@literal @}MethodLog
 * public void someMethod() {
 *     // Method body
 * }
 * </pre>
 * <p>
 * This annotation is applicable only to methods and can be used in combination
 * with other tools for event logging and application monitoring.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {
}
