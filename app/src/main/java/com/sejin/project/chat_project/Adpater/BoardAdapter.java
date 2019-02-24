package com.sejin.project.chat_project.Adpater;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.VO.BoardVO;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class bHolder extends RecyclerView.ViewHolder {

        View view;
        LinearLayout layout;
        TextView bno,regdate,title,writer,count;

        bHolder(View view){
            super(view);
            this.view = view;
            layout = view.findViewById(R.id.layout_boardlist_item);
            bno = view.findViewById(R.id.board_bno);
            regdate = view.findViewById(R.id.board_regdate);
            title = view.findViewById(R.id.board_title);
            writer = view.findViewById(R.id.board_writer);
            count = view.findViewById(R.id.board_count);
        }
    }

    // 아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick{
        void onClick(View view,int position);
    }

    // 아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    private ArrayList<BoardVO> bList;

    public BoardAdapter(ArrayList<BoardVO> bList){
        this.bList = bList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_boardlist_item,parent,false);
        return new bHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        bHolder b = (bHolder) holder;
        b.bno.setText(String.valueOf(bList.get(position).getBno()));
        b.regdate.setText(bList.get(position).getRegdate());
        b.title.setText(bList.get(position).getTitle());
        b.writer.setText(bList.get(position).getWriter());
        b.count.setText(String.valueOf(bList.get(position).getViewcnt()));

        ((bHolder) holder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null){
                    itemClick.onClick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bList.size();
    }
}
