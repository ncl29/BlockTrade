package com.example.blocktradefinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.*;

public class MessageActivity extends BaseActivity {

    private RecyclerView recyclerViewConversations;
    private List<String> chatPartnerIds;
    private ConversationAdapter adapter;
    private DatabaseReference messageRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Initialize bottom navigation
        setupBottomNavigation(R.id.nav_message);

        // Initialize RecyclerView and other components
        recyclerViewConversations = findViewById(R.id.recyclerViewConversations);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(this));
        chatPartnerIds = new ArrayList<>();
        adapter = new ConversationAdapter(chatPartnerIds, this);
        recyclerViewConversations.setAdapter(adapter);

        currentUserId = FirebaseAuth.getInstance().getUid();
        messageRef = FirebaseDatabase.getInstance().getReference("messages");

        // Load conversations
        loadConversations();

        // Handle click on a conversation partner
        adapter.setOnConversationClickListener(new ConversationAdapter.OnConversationClickListener() {
            @Override
            public void onConversationClick(String partnerId) {
                // Open the conversation with the clicked partner
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                intent.putExtra("PARTNER_ID", partnerId); // Pass the partner's ID to the chat activity
                startActivity(intent);
            }
        });
    }

    // Method to load conversations from Firebase
    private void loadConversations() {
        DatabaseReference userMessagesRef = messageRef.child(currentUserId); // Load only messages for current user

        userMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> uniqueChatPartners = new HashSet<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Go through each conversation for this user
                    for (DataSnapshot messageSnapshot : userSnapshot.getChildren()) {
                        Message msg = messageSnapshot.getValue(Message.class);
                        if (msg != null) {
                            // Null check for senderId and receiverId before using equals
                            if (msg.senderId != null && msg.receiverId != null) {
                                if (msg.senderId.equals(currentUserId)) {
                                    uniqueChatPartners.add(msg.receiverId);
                                } else if (msg.receiverId.equals(currentUserId)) {
                                    uniqueChatPartners.add(msg.senderId);
                                }
                            } else {
                                Log.e("MessageActivity", "senderId or receiverId is null in message: " + msg);
                            }
                        }
                    }
                }
                chatPartnerIds.clear();
                chatPartnerIds.addAll(uniqueChatPartners);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MessageActivity", "Failed to load conversations", error.toException());
            }
        });
    }
}
