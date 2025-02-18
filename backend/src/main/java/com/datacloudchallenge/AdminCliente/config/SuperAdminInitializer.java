package com.datacloudchallenge.AdminCliente.config;

import com.datacloudchallenge.AdminCliente.data.enums.AccessLevel;
import com.datacloudchallenge.AdminCliente.data.models.UserModel;
import com.datacloudchallenge.AdminCliente.data.repository.UserRepository;
import com.datacloudchallenge.AdminCliente.domain.dtos.user.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcUserDetailsManager userDetailsManager;

    public SuperAdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, JdbcUserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        UserModel user = new UserModel();
        user.setName("Super Admin");
        user.setPhoneNumber("000000000");
        user.setEmail("super@email");
        user.setPassword("00000");
        user.setAccessLevel(AccessLevel.ROLE_SUPER_ADMIN);

        if(userRepository.existsByPhoneNumber(user.getPhoneNumber())) return;

        UserDetails userDetails = User.withUsername(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(user.getAccessLevel().name())
                .build();

        userDetailsManager.createUser(userDetails);

        UserModel userToUpdate = userRepository.findByPhoneNumber(user.getPhoneNumber()).orElse(null);
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setAccessLevel(user.getAccessLevel());
        userRepository.save(userToUpdate);

    }


}
