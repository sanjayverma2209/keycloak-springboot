package com.springdemo.keycloak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthDemoController {

    @GetMapping("/v1")
    public ResponseEntity<String> api1(){
        return ResponseEntity.ok("For All Users");

    }

    @GetMapping("/v2")
    public ResponseEntity<String> api2(){
        return ResponseEntity.ok("For Admin");
    }

    @GetMapping("/v3")
    public ResponseEntity<String> api3(){
        return ResponseEntity.ok("For General User");
    }

    @GetMapping("/v4")
    public ResponseEntity<String> api4(){
        return ResponseEntity.ok("For Both Admin and General Users");
    }
}
