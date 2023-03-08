package com.example.chatApplication.controller;

import com.example.chatApplication.dao.StatusRepository;
import com.example.chatApplication.dao.UserRepository;
import com.example.chatApplication.model.ChatHistory;
import com.example.chatApplication.model.Status;
import com.example.chatApplication.model.User;
import com.example.chatApplication.service.ChatHistoryService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatHistoryController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    ChatHistoryService service;
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody String message){
        JSONObject jsonbject=validMessage(message);

    if(jsonbject.isEmpty()){
      JSONObject msg=new JSONObject(message);
      ChatHistory chat=setChatHistory(msg);
      service.saveChat(chat);
      return new ResponseEntity<>("message sent", HttpStatus.CREATED);
    }
    return new ResponseEntity<>(jsonbject.toString(),HttpStatus.BAD_REQUEST);
    }
    @GetMapping(value = "/get-chat")
    public ResponseEntity<String> getChatsByUserId(@RequestParam int senderId) {
        JSONObject response = service.getChatsByUserId(senderId);
        if(response.isEmpty()){
            return new ResponseEntity<>("chat not exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/get-conversation")
    public ResponseEntity<String> getConversationBetweenTwoUsers(@RequestParam int user1,@RequestParam int user2) {
        JSONObject response = service.getConversation(user1, user2);
        if(response.isEmpty()){
            return new ResponseEntity<>("chat not exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public String clearChat(@RequestParam int id){
        return service.clearAll( id);
    }
    @DeleteMapping("/delete/chat/id/{id}")
    public String deleteById(@PathVariable int id){
        return service.deleteById( id);
    }
    @DeleteMapping("/delete/conversation")
    public String clearChatBTTwo(@RequestParam int id1,@RequestParam int id2){
        return service.deleteConversation( id1,id2);
    }

    private JSONObject validMessage(String message) {
        JSONObject object=new JSONObject(message);
        JSONObject error=new JSONObject();
        if(!object.has("sender")){
            error.put("sender","missing sender");
        }
        if(!object.has("receiver")){
            error.put("receiver","missing receiver");
        }
        if(!object.has("message")){
            String msg=object.getString("message");
            if(msg.isEmpty() || msg.isBlank())
            error.put("message","message body empty");
            else{
                error.put("message","missing message");
            }
        }

        return error;
    }

    private ChatHistory setChatHistory(JSONObject requestObj) {
        ChatHistory chat = new ChatHistory();

        String message = requestObj.getString("message");
        int senderId =  requestObj.getInt("sender");
        int receiverId =  requestObj.getInt("receiver");
        User senderUserObj = userRepository.findById(senderId).get();
        User receiverUserObj = userRepository.findById(receiverId).get();

        chat.setReceiver(receiverUserObj);
        chat.setSender(senderUserObj);
        chat.setMessage(message);
        Status status = statusRepository.findById(1).get();
        chat.setStatusId(status);

        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        chat.setCreatedDate(createdTime);

        return chat;

    }


}
