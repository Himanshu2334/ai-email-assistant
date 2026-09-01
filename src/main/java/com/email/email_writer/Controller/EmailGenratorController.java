package com.email.email_writer.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.email_writer.DTO.EmailRequest;
import com.email.email_writer.Service.EmailgeneratorService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailGenratorController {


    private final EmailgeneratorService emailgeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest)
    {
       System.out.println("hello EmailgeneratorService");
       
       String response = emailgeneratorService.generateEmail(emailRequest);
       return ResponseEntity.ok(response);
    }

}
