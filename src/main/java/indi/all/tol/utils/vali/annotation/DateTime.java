package indi.all.tol.utils.vali.annotation;


import indi.all.tol.utils.vali.validator.DateTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Wenhao Huang
 * @Date: 2024/10/30 15:49
 * @Version: 1.0
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface DateTime {
    // 提示内容
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
