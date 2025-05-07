package com.example.client.controllers;

import com.example.client.entites.Action;
import com.example.client.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actions")

public class ActionController {
    @Autowired
    private ActionService actionService;

    @PostMapping("/add")
    public Action addAction(@RequestBody Action action) {
        return actionService.addAction(action);
    }

    @DeleteMapping("/delete/{id}")
    public Action deleteAction(@PathVariable long id) {
        return actionService.deleteAction(id);
    }

    @GetMapping("/all")
    public List<Action> getAllActions() {
        return actionService.getAllActions(); // ✅ doit être une liste bien sérialisée
    }


    @PutMapping("/update")
    public Action updateAction(@RequestBody Action action) {
        return actionService.updateAction(action);
    }
    @GetMapping("/ping")
    public String testDatabaseConnection() {
        return "Connexion à la base de données réussie !";
    }

}
