package com.kosting.authservice.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Senha inválida: mínimo 6 caracteres, com pelo menos 1 número e 1 caractere especial.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

