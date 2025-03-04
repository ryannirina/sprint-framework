package com.sprintframework.test.controller;

import com.sprintframework.web.annotation.Controller;
import com.sprintframework.web.annotation.GetMapping;

@Controller
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Sprint Framework!";
    }
    
    @GetMapping("/greet")
    public String greet() {
        return "Welcome to Sprint Framework!";
    }
}
