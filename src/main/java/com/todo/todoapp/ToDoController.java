package com.todo.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/tasks")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService){
        this.toDoService = toDoService;
    }

    @GetMapping
    public List<ToDo> getAllTasks(){
        return toDoService.getAllTasks();
    }

    @PostMapping
    public ToDo addTask(@RequestBody ToDo toDo){
        return toDoService.addTask(toDo.getTitle());
    }

    @PutMapping("/{id}")
    public ToDo updateTask(@PathVariable Integer id){
        return toDoService.markCompleted(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id){
        toDoService.deleteTask(id);
        return new ResponseEntity<>("Task Deleted", HttpStatus.OK);
    }
}
