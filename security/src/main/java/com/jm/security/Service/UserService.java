package com.jm.security.Service;

import com.jm.security.Entity.UserSec;
import com.jm.security.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public List<UserSec> findAll(){
        return userRepository.findAll();
    }

    public Optional<UserSec> findById(Long id){
        return userRepository.findById(id);
    }

    public UserSec save(UserSec userSec){
        return userRepository.save(userSec);
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public UserSec update(UserSec userSec){
        return userRepository.save(userSec);
    }

    public String encriptPassword(String password){
        return new BCryptPasswordEncoder().encode(password);
    }
}
