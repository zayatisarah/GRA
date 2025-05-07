package com.example.client.services.imp;

import com.example.client.entites.Action;
import com.example.client.repositories.ActionRepository;
import com.example.client.services.ActionService;
import com.example.client.services.ActionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionRepository actionRepository;
    @Override
    public Action addAction(Action action) {
        return actionRepository.save(action);
    }

    @Override
    public Action deleteAction(long id) {
        Optional<Action> action = actionRepository.findById(id);
        if (action.isPresent()) {
            actionRepository.delete(action.get());
            return action.get();
        }
        return null;
    }

    @Override
    public List<Action> getAllActions() {
        return actionRepository.findAll();
    }

    @Override
    public Action updateAction(Action action) {
        if (actionRepository.existsById(action.getIdActions())) {
            return actionRepository.save(action);
        }
        return null;
    }
}
