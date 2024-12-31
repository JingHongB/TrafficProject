package com.example.model.dto;

import lombok.Data;

/**
 * 接收前端登录请求的数据传输对象
 */
@Data
public class AccountLoginDTO {
    private String username;
    private String password;
}
