package com.echoss.svc.common.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParams {

  /**
   * The name of the Request attribute to bind to.
   */
  //String value() default "";

  /**
   * Whether the parameter is required.
   * Default is true, leading to an exception thrown in case
   * of the parameter missing in the request. Switch this to
   * false if you prefer a
   * null in case of the parameter missing.
   * Alternatively, provide a {@link #defaultValue() defaultValue},
   * which implicitely sets this flag to false.
   */
  boolean required() default false;

  /**
   * The default value to use as a fallback. Supplying a default value
   * implicitely sets {@link #required()} to false.
   */
  //String defaultValue() default "";
}
