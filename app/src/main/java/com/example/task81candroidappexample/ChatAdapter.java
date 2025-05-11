package com.example.task81candroidappexample;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {
    List<ChatMessage> items;
    public ChatAdapter(List<ChatMessage> it) { items=it; }

    @Override public int getItemViewType(int pos) {
        return items.get(pos).isUser ? R.layout.item_user : R.layout.item_user;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext()).inflate(viewType, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        h.text.setText(items.get(pos).text);
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView text;
        VH(View v){
            super(v);
            text = v.findViewById(android.R.id.text1);
        }
    }
}