package com.todo.todoapp.exception;

public class ToDoNotFoundException extends RuntimeException {
    public ToDoNotFoundException(Integer id){
        super("ToDo with id "+ id+" not found.");
    }
}
