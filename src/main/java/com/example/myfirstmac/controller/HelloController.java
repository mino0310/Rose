package com.example.myfirstmac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String test(@RequestParam String te) {
        System.out.println("for Test");
        System.out.println(te);
        System.out.println("테스트 종료요");
        return "hi";
    }
}
