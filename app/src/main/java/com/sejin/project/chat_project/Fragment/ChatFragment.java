package com.sejin.project.chat_project.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sejin.project.chat_project.Adpater.ChatAdapter;
import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.VO.ChatVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChatFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    EditText chatcontent;
    Button chatbtn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Set<String> set;
    ArrayList<ChatVO> list;
    ChatAdapter chatadapter;
    ChildEventListener listener;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        mRecyclerView = view.findViewById(R.id.recycler_char_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        chatcontent = view.findViewById(R.id.chat_edittext);
        chatbtn = view.findViewById(R.id.chat_addbtn);
        set = new HashSet<>();



        list = new ArrayList<>();
        chatadapter = new ChatAdapter(list);
        mRecyclerView.setAdapter(chatadapter);

        // Add ChatVO int Firebase
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sf = view.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                String name = sf.getString("name","");
                String content = chatcontent.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String time = sdf.format(date);
                ChatVO chatData = new ChatVO(name,content,time);
                databaseReference.child("message").push().setValue(chatData);
                chatcontent.setText(null);
            }
        });

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.child("message").addChildEventListener(listener);
        databaseReference.removeEventListener(listener);

    }

    private void chatConversation(DataSnapshot dataSnapshot){

        if(set.contains(dataSnapshot.getValue().toString()))
            return;
        set.add(dataSnapshot.getValue().toString());
        ChatVO vo = dataSnapshot.getValue(ChatVO.class);
        String date = vo.getDate();
        String message = vo.getMessage();
        String name = vo.getUserName();
        list.add(new ChatVO(name,message,date));
        chatadapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(list.size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat,container,false);
    }

}
