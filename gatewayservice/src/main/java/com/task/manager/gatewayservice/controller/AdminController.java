package com.task.manager.gatewayservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.task.manager.gatewayservice.entity.TaskCountDTO;
import com.task.manager.gatewayservice.entity.TaskDTO;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        RestTemplate restTemplate = new RestTemplate();
        
        String uri = "http://localhost:8081/admin/delete/" + id;
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/non-verified")
    public ResponseEntity<?> deleteNonVerified() {
        RestTemplate restTemplate = new RestTemplate();
        
        String uri = "http://localhost:8081/admin/delete/non-verified";
        
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }


        //admin
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://localhost:8082/tasks/delete/"+id;

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, null,String.class);
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/task-count")
    public ResponseEntity<?> userTaskList(@PathVariable long id) {
        RestTemplate restTemplate = new RestTemplate();
        

        String uri = "http://localhost:8082/tasks/task-count";

        try {
            ResponseEntity<List<TaskCountDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,new ParameterizedTypeReference<List<TaskCountDTO>>() {});
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/task/{id}")
    public ResponseEntity<?> userTask(@PathVariable long id) {
        RestTemplate restTemplate = new RestTemplate();

        String uri = "http://localhost:8082/tasks/task/"+id;

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,TaskDTO.class);
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }
    

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@GetMapping("/tasklist")
    public ResponseEntity<?> userTaskList() {
        RestTemplate restTemplate = new RestTemplate();
        

        String uri = "http://localhost:8082/tasks/tasklist";

        try {
            ResponseEntity<List<TaskDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,new ParameterizedTypeReference<List<TaskDTO>>() {});
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/tasklist/page")
    public ResponseEntity<?> getTasksP(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        URI uri = UriComponentsBuilder
            .fromHttpUrl("http://localhost:8082/tasks/tasklist/page")
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("sortBy", sortBy)
            .queryParam("sortDir", sortDir)
            .buildAndExpand()
            .toUri();

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null,String.class);
            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // This captures 4xx and 5xx errors and returns them as-is
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (Exception ex) {
            // Fallback for unexpected issues (e.g., timeout, no connection)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

}
