package com.junyounggoat.dreamstore.userservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Tag(name = "HomeController", description = "홈 컨트롤러")
public class HomeController {
    @GetMapping("")
    public String index() {
        return "Hello JunYoungGOAT";
    }
}
