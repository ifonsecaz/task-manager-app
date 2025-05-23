package com.task.manager.gatewayservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.task.manager.gatewayservice.entity.EmailChangeRequestDTO;
import com.task.manager.gatewayservice.entity.PasswordResetRequestDTO;
import com.task.manager.gatewayservice.entity.SmallTaskDTO;
import com.task.manager.gatewayservice.entity.TaskDTO;
import com.task.manager.gatewayservice.entity.UserDTO;
import com.task.manager.gatewayservice.security.JwtUtil;
import com.task.manager.gatewayservice.security.RateLimiterService;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private RateLimiterService rateLimiterService;
    @Autowired
    private JwtUtil jwtUtils;

    private long extractUserIdFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token= bearerToken.substring(7); // Remove "Bearer " prefix
            return jwtUtils.getUserIdFromToken(token);
        }
        return 0;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        final String uri = "http://localhost:8081/user/info/" + username;
        RestTemplate restTemplate = new RestTemplate();
        
        try{
            return restTemplate.exchange(uri, HttpMethod.GET, null, UserDTO.class);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Handle error response as String
            String errorBody = ex.getResponseBodyAsString();
            HttpStatusCode statusCode = ex.getStatusCode();

            return ResponseEntity.status(statusCode).body(errorBody);
        } catch (Exception ex) {
            // Catch all other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
        }
    }

    @PostMapping("/resetpwd")
    public ResponseEntity<?> getUserInfo(@RequestBody PasswordResetRequestDTO req, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimiterService.resolveBucket(ip,"resetpwd");

        if (bucket.tryConsume(1)) {
            RestTemplate restTemplate = new RestTemplate();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String uri = "http://localhost:8081/user/resetpwd/"+username;
            
             
            try {
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, null, String.class);
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
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many attempts");
        }
    }
        
    @PostMapping("/changemail")
    public ResponseEntity<?> getUserInfo(@RequestBody EmailChangeRequestDTO req, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimiterService.resolveBucket(ip,"resetpwd");

        if (bucket.tryConsume(1)) {
            RestTemplate restTemplate = new RestTemplate();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String uri = "http://localhost:8081/user/changemail/"+username;
 
            try {
                ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, null, String.class);
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
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many attempts");
        }
    }


    @PostMapping("/newtask")
    public ResponseEntity<?> addTask(@RequestBody SmallTaskDTO req, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/add/"+user_id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SmallTaskDTO> entity = new HttpEntity<>(req,headers);

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.POST, entity, TaskDTO.class);
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

    @GetMapping("/tasklist")
    public ResponseEntity<?> userTaskList(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/tasklist/"+user_id;

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

    @GetMapping("/task/{id}")
    public ResponseEntity<?> userTask(@PathVariable long id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/task/"+id+"/user/"+user_id;

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

    @GetMapping("/task-count")
    public ResponseEntity<?> userTaskCount(HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/task-count/user/"+user_id;

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
    
    @GetMapping("/tasks/priority/{priority}")
    public ResponseEntity<?> userTaskListPriority(@PathVariable String priority, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/tasklist/"+user_id+"/priority/"+priority;

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

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<?> userTaskListStatus(@PathVariable String status, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/tasklist/"+user_id+"/status/"+status;

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

    @GetMapping("/tasks/title/{title}")
    public ResponseEntity<?> userTaskListTitle(@PathVariable String title, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/tasklist/"+user_id+"/title/"+title;

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
    public ResponseEntity<?> getUsersTasks(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        URI uri = UriComponentsBuilder
            .fromHttpUrl("http://localhost:8082/tasks/tasklist/page/{user_id}")
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("sortBy", sortBy)
            .queryParam("sortDir", sortDir)
            .buildAndExpand(user_id)
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

    @GetMapping("/tasks/due-date")
    public ResponseEntity<?> userTaskListDate(@RequestParam(value="dueDate") String value, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/tasklist/"+user_id+"/due-date?dueDate={value}";

        Map<String, String> params = new HashMap<>();
        params.put("value", value);

        try {
            ResponseEntity<List<TaskDTO>> response = restTemplate.exchange(uri, HttpMethod.GET, null,new ParameterizedTypeReference<List<TaskDTO>>() {},params);
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

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/delete/"+id+"/user/"+user_id;

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

    @PutMapping("/mark-as-complete/{id}")
    public ResponseEntity<?> markComplete(@PathVariable long id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/mark-as-completed/"+id+"/user/"+user_id;

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.PUT, null,TaskDTO.class);
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

    @PutMapping("/mark-as-pending/{id}")
    public ResponseEntity<?> markPending(@PathVariable long id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/mark-as-pending/"+id+"/user/"+user_id;

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.PUT, null,TaskDTO.class);
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

    @PutMapping("/mark-as-in-progress/{id}")
    public ResponseEntity<?> markProgress(@PathVariable long id, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/mark-as-in-progress/"+id+"/user/"+user_id;

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.PUT, null,TaskDTO.class);
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
    
    @PutMapping("/update-task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable long id, @RequestBody SmallTaskDTO task, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        
        long user_id = extractUserIdFromRequest(request);

        String uri = "http://localhost:8082/tasks/update/task/"+id+"/user/"+user_id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SmallTaskDTO> entity = new HttpEntity<>(task,headers);

        try {
            ResponseEntity<TaskDTO> response = restTemplate.exchange(uri, HttpMethod.PUT, entity,TaskDTO.class);
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

