package org.example.server.Controller;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;

@Controller
public class LogController {

    @MessageMapping("/logs")
    @SendTo("/topic/logs")
    public String sendLog(String log) {
        return log;
    }
}
