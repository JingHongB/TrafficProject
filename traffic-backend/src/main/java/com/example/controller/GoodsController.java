package com.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/poi")
@Tag(name = "货物", description = "货物相关操作")
//TODO 补充货物相关操作
public class GoodsController {
}
