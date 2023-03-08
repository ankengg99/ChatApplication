package com.example.chatApplication.service;

import com.example.chatApplication.dao.StatusRepository;
import com.example.chatApplication.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Autowired
    StatusRepository repository;


    public void saveStatus(Status status) {
        Status statusObj = repository.save(status);

    }
}
