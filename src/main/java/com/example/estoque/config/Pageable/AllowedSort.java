package com.example.estoque.config.Pageable;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AllowedSort {
    String[] value();
    String defaultProp() default "createdAt";
    String defaultDir() default "DESC";
}
