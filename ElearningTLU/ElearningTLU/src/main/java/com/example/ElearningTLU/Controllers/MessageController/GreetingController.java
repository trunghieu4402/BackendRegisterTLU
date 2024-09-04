package com.example.ElearningTLU.Controllers.MessageController;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    @MessageMapping("/send")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }
}
