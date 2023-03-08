package com.example.chatApplication.service;

import com.example.chatApplication.dao.UserRepository;
import com.example.chatApplication.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public void saveUser(User user){
        userRepository.save(user);
    }
    public JSONArray getUsers(String userId) {
        JSONArray response = new JSONArray();
        if(null != userId) {
            List<User> usersList = userRepository.findUserByUserId(Integer.valueOf(userId));
            for (User user:usersList) {
                JSONObject userObj = createResponse(user);
                response.put(userObj);
            }
        } else {
            List<User> usersList = userRepository.findAll();
            for (User user:usersList) {
                JSONObject userObj = createResponse(user);
                response.put(userObj);
            }
        }
        return response;
    }

    public JSONObject login (String username, String password) {
        JSONObject response = new JSONObject();
        List<User> user = userRepository.findByUserName(username);
        if(user.isEmpty()) {
            response.put("errorMessage", "username doesn't exist");
        } else {
            User userObj = user.get(0);
            if(password.equals(userObj.getPassword())) {
                response = createResponse(userObj);
            }else {
                response.put("errorMessage" , "password is not valid");
            }
        }
        return response;
    }

    private JSONObject createResponse(User user) {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("userId", user.getUserId());
        jsonObj.put("username", user.getUserName());
        jsonObj.put("firstName", user.getFirstName());
        jsonObj.put("lastName", user.getLastName());
        jsonObj.put("age", user.getAge());
        jsonObj.put("email", user.getEmail());
        jsonObj.put("phoneNumber", user.getPhoneNumber());
        jsonObj.put("gender", user.getGender());
        jsonObj.put("createdDate", user.getCreatedDate());

        return jsonObj;
    }
    public JSONObject deleteUserByUserId(int userId) {
        List<User> user=userRepository.findUserByUserId(userId);
        JSONObject jsonObject=new JSONObject();
        if(user.isEmpty()){
           jsonObject.put("userId","user does not exist");
           return jsonObject;
        }
        userRepository.deleteUserByUserId(userId);
        return jsonObject;
    }
    public JSONObject updateUser(User newUser, int userId) {
        List<User> usersList = userRepository.findUserByUserId(userId);
        JSONObject obj = new JSONObject();
        if(!usersList.isEmpty()) {
            User oldUser = usersList.get(0);
            oldUser.setUserId(newUser.getUserId());

            oldUser.setPassword(newUser.getPassword());
            Timestamp updatedTime = new Timestamp(System.currentTimeMillis());
            oldUser.setUpdatedDate(updatedTime);
            userRepository.save(oldUser);
        } else {
            obj.put("errorMessage" , "User doesn't exist");
        }
        return obj;
    }
}
