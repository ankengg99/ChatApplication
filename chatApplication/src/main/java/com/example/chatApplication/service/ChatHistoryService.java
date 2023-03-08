package com.example.chatApplication.service;

import com.example.chatApplication.dao.ChatHistoryRepository;
import com.example.chatApplication.model.ChatHistory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {
    @Autowired
    ChatHistoryRepository repository;
    public void saveChat(ChatHistory chatHistory){
       repository.save(chatHistory);
    }

    public JSONObject getChatsByUserId(int senderId) {
        List<ChatHistory> chatList =repository.getChatsByUserId(senderId);
        JSONObject response = new JSONObject();

        if(!chatList.isEmpty()) {
            response.put("senderId" , chatList.get(0).getSender().getUserId());
            response.put("senderName" , chatList.get(0).getSender().getFirstName());
        }
        else{
            return response;
        }
        JSONArray receivers = new JSONArray();
        for (ChatHistory chats: chatList) {
            JSONObject receiverObj = new JSONObject();
            receiverObj.put("receiverId" , chats.getReceiver().getUserId());
            receiverObj.put("receiverName" , chats.getReceiver().getFirstName());
            receiverObj.put("message" , chats.getMessage());
            receivers.put(receiverObj);
        }
        response.put("receivers", receivers);

        return response;
    }

    public JSONObject getConversation(int user1, int user2) {
        JSONObject response = new JSONObject();
        JSONArray conversations = new JSONArray();
        List<ChatHistory> chatList = repository.getConversation(user1, user2);
         if(chatList.isEmpty()){
             return response;
         }
        for (ChatHistory chat : chatList) {
            JSONObject messageObj = new JSONObject();
            messageObj.put("chatId" , chat.getChatId());
            messageObj.put("timestamp" , chat.getCreatedDate());
            messageObj.put("senderName" , chat.getSender().getFirstName());
            messageObj.put("message", chat.getMessage());
            conversations.put(messageObj);
        }
        response.put("conversation", conversations);
        return response;
    }

    public String clearAll(int id) {
        if(repository.getChatsById(id).isEmpty()){
            return "chat not exist";
        }
        repository.clearAll(id);
        return "deleted All chat";
    }

    public String deleteConversation(int id1, int id2) {
        if(repository.getConversation(id1,id2).isEmpty()){
            return "chat not exist";
        }
        repository.deleteConversation(id1,id2);
        return "deleted";
    }

    public String deleteById(int id) {
        if(!repository.existsById(id)){
            return "chat not exist";
        }
        repository.deleteChatById(id);
        return "chat deleted";
    }
}
