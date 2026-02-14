package com.todo.todoapp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    Page<ToDo> findByCompleted(Boolean completed, Pageable pageable);
}
