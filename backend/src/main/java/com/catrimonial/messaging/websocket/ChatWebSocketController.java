package com.catrimonial.messaging.websocket;

import com.catrimonial.messaging.dto.MessageRequest;
import com.catrimonial.messaging.dto.MessageResponse;
import com.catrimonial.messaging.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessagingService messagingService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload MessageRequest request, Principal principal) {
        UUID senderId = UUID.fromString(principal.getName());
        MessageResponse response = messagingService.sendMessage(senderId, request);

        // Send to receiver's personal queue
        messagingTemplate.convertAndSendToUser(
                request.getReceiverId().toString(),
                "/queue/messages",
                response
        );

        // Send back to sender for confirmation
        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                response
        );

        log.debug("WebSocket message delivered from {} to {}", senderId, request.getReceiverId());
    }

    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload TypingNotification notification, Principal principal) {
        messagingTemplate.convertAndSendToUser(
                notification.getReceiverId().toString(),
                "/queue/typing",
                new TypingNotification(UUID.fromString(principal.getName()), notification.getReceiverId(), notification.isTyping())
        );
    }

    @MessageMapping("/chat.read")
    public void handleReadReceipt(@Payload ReadReceipt receipt, Principal principal) {
        UUID readerId = UUID.fromString(principal.getName());
        messagingService.markAsRead(readerId, receipt.getSenderId());

        messagingTemplate.convertAndSendToUser(
                receipt.getSenderId().toString(),
                "/queue/read-receipts",
                new ReadReceipt(readerId, receipt.getSenderId())
        );
    }

    public record TypingNotification(UUID senderId, UUID receiverId, boolean isTyping) {}
    public record ReadReceipt(UUID readerId, UUID senderId) {}
}
