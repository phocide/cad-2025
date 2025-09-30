package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
public class TaskController {
    private List<Task> taskList = new ArrayList<>();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskList);
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String title, @RequestParam String description) {
        Task task = new Task(title, description);
        taskList.add(task);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'MODERATOR')")
    @PostMapping("/addTaskWithDeadline")
    public String addTaskWithDeadline(@RequestParam String title, @RequestParam String description,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        Task task = new Task(title, description, deadline);
        taskList.add(task);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('EDITOR', 'MODERATOR')")
    @PostMapping("/updateDeadline/{id}")
    public String updateDeadline(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline) {
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                task.setDeadline(deadline);
                break;
            }
        }
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable Long id, @RequestParam boolean completed) {
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                task.setCompleted(completed);
                break;
            }
        }
        return "redirect:/";
    }
}