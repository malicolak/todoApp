package com.example.todoapp.controller;

import com.example.todoapp.entity.ToDo;
import com.example.todoapp.repository.ToDoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class ToDoController {

    private final ToDoRepository toDoRepository;

    @GetMapping({"", "/", "/home"})
    public String showMainPage(Model model) {
        model.addAttribute("todos", toDoRepository.findAll());
        return "index";
    }
    @PostMapping("/add")
    public String addNewToDo(@RequestParam String title) {
        ToDo toDo = ToDo.builder()
                .title(title)
                .completed(false)
                .build();
        toDoRepository.save(toDo);
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String deleteToDo(@PathVariable Long id) {
        ToDo deletedToDo = toDoRepository.findById(id).get();
        toDoRepository.delete(deletedToDo);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateToDo(@PathVariable Long id){
        ToDo updatedToDo = toDoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo not found"+id));
        updatedToDo.setCompleted(!updatedToDo.isCompleted());
        toDoRepository.save(updatedToDo);
        return "redirect:/";
    }
}
