package com.catrimonial.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private UUID senderId;
    private String senderName;
    private String senderImage;
    private UUID receiverId;
    private String receiverName;
    private String content;
    private String messageType;
    private String imageUrl;
    private Boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
