package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.UserDTOReceive;
import com.jeido.vtuberrpgapi.dto.UserDTOSend;
import com.jeido.vtuberrpgapi.entites.User;
import com.jeido.vtuberrpgapi.repositories.UserRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements BaseService<UserDTOReceive, UserDTOSend> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTOSend toDTOSend(User user) {
        return UserDTOSend.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isAdmin(user.getIsAdmin() != null && user.getIsAdmin())
                .build();
    }

    public List<UserDTOSend> toDTOSend(List<User> users) {
        List<UserDTOSend> dtoSends = new ArrayList<>();
        for (User user : users) {
            dtoSends.add(toDTOSend(user));
        }

        return dtoSends;
    }

    @Override
    public UserDTOSend create(UserDTOReceive userDTOReceive) {
           if (existUsername(userDTOReceive.getUsername())) {
               throw new UsernameAlreadyInDBException(userDTOReceive.getUsername());
           }

           if (existEmail(userDTOReceive.getEmail())) {
               throw new EmailAlreadyInDBException(userDTOReceive.getEmail());
           }

           return toDTOSend(userRepository.save(User.builder()
                   .username(userDTOReceive.getUsername())
                   .email(userDTOReceive.getEmail())
                   .password(userDTOReceive.getPassword())
                   .build()
           ));
    }

    @Override
    public UserDTOSend findById(UUID id) {
        return toDTOSend(userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException(id)));
    }

    public UserDTOSend findByUsername(String username) {
        return toDTOSend(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    public UserDTOSend findByEmail(String email) {
        return toDTOSend(userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email)));
    }

    @Override
    public List<UserDTOSend> findAll() {
        return toDTOSend((List<User>) userRepository.findAll());
    }

    public List<UserDTOSend> findAdmins() {
        return toDTOSend(userRepository.findAllByIsAdmin(true));
    }

    @Override
    public boolean exist(UUID id) {
        return userRepository.existsById(id);
    }

    public boolean existUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTOSend update(UUID id, UserDTOReceive userDTOReceive) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserIdNotFoundException(id));

        if (userDTOReceive.getUsername() != null && !userDTOReceive.getUsername().equals(userToUpdate.getUsername())) {
            if (existUsername(userDTOReceive.getUsername())) {
                throw new UsernameAlreadyInDBException(userDTOReceive.getUsername());
            }

            userToUpdate.setUsername(userDTOReceive.getUsername());
        }

        if (userDTOReceive.getEmail() != null && !userDTOReceive.getEmail().equals(userToUpdate.getEmail())) {
            if (existEmail(userDTOReceive.getEmail())) {
                throw new EmailAlreadyInDBException(userDTOReceive.getEmail());
            }

            userToUpdate.setEmail(userDTOReceive.getEmail());
        }

        if (userDTOReceive.getPassword() != null && !userDTOReceive.getPassword().equals(userToUpdate.getPassword())) {
            userToUpdate.setPassword(userDTOReceive.getPassword());
        }

        return toDTOSend(userRepository.save(userToUpdate));
    }

    @Override
    public boolean delete(UUID id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }
}
