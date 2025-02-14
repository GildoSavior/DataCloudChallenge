package com.datacloudchallenge.AdminCliente.domain.services;

import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.Result;
import com.datacloudchallenge.AdminCliente.domain.usecase.UserUseCase;
import com.datacloudchallenge.AdminCliente.domain.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserUseCase {

    @Autowired
    private UserRepository repository;

    @Override
    public Result<List<UserModel>> findAll() {
        try {
            List<UserModel> allUsers = repository.findAll();
            return  Result.success(allUsers);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserModel> findUserById(Long id) {
        try {

            if(id.toString().isBlank())
                Result.failure("O id deve estar preenchido");

            UserModel user = repository.findById(id).orElseThrow(() -> new Exception("Não existe utilizador com esse id: ${id} !"));
            return  Result.success(user);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserModel> findUserByEmail(String email) {
        try {

            if(email.isBlank())
                Result.failure("O emal deve estar preenchido");

            UserModel user = repository.findByEmail(email);

            if(user == null ) {
                Result.failure("Não existe nenhum utilizador com este email: ${email}");
            }
            return  Result.success(user);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserModel> createUser(UserModel user) {
        try {
                String erros = Validator.validateUser(user);
                if (!erros.isEmpty()) return Result.failure(erros);

                UserModel userSaved = repository.save(user);

            return Result.success(user);

        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<UserModel> updateUser(UserModel user) {
        try {

            String errors = Validator.validateUser(user);

            if (!errors.isEmpty()) return Result.failure(errors);

            UserModel userToUpdate = repository.findByEmail(user.getEmail());

            if(userToUpdate == null) return Result.failure("Não existe utilizador com este email: ${user.getEmail()}");

            userToUpdate.setName(user.getName());
            user.setPhoneNumber(user.getPhoneNumber());
            userToUpdate.setData(userToUpdate.getData());
            userToUpdate.setImageUrl(user.getImageUrl());
            userToUpdate.setAccessLevel(user.getAccessLevel());
            repository.save(userToUpdate);

            return Result.success(userToUpdate);

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @Override
    public Result<String> deleteUser(String email) {
        try {

            UserModel user = repository.findByEmail(email);

            if(user == null) return Result.failure("Não existe utilizador com este email: ${user.getEmail()}");

            repository.deleteByEmail(email);

            return Result.success("Utilizador eliminado com sucesso");

        } catch(Exception e) {
            return Result.failure(e.getMessage());
        }
    }

}
