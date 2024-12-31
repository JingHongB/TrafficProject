package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Account;

public interface AccountService extends IService<Account> {
    boolean authenticateUser(String username, String password);
}
