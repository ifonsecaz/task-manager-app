package com.task.manager.userservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.task.manager.userservice.entity.User;
import com.task.manager.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import com.task.manager.userservice.entity.TaskDTO;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Scheduled(fixedRate = 3600000) // Runs hourly
    @Transactional
    public void checkPendingEmailValidations() {
        // Find users with unverified emails
        List<User> users = userRepository.findByAcvalidatedFalse();
        
        for (User user : users) {
            if (user.getLastPasswordReset().plusMinutes(20).isBefore(LocalDateTime.now())) {
                // Revert to old email
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
        }
    }

    @Scheduled(cron = "0 0 8 * * ?") 
    @Transactional
    public void sendReminders() {
        List<User> users = userRepository.findAll();
        for(User user:users){
            RestTemplate restTemplate = new RestTemplate();
            
            long user_id = user.getUser_id();

            String uri = "http://localhost:8082/tasks/tasklist/neardate/"+user_id;

            try {
                ResponseEntity<List<TaskDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,new ParameterizedTypeReference<List<TaskDTO>>() {});
                if(response.getBody().size()>0)
                    emailService.sendReminder(user.getEmail(), response.getBody());
            } catch (Exception ex) {
                // Fallback for unexpected issues (e.g., timeout, no connection)
                System.err.println(ex);

            }                
        }
    
    }
}
