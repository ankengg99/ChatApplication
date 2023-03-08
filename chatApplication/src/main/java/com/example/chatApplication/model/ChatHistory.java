package com.example.chatApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CollectionIdJdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_chat_history")
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;
    @JoinColumn
    @ManyToOne
    private User sender;
    @JoinColumn
    @ManyToOne
    private User receiver;
    private String message;


    private Timestamp createdDate;

    private Timestamp updatedDate;
    @JoinColumn(name="status_id")
    @ManyToOne
    private Status statusId;
}
