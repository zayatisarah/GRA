package com.example.client.services;

import com.example.client.entites.Action;

import java.util.List;

public interface ActionService {
    Action addAction(Action action);
    Action deleteAction(long id);
    List<Action> getAllActions();
    Action updateAction(Action action);
}
