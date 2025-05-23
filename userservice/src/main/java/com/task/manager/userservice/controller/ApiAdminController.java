package com.task.manager.userservice.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.task.manager.userservice.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.task.manager.userservice.entity.User;



@RestController
@RequestMapping("/admin")
public class ApiAdminController {
    private final UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder encoder;

    public ApiAdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        User aux=userRepository.findById(id).get();
        aux.removeRole();
        userRepository.delete(aux);
        return ResponseEntity.ok("User deleted");
    }

    @DeleteMapping("/delete/non-verified")
    public ResponseEntity<?> deleteNonVerified() {
        List<User> aux=userRepository.findByAcvalidatedFalse();
        for(User user : aux){
            if(user.getOldEmail()!=null){
                user.setEmail(user.getOldEmail());
                user.setAcvalidated(true);
                user.setLastPasswordReset();
                userRepository.save(user);
            }
            else{
                user.removeRole();
                userRepository.delete(user);
            }
        }
        return ResponseEntity.ok("Completed");
    }

    
}
    