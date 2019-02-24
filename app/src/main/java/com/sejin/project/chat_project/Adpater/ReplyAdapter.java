package com.sejin.project.chat_project.Adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.VO.ReplyVO;

import java.util.ArrayList;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,content,date;
        LinearLayout reply_item_box;

        MyViewHolder(View view){
            super(view);
            reply_item_box = view.findViewById(R.id.layout_replylist_item);
            name = view.findViewById(R.id.replylist_name);
            content = view.findViewById(R.id.replylist_content);
            date = view.findViewById(R.id.replylist_regdate);
        }
    }

    private ArrayList<ReplyVO> replylist;

    public ReplyAdapter(ArrayList<ReplyVO> replylist){
        this.replylist = replylist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_replylist_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.name.setText(replylist.get(position).getReplyer());
        myViewHolder.content.setText(replylist.get(position).getReplytext());
        myViewHolder.date.setText(replylist.get(position).getRegdate());

    }

    @Override
    public int getItemCount() {
        return replylist.size();
    }
}
