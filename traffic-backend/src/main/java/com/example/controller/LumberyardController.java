package com.example.controller;

import com.example.model.RestBean;
import com.example.model.entity.Lumberyard;
import com.example.service.LumberyardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lumberyard")
@Tag(name = "伐木场", description = "伐木场相关操作")
public class LumberyardController {

}
