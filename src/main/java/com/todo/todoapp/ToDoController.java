package com.todo.todoapp;

import com.todo.todoapp.dto.CreateToDoRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "api/v1/tasks")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService){
        this.toDoService = toDoService;
    }

    @GetMapping
    public Page<ToDo> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @PageableDefault(size = 10,sort = "id") Pageable pageable){
        return toDoService.getTasks(completed, pageable);
    }

    @PostMapping
    public ToDo addTask(@Valid @RequestBody CreateToDoRequest request){
        return toDoService.addTask(request.getTitle());
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
