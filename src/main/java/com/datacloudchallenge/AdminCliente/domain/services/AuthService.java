package com.datacloudchallenge.AdminCliente.domain.services;

import com.datacloudchallenge.AdminCliente.config.JwtUtils;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.mappers.UserMapper;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.login.LoginResponse;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpRequest;
import com.datacloudchallenge.AdminCliente.domain.dtos.auth.signup.SignUpResponse;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;

    public Result<SignUpResponse> signUp(SignUpRequest request) {

        try {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                return Result.failure("Já existe um utilizador com este número de telefone");
            }

            UserModel user = UserMapper.toUser(request);
            String errors = Validator.validateUser(user);

            if (!errors.isEmpty()) {
                return Result.failure(errors);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(true);

            UserModel userCreated = userRepository.save(user);
            userRepository.flush();

            JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
            userDetailsManager.setCreateUserSql("INSERT INTO users (phone_number, password, enabled) VALUES (?, ?, ?)");
            userDetailsManager.setCreateAuthoritySql("INSERT INTO authorities (phone_number, authority) VALUES (?,?)");

            UserDetails userDetails = User.withUsername(userCreated.getPhoneNumber())
                    .password(userCreated.getPassword())
                    .roles(AccessLevel.CLIENT.name())
                    .build();

            userDetailsManager.createUser(userDetails);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCreated.getPhoneNumber(), userCreated.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            SignUpResponse response = new SignUpResponse(userCreated, jwtToken);

            return Result.success(response);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<LoginResponse> login(LoginRequest request) {
        try {

            if (!userRepository.existsByPhoneNumber(request.getPhoneNumber()))
                return Result.failure("Não existe um utilizador com este número de telefone");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

}
