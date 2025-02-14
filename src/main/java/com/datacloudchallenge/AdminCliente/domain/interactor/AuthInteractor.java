package com.datacloudchallenge.AdminCliente.domain.interactor;

import com.datacloudchallenge.AdminCliente.config.JwtUtils;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.mappers.UserMapper;
import com.datacloudchallenge.AdminCliente.data.models.User;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpResponse;
import com.datacloudchallenge.AdminCliente.domain.usecase.AuthUseCase;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthInteractor implements AuthUseCase {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Result<SignUpResponse> signUp(SignUpRequest request) {
        try {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
                return Result.failure("Já existe um utilizador com este número de telefone");

            User user = UserMapper.toUser(request);

            String errors = Validator.validateUser(user);
            if (!errors.isEmpty()) {
                return Result.failure(errors);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            User userCreated = userRepository.save(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            AccessLevel accessLevel = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(AccessLevel::valueOf)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Nível de acesso inválido"));

            SignUpResponse response = new SignUpResponse(userCreated, jwtToken);
            return Result.success(response);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }


    @Override
    public Result<LoginResponse> login(LoginRequest request) {
        return null;
    }
}
