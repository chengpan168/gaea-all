package com.gaea.common.annotation;

import java.lang.annotation.*;

/**
 * Created by Fe on 15/9/8.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CsrfTokenAnnotation {
    boolean checked() default  true;
}
