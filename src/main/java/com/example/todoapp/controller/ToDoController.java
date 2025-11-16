package com.example.todoapp.controller;

import com.example.todoapp.entity.ToDo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.ToDoRepository;
import com.example.todoapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ToDoController {

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;

    @GetMapping({"", "/", "/home"})
    public String showMainPage(Model model, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        model.addAttribute("todos", toDoRepository.findByUser(user));
        return "index";
    }
    @PostMapping("/add")
    public String addNewToDo(@RequestParam String title, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        ToDo toDo = ToDo.builder()
                .title(title)
                .completed(false)
                .user(user)
                .build();
        toDoRepository.save(toDo);
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String deleteToDo(@PathVariable Long id, Authentication authentication) {

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        ToDo deletedToDo = toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"+id));

        //Sadece to-do nun sahibi silebilir
        if(!deletedToDo.getUser().equals(user)){
            throw new RuntimeException("This todo does not belong to you");
        }

        toDoRepository.delete(deletedToDo);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateToDo(@PathVariable Long id, Authentication authentication){

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        ToDo updatedToDo = toDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"+id));

        //Sadece to-do nun sahibi güncelleyebilir
        if(!updatedToDo.getUser().equals(user)){
            throw new RuntimeException("This todo does not belong to you");
        }

        updatedToDo.setCompleted(!updatedToDo.isCompleted());
        toDoRepository.save(updatedToDo);
        return "redirect:/";
    }
}
