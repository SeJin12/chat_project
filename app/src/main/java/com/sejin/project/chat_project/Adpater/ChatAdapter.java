package com.sejin.project.chat_project.Adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.VO.ChatVO;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,content,date;
        LinearLayout chat_item_box;

        MyViewHolder(View view){
            super(view);
            chat_item_box = view.findViewById(R.id.layout_chat_item);
            name = view.findViewById(R.id.chat_name);
            content = view.findViewById(R.id.chat_content);
            date = view.findViewById(R.id.chat_date);
        }
    }

    private ArrayList<ChatVO> chatlist;

    public ChatAdapter(ArrayList<ChatVO> chatlist){
        this.chatlist = chatlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_chatroom_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.name.setText(chatlist.get(position).getUserName());
        myViewHolder.content.setText(chatlist.get(position).getMessage());
        myViewHolder.date.setText(chatlist.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }
}
