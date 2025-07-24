package com.message.message.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class messageController {

    private final StreamBridge streamBridge;

    public messageController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @GetMapping("/message")
    public String getMethodName() {
        // sendNotification("Streaming with rabbitMQ message => Hello World!");
        return "Message sent successfully";
    }

    /*
     * public void sendNotification(String message) {
     * boolean sent = streamBridge.send("notification-out-0", message);
     * if (!sent) {
     * throw new RuntimeException("Failed to send message: " + message);
     * }
     * }
     */

}
