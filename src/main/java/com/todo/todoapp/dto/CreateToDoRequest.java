package com.todo.todoapp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateToDoRequest {
    @NotBlank(message = "Title field cannot be empty.")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
    private String title;

    public CreateToDoRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
