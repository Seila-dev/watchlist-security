package com.kosting.authservice.controllers;

import com.kosting.authservice.core.User;
import com.kosting.authservice.dto.DataRequestLogin;
import com.kosting.authservice.dto.DataRequestSignUp;
import com.kosting.authservice.dto.DataTokenJwt;
import com.kosting.authservice.infra.config.ConfigSecurity;
import com.kosting.authservice.infra.security.TokenService;
import com.kosting.authservice.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@SecurityRequirement(name = ConfigSecurity.SECURITY)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping(("/login"))
    @Operation(summary = "User Login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity logIn(@RequestBody @Valid DataRequestLogin dataRequestLogin)
    {
        try{

            var authenticationtoken = new UsernamePasswordAuthenticationToken(dataRequestLogin.login(),dataRequestLogin.password());

            var authentication = authenticationManager.authenticate(authenticationtoken);

            var token = tokenService.generateToken((User) authentication.getPrincipal());

            return ResponseEntity.ok(new DataTokenJwt(token));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(("/signup"))
    @Operation(summary = "User Sign Up", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Usuário já cadastrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity signUp(@RequestBody @Valid DataRequestSignUp dataRequestSignUp)
    {
        var user = this.userRepository.existsByLogin(dataRequestSignUp.login());


        if(!user)
        {
            var newUser = new User(dataRequestSignUp);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(newUser);
            return ResponseEntity.ok("Cadastro realizado com sucesso!");
        }

            return ResponseEntity.badRequest().body("Usuário já cadastrado!");
    }
}
