package com.datacloudchallenge.AdminCliente.domain.services;

import com.datacloudchallenge.AdminCliente.config.JwtUtils;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.AuthResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.usecase.AuthUseCase;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager userDetailsManager;

    public AuthService(
            UserRepository userRepository,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JdbcUserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }



    @Override
    public Result<AuthResponse> createUser(SignUpRequest request) {

        try {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                return Result.failure("Já existe um utilizador com este número de telefone");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                return Result.failure("Já existe um utilizador com este email");
            }

            UserModel user = SignUpRequest.signupRequestToUser(request);
            String errors = Validator.validateUserCreate(user);

            if (!errors.isEmpty()) {
                return Result.failure(errors);
            }

            String encodedPassword = passwordEncoder.encode(request.getPassword());

            UserDetails userDetails = User.withUsername(request.getPhoneNumber())
                    .password(encodedPassword)
                    .authorities(user.getAccessLevel().name())
                    .build();

            userDetailsManager.createUser(userDetails);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            user.setPassword(encodedPassword);

            UserModel userUpdated = userRepository.findByPhoneNumber(user.getPhoneNumber())
                    .orElseThrow(() -> new Exception("Utilizador não existe"));
            userUpdated.setName(user.getName());
            userUpdated.setEmail(user.getEmail());
            userUpdated.setAccessLevel(user.getAccessLevel());
            userUpdated.setLastLogin(LocalDateTime.now());

            userRepository.save(userUpdated);
            userRepository.flush();

            AuthResponse response = new AuthResponse(user.getPhoneNumber(), user.getAccessLevel(), jwtToken);

            return Result.success(response, "Utilizador criado com sucesso");

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<AuthResponse> login(LoginRequest request) {
        try {

            if (!userRepository.existsByPhoneNumber(request.getPhoneNumber()))
                return Result.failure("Não existe um utilizador com este número de telefone");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).findFirst().get();

            UserModel userToUpdate = userRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
            assert userToUpdate != null;
            userToUpdate.setLastLogin(LocalDateTime.now());
            userRepository.save(userToUpdate);

            AuthResponse response = new AuthResponse(request.getPhoneNumber(),  AccessLevel.valueOf(role), jwtToken);

            return Result.success(response, "Login efetuado com sucesso");

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<String> logout() {
        try {
            SecurityContextHolder.clearContext();
            return Result.success(null, "Logout efetuado com sucesso");
        } catch (Exception e) {
            return Result.failure("Erro ao efetuar logout: " + e.getMessage());
        }
    }


}
