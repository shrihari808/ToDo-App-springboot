package com.todo.todoapp;

import com.todo.todoapp.exception.ToDoNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepository repo;


    public ToDoService(ToDoRepository repo) {
        this.repo = repo;
    }

    public Page<ToDo> getAllTasks(Pageable pageable){
        return repo.findAll(pageable);
    }

    public ToDo addTask(String title){
        ToDo todo = new ToDo();
        todo.setCompleted(false);
        todo.setTitle(title);
        return repo.save(todo);
    }

    public void deleteTask(Integer id){
        if(!repo.existsById(id)){
            throw new ToDoNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public ToDo markCompleted(Integer id){
        ToDo todo = repo.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));
        todo.setCompleted(true);
        return repo.save(todo);
    }
}
