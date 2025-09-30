package com.example.demo;

import java.time.LocalDate;

public class Task {
    private static Long nextId = 1L; // Статическая переменная для генерации уникальных идентификаторов задач
    private Long id; // Уникальный идентификатор задачи
    private String title; // Название задачи
    private String description; // Описание задачи
    private boolean completed; // Статус задачи (выполнена или нет)
    private LocalDate deadline; // Срок выполнения задачи
    // Конструкторы

    public Task() {
        this.id = generateId();
    }

    public Task(String title, String description) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        this.completed = false; // По умолчанию задача не выполнена
    }

    public Task(String title, String description, LocalDate deadline) {
        this.id = generateId();
        this.title = title;
        this.description = description;
        this.completed = false; // По умолчанию задача не выполнена
        this.deadline = deadline;
    }

    // Генерация уникального идентификатора задачи
    private synchronized Long generateId() {
        return nextId++;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    // Переопределение метода toString() для удобного вывода информации о задаче
    @Override
    public String toString() {
        return "Task{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", completed=" + completed +
        ", deadline=" + deadline +
        '}';
    }
}
