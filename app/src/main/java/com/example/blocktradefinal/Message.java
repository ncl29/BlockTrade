package com.example.blocktradefinal;

public class Message {
    public String senderId;
    public String receiverId;
    public String senderName;  // Sender's name field
    public String message;
    public long timestamp;

    // Default constructor required for Firebase
    public Message() {
        // Required for Firebase
    }

    // Updated constructor to include sender's name
    public Message(String senderId, String receiverId, String senderName, String message, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;  // Initialize sender's name
        this.message = message;
        this.timestamp = timestamp;
    }
}
