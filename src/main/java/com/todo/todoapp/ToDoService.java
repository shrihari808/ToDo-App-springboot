package com.todo.todoapp;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepository repo;


    public ToDoService(ToDoRepository repo) {
        this.repo = repo;
    }

    public List<ToDo> getAllTasks(){
        return repo.findAll();
    }

    public ToDo addTask(String title){
        ToDo todo = new ToDo();
        todo.setCompleted(false);
        todo.setTitle(title);
        return repo.save(todo);
    }

    public void deleteTask(Integer id){
        repo.deleteById(id);
    }

    public ToDo markCompleted(Integer id){
        ToDo todo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        todo.setCompleted(true);
        return repo.save(todo);
    }
}
