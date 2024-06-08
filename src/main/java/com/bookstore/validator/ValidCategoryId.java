package com.bookstore.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidCategoryIdValidator.class)
public @interface ValidCategoryId {
String message() default "Danh mục sách không hợp lệ";
Class<?>[] groups() default {};
Class<? extends Payload>[] payload() default {};
}
