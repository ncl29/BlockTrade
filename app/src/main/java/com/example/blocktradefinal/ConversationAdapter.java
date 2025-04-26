package com.example.blocktradefinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<String> chatPartnerIds;
    private Context context;
    private OnConversationClickListener listener;

    public ConversationAdapter(List<String> chatPartnerIds, Context context) {
        this.chatPartnerIds = chatPartnerIds;
        this.context = context;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        String partnerId = chatPartnerIds.get(position);
        // Set up the UI with the partner's info (e.g., name, last message)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConversationClick(partnerId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatPartnerIds.size();
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        this.listener = listener;
    }

    public interface OnConversationClickListener {
        void onConversationClick(String partnerId);
    }

    // ViewHolder class to handle views in item_conversation.xml
    public static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserId); // Ensure ID matches with your XML
        }
    }
}
