package com.example.blocktradefinal;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private DatabaseReference messageRef;
    private String currentUserId;
    private String partnerId;

    private EditText messageInput;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get partner ID passed from MessageActivity
        partnerId = getIntent().getStringExtra("PARTNER_ID");
        currentUserId = FirebaseAuth.getInstance().getUid();

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        recyclerViewMessages = findViewById(R.id.recyclerViewChat);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        messageRef = FirebaseDatabase.getInstance().getReference("messages");

        // Load existing conversation
        loadConversation();

        // Send a new message
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                sendMessage(messageText, partnerId);
                messageInput.setText(""); // Clear input field
            }
        });
    }

    private void loadConversation() {
        // Fetch messages from Firebase for this pair of users
        DatabaseReference conversationRef = messageRef.child(currentUserId).child(partnerId);
        conversationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg != null) {
                    messageList.add(msg);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage(String messageText, String receiverId) {
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); // Get the sender's name
        long timestamp = System.currentTimeMillis();

        // Create the message object with sender's name
        Message message = new Message(senderId, receiverId, senderName, messageText, timestamp);
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("messages");

        // Generate a unique ID for the message
        String messageId = messageRef.push().getKey();

        if (messageId != null) {
            // Save message under both sender and receiver for symmetry
            messageRef.child(senderId).child(receiverId).child(messageId).setValue(message);
            messageRef.child(receiverId).child(senderId).child(messageId).setValue(message);
        }
    }
}
