package com.example.controller;

import com.example.model.RestBean;
import com.example.model.dto.AccountLoginDTO;
import com.example.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "账号", description = "账号相关操作")
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public RestBean<Void> login(@RequestBody AccountLoginDTO accountLoginDTO) {
        String username = accountLoginDTO.getUsername();
        String password = accountLoginDTO.getPassword();

        if (accountService.authenticateUser(username, password)) {
            return RestBean.success();
        } else {
            return RestBean.failure(401, "用户名或密码错误");
        }
    }
}
