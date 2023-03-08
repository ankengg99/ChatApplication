package com.example.chatApplication.controller;


import com.example.chatApplication.dao.StatusRepository;
import com.example.chatApplication.dao.UserRepository;
import com.example.chatApplication.model.Status;
import com.example.chatApplication.model.User;

import com.example.chatApplication.service.UserService;
import com.example.chatApplication.utils.CommonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
        @Autowired
    UserRepository userRepository;

    @Autowired
    StatusRepository statusRepository;
@Autowired
    UserService service;
    @PostMapping(value = "/create-user")
    public ResponseEntity<String> createUser(@RequestBody String userData) {

        JSONObject isValid = validateUserRequest(userData);

        User user = null;

        if(isValid.isEmpty()) {
            user = setUser(userData);
            service.saveUser(user);

        } else {
            return new ResponseEntity<String>(isValid.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Saved", HttpStatus.CREATED);
    }
    @GetMapping(value = "/get-users")
    public ResponseEntity<String> getUsers(@Nullable @RequestParam String userId) {

        JSONArray userArr = service.getUsers((userId));
        return new ResponseEntity<>(userArr.toString(), HttpStatus.OK);

    }
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody String requestData) {
        JSONObject requestJson = new JSONObject(requestData);

        JSONObject isValidLogin = validateLogin(requestJson);

        if (isValidLogin.isEmpty()) {
            String username = requestJson.getString("username");
            String password = requestJson.getString("password");
            JSONObject responseObj = service.login(username, password);
            if(responseObj.has("errorMessage")) {
                return new ResponseEntity<String>(responseObj.toString(), HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<String>(responseObj.toString(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<String>(isValidLogin.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private JSONObject validateLogin(JSONObject requestJson) {

        JSONObject errorList = new JSONObject();

        if(!requestJson.has("username")) {
            errorList.put("username", "missing parameter");
        }
        if(!requestJson.has("password")) {
            errorList.put("password", "missing parameter");
        }
        return errorList;
    }

    @PutMapping(value = "/update-user/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody String requestData) {
        JSONObject isRequestValid = validateUserRequest(requestData);
        User user = null;

        if(isRequestValid.isEmpty()) {
            user = setUser(requestData);
            JSONObject responseObj = service.updateUser(user, userId);
            if(responseObj.has("errorMessage")) {
                return new ResponseEntity<String>(responseObj.toString(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<String>(isRequestValid.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("user updated", HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam int id){
           JSONObject jsonObject=
                   service.deleteUserByUserId(id);
      if(!jsonObject.isEmpty()) {
          return new ResponseEntity<>(jsonObject.toString(),HttpStatus.BAD_REQUEST);
      }

      return new ResponseEntity<String>("Deleted", HttpStatus.NO_CONTENT);

    }


    private JSONObject validateUserRequest(String userData) {
        JSONObject userJson = new JSONObject(userData);
        JSONObject errorList = new JSONObject();

        if(userJson.has("username")) {
            String username = userJson.getString("username");
            List<User> list=userRepository.findByUserName(username);
            if(!list.isEmpty()){
                errorList.put("username","already exist user");
                return errorList;
            }
        } else {
            errorList.put("username", "Missing parameter");
        }
        if(userJson.has("password")) {
            String password = userJson.getString("password");
            if(!CommonUtils.isValidPassword(password)) {
                errorList.put("password", "Please enter valid password eg: Akshay@12390");
            }
        } else {
            errorList.put("password", "Missing parameter");
        }

        if(userJson.has("firstName")) {
            String firstName = userJson.getString("firstName");
        } else {
            errorList.put("firstName", "Missing parameter");
        }

        if(userJson.has("email")) {
            String email = userJson.getString("email");
            if(!CommonUtils.isValidEmail(email)) {
                errorList.put("email", "Please enter a valid email");
            }
        } else {
            errorList.put("email", "Missing parameter");
        }

        if(userJson.has("phoneNumber")) {
            String phoneNumber = userJson.getString("phoneNumber");
            if(!CommonUtils.isValidPhoneNumber(phoneNumber)) {
                errorList.put("phoneNumber", "Please enter a valid phone number");
            }
        } else {
            errorList.put("phoneNumber", "Missing parameter");
        }

        return errorList;

    }

    private User setUser(String userData) {
        User user = new User();
        JSONObject json = new JSONObject(userData);

        user.setEmail(json.getString("email"));
        user.setPassword(json.getString("password"));
        user.setFirstName(json.getString("firstName"));
        user.setUserName(json.getString("username"));
        user.setPhoneNumber(json.getString("phoneNumber"));

        if(json.has("age")) {
            user.setAge(json.getInt("age"));
        }

        if(json.has("lastName")){
            user.setLastName(json.getString("lastName"));
        }
        if(json.has("gender")){
            user.setGender(json.getString("gender"));
        }
        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        user.setCreatedDate(createdTime);

        Status status = statusRepository.findById(1).get();
        user.setStatusId(status);

        return user;

    }
}