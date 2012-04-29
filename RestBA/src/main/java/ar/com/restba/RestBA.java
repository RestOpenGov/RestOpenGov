package ar.com.restba;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies how a Facebook JSON response attribute maps to a Java field.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface RestBA {
  /**
   * Name of the Facebook API result attribute to map to - {@code affiliation},
   * for example.
   * 
   * @return Name of the Facebook API result attribute to map to.
   */
  String value() default "";
}