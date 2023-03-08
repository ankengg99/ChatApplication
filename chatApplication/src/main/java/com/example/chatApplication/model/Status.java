package com.example.chatApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    private String statusName;
    private String statusDescription;
}
