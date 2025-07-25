package com.kosting.authservice.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        String regex = "^(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{6,}$";
        return password.matches(regex);
    }
}

