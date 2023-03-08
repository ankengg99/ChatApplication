package com.example.chatApplication.dao;

import com.example.chatApplication.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory,Integer> {
    @Query(value = "select * from tbl_chat_history where sender_user_id=:senderId and status_id=1",nativeQuery = true)
    List<ChatHistory> getChatsByUserId(int senderId);
    @Query(value = "select * from tbl_chat_history where (sender_user_id=:senderId or receiver_user_id=:senderId) and status_id=1",nativeQuery = true)
    List<ChatHistory> getChatsById(int senderId);
    @Query(value = "select * from tbl_chat_history where ((sender_user_id=:user1 and receiver_user_id=:user2) " +
            "or (sender_user_id=:user2 and receiver_user_id=:user1)) and status_id=1 order by created_date",nativeQuery = true)
    List<ChatHistory> getConversation(int user1, int user2);
    @Modifying
    @Transactional
    @Query(value = "update tbl_chat_history set status_id=2 where (sender_user_id=:id or receiver_user_id=:id)"+
        "and status_id=1",countQuery = "select count(*) from tbl_user",nativeQuery = true)
    public void clearAll(int id);
    @Modifying
    @Transactional
    @Query(value = "update tbl_chat_history set status_id=2 where  ((sender_user_id=:user1 and receiver_user_id=:user2) " +
            "or (sender_user_id=:user2 and receiver_user_id=:user1)) and status_id=1 ",
            countQuery = "select count(*) from tbl_user",nativeQuery = true)
   public void deleteConversation(int user1, int user2);
    @Modifying
    @Transactional
    @Query(value = "update tbl_chat_history set status_id=2 where chat_id=:id and status_id=1",
            countQuery = "select count(*) from tbl_user",nativeQuery = true)
   public void deleteChatById(int id);
}
