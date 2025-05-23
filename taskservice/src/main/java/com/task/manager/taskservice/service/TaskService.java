package com.task.manager.taskservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.task.manager.taskservice.entity.SmallTaskDTO;
import com.task.manager.taskservice.entity.Task;
import com.task.manager.taskservice.entity.TaskCountDTO;
import com.task.manager.taskservice.repository.TaskRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Page<Task> getUserTasks(long userId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return taskRepository.findByUserId(userId, pageable);
    }

    public Page<Task> getTasks(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return taskRepository.findAll(pageable);
    }

    //Save
    public Task saveTask(SmallTaskDTO task,long user_id){
        Task newTask=new Task(user_id, task.getTitle(), task.getDescription(), "Pending", task.getPriority(), LocalDateTime.now(), task.getDueDate());
        return taskRepository.save(newTask);
    }

        //Read  
    public List<Task> fetchTaskList(){
        return taskRepository.findAll();
    }

        //Read 1
    public Task fetchTaskByID(Long taskID){
        Optional<Task> res= taskRepository.findById(taskID);
        if(res.isPresent()){
            return res.get();
        }
        return null;
    }

    public Task fetchTaskByID(Long taskID, long user_id){
        Optional<Task> res= taskRepository.findById(taskID);
        if(res.isPresent()&&res.get().getuserId()==user_id){
            return res.get();
        }
        return null;
    }

    public List<TaskCountDTO> fetchTaskCount(){
        return taskRepository.findUsersTaskCount();
    }

    public int fetchTaskCount(long user_id){
        return taskRepository.findByUserId(user_id).size();
    }

    public List<Task> fetchTaskByName(String name, long userId){
        return taskRepository.findByTitleContaining(name, userId);
    }

    public List<Task> fetchTaskByStatus(String status, long userId){
        return taskRepository.findByStatusAndUserId(status,userId);
    }

    public List<Task> fetchTaskByPriority(String priority, long userId){
        return taskRepository.findByPriorityAndUserId(priority, userId);
    }

    public List<Task> fetchTaskNearDate(long userId){
        LocalDateTime limit=LocalDateTime.now().plusDays(1);
        return taskRepository.findByNearDate(LocalDateTime.now(),limit,userId);
    }

    public List<Task> fetchTaskByDueDate(LocalDateTime dueDate, long userId){
        return taskRepository.findByDueDateAndUserId(dueDate, userId);
    }

    public List<Task> fetchTaskByUserId(long userId){
        return taskRepository.findByUserId(userId);
    }

    //Delete
    public boolean deleteTask(Long taskID){
        boolean res=false;
        if(taskRepository.findById(taskID).isPresent()){
            res=true;
            taskRepository.deleteById(taskID);
        }
        return res;
    }

    public boolean deleteTask(Long taskID, long user_id){
        boolean res=false;
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            res=true;
            taskRepository.deleteById(taskID);
        }
        return res;
    }

    //Update
    public Task updateTask(SmallTaskDTO task, long user_id, Long taskID){
        Optional<Task> aux2=taskRepository.findById(taskID);
        if(aux2.isPresent()&&aux2.get().getuserId()==user_id){
            Task aux=aux2.get();

            if(!aux.getTitle().equals(task.getTitle()))
                aux.setTitle(task.getTitle());
            if(!aux.getDescription().equals(task.getDescription()))
                aux.setDescription(task.getDescription());
            if(aux.getPriority().equals(task.getPriority()))
                aux.setPriority(task.getPriority());
            if(aux.getDueDate().compareTo(task.getDueDate())!=0)
                aux.setDueDate(task.getDueDate());
            return taskRepository.save(aux);

        }
        return null;
    }

    public Task updateTitle(Long taskID, long user_id,String title){
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            Task updatedTask=aux.get();
            
            updatedTask.setTitle(title);
            return taskRepository.save(updatedTask);
        }
        return null;
    }

    public Task updateDescription(Long taskID, long user_id, String description){
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            Task updatedTask=aux.get();
            
            updatedTask.setDescription(description);
            return taskRepository.save(updatedTask);
        }
        return null;
    }

    public Task updateStatus(Long taskID,long user_id, String status){
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            Task updatedTask=aux.get();
            
            updatedTask.setStatus(status);
            return taskRepository.save(updatedTask);
        }
        return null;
    }

    public Task updatePriority(Long taskID,long user_id, String priority){
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            Task updatedTask=aux.get();
            
            updatedTask.setPriority(priority);
            return taskRepository.save(updatedTask);
        }
        return null;
    }
    
    public Task updateDueTime(Long taskID,long user_id, LocalDateTime dueDate){
        Optional<Task> aux=taskRepository.findById(taskID);
        if(aux.isPresent()&&aux.get().getuserId()==user_id){
            Task updatedTask=aux.get();
            
            updatedTask.setDueDate(dueDate);
            return taskRepository.save(updatedTask);
        }
        return null;
    }
}
