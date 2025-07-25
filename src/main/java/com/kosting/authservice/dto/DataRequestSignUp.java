package com.kosting.authservice.dto;

import com.kosting.authservice.util.ValidPassword;
import jakarta.validation.constraints.Email;

public record DataRequestSignUp(@Email String login, @ValidPassword String password) {
}
