package com.task.manager.taskservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.task.manager.taskservice.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.task.manager.taskservice.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.data.domain.Page;



@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> saveTask(@PathVariable long id,
        @Valid @RequestBody SmallTaskDTO task)
    {
        Task res=taskService.saveTask(task, id);
        if(res!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Verify the data");
        }
    }

    //admin
    @GetMapping("/tasklist")
    public List<Task> fetchTaskList()
    {
        return taskService.fetchTaskList();
    }

    @GetMapping("/tasklist/{id}")
    public List<Task> fetchUserTaskList(@PathVariable("id") long id)
    {
        return taskService.fetchTaskByUserId(id);
    }

    //number of page to be retrieved, size how many can be in one page
    @GetMapping("/tasklist/page")
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<Task> tasks = taskService.getTasks(page, size, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasklist/page/{id}")
    public ResponseEntity<Page<Task>> getUsersTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @PathVariable long id
    ) {
        Page<Task> tasks = taskService.getUserTasks(id, page, size, sortBy, sortDir);
        return ResponseEntity.ok(tasks);
    }

    //admin
    @GetMapping("/task/{id}")
    public ResponseEntity<?> fetchTask(@PathVariable("id") long id)
    {
        Task res=taskService.fetchTaskByID(id);
        if(res!=null){
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    @GetMapping("/task/{id}/user/{user_id}")
    public ResponseEntity<?> fetchUserTask(@PathVariable("id") long id,@PathVariable("user_id") long user_id)
    {
        Task res=taskService.fetchTaskByID(id, user_id);
        if(res!=null){
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    //Admin
    @GetMapping("/task-count")
    public List<TaskCountDTO> fetchTaskCount() {
        return taskService.fetchTaskCount();
    }

    @GetMapping("/task-count/user/{user_id}")
    public ResponseEntity<?> fetchTaskCount(@PathVariable long user_id) {
        int res=taskService.fetchTaskCount(user_id);
        return ResponseEntity.status(HttpStatus.OK).body("You have " + res + " tasks"); 
    }
    
    

    @GetMapping("/tasklist/{id}/priority/{priority}")
    public List<Task> fetchUserTaskListPriority(@PathVariable("id") long id, @PathVariable String priority)
    {
        return taskService.fetchTaskByPriority(priority,id);
    }

    @GetMapping("/tasklist/neardate/{id}")
    public List<Task> fetchUserTaskListNearDate(@PathVariable("id") long id)
    {
        return taskService.fetchTaskNearDate(id);
    }

    @GetMapping("/tasklist/{id}/status/{status}")
    public List<Task> fetchUserTaskListStatus(@PathVariable("id") long id, @PathVariable String status)
    {
        return taskService.fetchTaskByStatus(status,id);
    }

    @GetMapping("/tasklist/{id}/due-date")
    public List<Task> fetchUserTaskListDate(@PathVariable("id") long id, @RequestParam(value="dueDate") String duedate)
    {
         // Parse to LocalDate
        
        try {
            LocalDate date = LocalDate.parse(duedate);
            LocalDateTime dateTime = date.atStartOfDay(); 
            return taskService.fetchTaskByDueDate(dateTime,id);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date/time string: " + e.getMessage());
        }
        return new ArrayList<Task>();  
    }

    @GetMapping("/tasklist/{id}/title/{title}")
    public List<Task> fetchUserTaskListName(@PathVariable("id") long id, @PathVariable String title)
    {
        return taskService.fetchTaskByName(title,id);
    }

    @DeleteMapping("/delete/{task_id}/user/{user_id}")
    public ResponseEntity<?> deleteUserTask(@PathVariable long task_id, @PathVariable long user_id){
        if(taskService.deleteTask(task_id,user_id))
            return ResponseEntity.status(HttpStatus.OK).body("Deleted succesfully");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    //admin
    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable long task_id){
        if(taskService.deleteTask(task_id))
            return ResponseEntity.status(HttpStatus.OK).body("Deleted succesfully");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    @PutMapping("/mark-as-completed/{id}/user/{user_id}")
    public ResponseEntity<?> markAscomplete(@PathVariable long id, @PathVariable long user_id) {
        Task res=taskService.updateStatus(id, user_id, "Completed");
        if(res!=null){
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    @PutMapping("/mark-as-pending/{id}/user/{user_id}")
    public ResponseEntity<?> markAsIncomplete(@PathVariable long id, @PathVariable long user_id) {
        Task res=taskService.updateStatus(id, user_id, "Pending");
        if(res!=null){
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    @PutMapping("/mark-as-in-progress/{id}/user/{user_id}")
    public ResponseEntity<?> markAsInProgess(@PathVariable long id, @PathVariable long user_id) {
        Task res=taskService.updateStatus(id, user_id, "In progress");
        if(res!=null){
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }

    //Valid sirve?
    @PutMapping("/update/task/{id}/user/{user_id}")
    public ResponseEntity<?>
    updateProduct(@Valid @RequestBody SmallTaskDTO task,
                     @PathVariable("id") Long task_id,
                     @PathVariable("user_id") Long user_id)
    {
        Task res= taskService.updateTask(task, user_id, task_id);
        if(res!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The provided ID couldnt be found");
    }
}
