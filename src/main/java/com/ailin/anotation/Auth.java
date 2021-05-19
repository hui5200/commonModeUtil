package com.ailin.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    /**
     * 默认不授权
     * @return
     */
    boolean isAuth() default false;

    /**
     * 授权模式
     * @return
     */
    String mode() default "";
}
