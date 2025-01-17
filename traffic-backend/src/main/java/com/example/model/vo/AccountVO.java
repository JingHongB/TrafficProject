package com.example.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AccountVO {
    String username;
    String email;
    String role;
    String avatar;
    Date registerTime;
}
