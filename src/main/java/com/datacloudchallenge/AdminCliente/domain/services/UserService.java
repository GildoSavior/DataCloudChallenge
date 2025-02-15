package com.datacloudchallenge.AdminCliente.domain.services;

import com.datacloudchallenge.AdminCliente.config.JwtUtils;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.mappers.UserMapper;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UpdateUserClientResquest;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager userDetailsManager;

    public UserService(
            UserRepository userRepository,
            JwtUtils jwtUtils,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JdbcUserDetailsManager userDetailsManager) {
        this.repository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public Result<List<UserDto>> findAll() {
        try {
            List<UserDto> allUsers = repository.findAll()
                    .stream()
                    .map(UserDto::userToUserDto)
                    .collect(Collectors.toList());


            return  Result.success(allUsers);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> findUserById(Long id) {
        try {

            if(id.toString().isBlank())
                return Result.failure("O id deve estar preenchido");

            UserModel user = repository.findById(id).orElseThrow(() -> new Exception("Não existe utilizador com esse id: ${id} !"));
            return  Result.success(UserDto.userToUserDto(user));

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> findUserByEmail(String email) {
        try {

            if(email.isBlank())
               return Result.failure("O emal deve estar preenchido");

            UserModel user = repository.findByEmail(email)
                    .orElseThrow(() -> new Exception("Não existe nenhum utilizador com este email: ${email"));

            return  Result.success(UserDto.userToUserDto(user));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> createUser(UserDto request) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");


            if(request.getAccessLevel().equals(AccessLevel.ROLE_SUPER_ADMIN))
                return Result.failure("Só pode ter um super admin no sistema");

            if(AccessLevel.valueOf(role) == AccessLevel.ROLE_ADMIN &&
                    !request.getAccessLevel().equals(AccessLevel.ROLE_CLIENT))
                return Result.failure("Você tem permissão apenas para criar clientes");

            var user = UserMapper.userDtoToUser(request);
            String errors = Validator.validateUserCreate(user);

            if(!errors.isEmpty()) return Result.failure(errors);

            if(repository.existsByPhoneNumber(request.getPhoneNumber())) return Result.failure("Já existe um utilizador com este numero");

            if(repository.existsByEmail(request.getEmail())) return Result.failure("Já existe um utilizador com este email");

            String encodedPassword = passwordEncoder.encode(request.getPassword());

            UserDetails userDetails = User.withUsername(request.getPhoneNumber())
                    .password(encodedPassword)
                    .authorities(AccessLevel.ROLE_CLIENT.name())
                    .build();

            userDetailsManager.createUser(userDetails);

            UserModel userUpdated = repository.findByPhoneNumber(user.getPhoneNumber())
                    .orElseThrow(() -> new Exception("Utilizador não existe"));
            userUpdated.setName(user.getName());
            userUpdated.setEmail(user.getEmail());
            userUpdated.setImageUrl(user.getImageUrl());

            repository.save(userUpdated);

            return Result.success(request);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> updateUser(String phoneNumber, UpdateUserClientResquest user) {
        try {

            String errors = Validator.validateUserUpdate(user);

            if (!errors.isEmpty()) return Result.failure(errors);

            UserModel userToUpdate = repository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new Exception("Este utilizador não existe"));

            userToUpdate.setName(user.getName());
            userToUpdate.setImageUrl(user.getImageUrl());
            repository.save(userToUpdate);

            return Result.success(UserDto.userToUserDto(userToUpdate));

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result<String> deleteUser(String phoneNumber) {
        try {
            if(!repository.existsByPhoneNumber(phoneNumber)) return  Result.failure("Este não existe utilizador");

            repository.deleteByPhoneNumber(phoneNumber);

            return Result.success("Utilizador eliminado com sucesso");

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }
    @Override
    public Result<UserModel> findUserByPhoneNumber(String phoneNumber) {
        try {

            if(phoneNumber.isBlank())
               return Result.failure("O numero de telefone deve estar preenchido");

            UserModel user = repository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new Exception("Não existe nenhum utilizador com este número: ${phoneNumber}"));

            return  Result.success(user);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
