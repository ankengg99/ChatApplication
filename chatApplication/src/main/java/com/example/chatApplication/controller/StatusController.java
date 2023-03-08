package com.example.chatApplication.controller;

import com.example.chatApplication.model.Status;
import com.example.chatApplication.service.StatusService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/status")
public class StatusController {

    @Autowired
    StatusService service;
//api/v1/status/create-status
    @PostMapping("/create-status")



    public ResponseEntity<String> createStatus(@RequestBody String statusData) {

        Status status = setStatus(statusData);
        service.saveStatus(status);
        return new ResponseEntity<>("status saved", HttpStatus.CREATED);

    }

    private  Status setStatus(String statusData) {
        //Create new status object
        Status status = new Status();

        //statusData has data type as String but format is JSON, so we create object of json
        JSONObject json = new JSONObject(statusData);
        String statusName = json.getString("statusName");
        String statusDescription = json.getString("statusDescription");

        status.setStatusName(statusName);
        status.setStatusDescription(statusDescription);

        return status;


    }

}