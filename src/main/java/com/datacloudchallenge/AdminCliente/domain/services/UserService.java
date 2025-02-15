package com.datacloudchallenge.AdminCliente.domain.services;

import com.datacloudchallenge.AdminCliente.config.JwtUtils;
import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.mappers.UserMapper;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import com.datacloudchallenge.AdminCliente.domain.utils.SecurityUtil;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
            String authenticatedPhoneNumber = SecurityUtil.getAuthenticatedPhoneNumber();
            List<UserDto> allUsers = repository.findAll()
                    .stream()
                    .map(UserDto::userToUserDto)
                    .sorted((u1, u2) -> u1.getPhoneNumber().equals(authenticatedPhoneNumber) ? -1 : (u2.getPhoneNumber().equals(authenticatedPhoneNumber) ? 1 : 0))
                    .collect(Collectors.toList());



            return  Result.success(allUsers, "Dados carregados com sucesso");
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
            return  Result.success(UserDto.userToUserDto(user), "Dados carregados com sucesso");

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

            return  Result.success(UserDto.userToUserDto(user), "Dados carregados com sucesso");
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> createUser(UserDto request) {
        try {

            String role = SecurityUtil.getRole();

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
            userUpdated.setAccessLevel(user.getAccessLevel());

            repository.save(userUpdated);

            return Result.success(request, "Utilizador criado com sucesso");

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> updateUser(String userToUpdatePhoneNumber, UserDto resquest) {
        try {
            String errors = Validator.validateUserUpdate(resquest);
            if (!errors.isEmpty()) return Result.failure(errors);

            boolean isUpdatingOwnInfo = userToUpdatePhoneNumber.equals(SecurityUtil.getAuthenticatedPhoneNumber());
            AccessLevel authenticatedUserRole = AccessLevel.valueOf(SecurityUtil.getRole());

            UserModel userToUpdate = getTargetUser(userToUpdatePhoneNumber);

            if (!hasPermission(isUpdatingOwnInfo, userToUpdate.getAccessLevel())) {
                return Result.failure("Você não tem permissão para realizar essa ação.");
            }

            if (getAuthenticatedUserRole() == AccessLevel.ROLE_SUPER_ADMIN) {
                userToUpdate.setAccessLevel(resquest.getAccessLevel());
            }

            userToUpdate.setName(resquest.getName());
            userToUpdate.setImageUrl(resquest.getImageUrl());
            repository.save(userToUpdate);

            return Result.success(UserDto.userToUserDto(userToUpdate), "Utilizador atualizado com sucesso");

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result<String> deleteUserByPhoneNumber(String phoneNumber) {
        try {
            if (!repository.existsByPhoneNumber(phoneNumber)) {
                return Result.failure("Este utilizador não existe");
            }

            boolean isUpdatingOwnInfo = phoneNumber.equals(SecurityUtil.getAuthenticatedPhoneNumber());

            AccessLevel userToDeleteAccessLevel = getTargetUser(phoneNumber).getAccessLevel();

            if (!hasPermission(isUpdatingOwnInfo, userToDeleteAccessLevel)) {
                return Result.failure("Você não tem permissão para realizar essa ação.");
            }

            repository.deleteByPhoneNumber(phoneNumber);
            return Result.success(null, "Utilizador eliminado com sucesso");

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserDto> findUserByPhoneNumber(String phoneNumber) {
        try {
            if (phoneNumber.isBlank()) return Result.failure("O número de telefone deve estar preenchido");

            boolean isUpdatingOwnInfo = phoneNumber.equals(SecurityUtil.getAuthenticatedPhoneNumber());

            AccessLevel userAccessLevel = getTargetUser(phoneNumber).getAccessLevel();

            if (!hasPermission(isUpdatingOwnInfo, userAccessLevel)) {
                return Result.failure("Você não tem permissão para realizar essa ação.");
            }

            UserModel user = getTargetUser(phoneNumber);
            return Result.success(UserDto.userToUserDto(user), "Dados carregados com sucesso");

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    private AccessLevel getAuthenticatedUserRole() {
         return AccessLevel.valueOf(SecurityUtil.getRole());
    }

    private boolean hasPermission(boolean isUpdatingOwnInfo, AccessLevel targetRole) {

        AccessLevel authenticatedRole = AccessLevel.valueOf(SecurityUtil.getRole());

        if(!authenticatedRole.equals(AccessLevel.ROLE_SUPER_ADMIN) && targetRole.equals(AccessLevel.ROLE_SUPER_ADMIN)) return false;

        if (authenticatedRole == AccessLevel.ROLE_CLIENT && !isUpdatingOwnInfo) return false;

        if(authenticatedRole == AccessLevel.ROLE_ADMIN && !isUpdatingOwnInfo && targetRole != AccessLevel.ROLE_CLIENT) return false;

        return true;
    }

    private UserModel getTargetUser(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NoSuchElementException("Usuário autenticado não encontrado"));
    }
}
