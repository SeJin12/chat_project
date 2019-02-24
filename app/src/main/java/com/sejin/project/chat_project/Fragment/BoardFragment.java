package com.sejin.project.chat_project.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.sejin.project.chat_project.Activity.CreateBoardActivity;
import com.sejin.project.chat_project.Activity.ReplyActivity;
import com.sejin.project.chat_project.Adpater.BoardAdapter;
import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.BoardInterface;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.VO.BoardVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardFragment extends Fragment {

    BoardInterface boardInterface;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    List<BoardVO> list;
    BoardAdapter boardAdapter;
    ArrayList<BoardVO> items;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,boardAdd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_open = AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);

        fab = view.findViewById(R.id.fab);
        boardAdd = view.findViewById(R.id.boardAdd);

        boardInterface = Client.getClient().create(BoardInterface.class);
        mRecyclerView = view.findViewById(R.id.recycler_board_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        items = new ArrayList<>();
        boardAdapter = new BoardAdapter(items);
        mRecyclerView.setAdapter(boardAdapter);



        boardAdapter.setItemClick(new BoardAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                BoardVO vo = items.get(position);
                Call<Map<String,String>> call = boardInterface.upViewCount(vo);
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Map<String,String> map = response.body();
                        if(map.get("code").equals("200")){
                            Log.i("BoardFragment","item click, board's viewcount plus 1.");
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                }); // 글 번호에 해당하는 글의 조회수 증가
                Call<BoardVO> callBoard = boardInterface.readBoard(vo);
                callBoard.enqueue(new Callback<BoardVO>() {
                    @Override
                    public void onResponse(Call<BoardVO> call, Response<BoardVO> response) {
                        Intent intent = new Intent(getContext(),ReplyActivity.class);
                        BoardVO vo1 = response.body();
                        intent.putExtra("bno",vo1.getBno());
                        intent.putExtra("title",vo1.getTitle());
                        intent.putExtra("content",vo1.getContent());
                        intent.putExtra("writer",vo1.getWriter());
                        intent.putExtra("regdate",vo1.getRegdate().substring(0,vo1.getRegdate().length()-2)); // 임시적으로 뒤에 .0을 제거
                        intent.putExtra("viewcnt",vo1.getViewcnt());
                        intent.putExtra("latitude",vo1.getLatitude());
                        intent.putExtra("longitude",vo1.getLongitude());
                        intent.putExtra("address",vo1.getAddress());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<BoardVO> call, Throwable t) {

                    }
                });
//                Intent intent = new Intent(getContext(),ReplyActivity.class);
//                intent.putExtra("bno",vo.getBno());
//                startActivity(intent);
                getActivity().overridePendingTransition(0,0); // 인텐트 이동 애니메이션 제거
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
//                Toast.makeText(getContext(), "fab Button", Toast.LENGTH_SHORT).show();
            }
        });

        boardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
//                Toast.makeText(getContext(), "boardAdd Button", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),CreateBoardActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0,0); // 인텐트 이동 애니메이션 제거
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board,container,false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Call<List<BoardVO>> call = boardInterface.readAllBoard();
        call.enqueue(new Callback<List<BoardVO>>() {
            @Override
            public void onResponse(Call<List<BoardVO>> call, Response<List<BoardVO>> response) {
                list = response.body();
                items.clear();
                for(BoardVO vo:list){
                    items.add(vo);
                }
                boardAdapter.notifyDataSetChanged(); // 갱신
            }

            @Override
            public void onFailure(Call<List<BoardVO>> call, Throwable t) {
                Toast.makeText(getContext(),"서버로부터 응답이 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void anim(){
        if(isFabOpen){
            boardAdd.startAnimation(fab_close);
            boardAdd.setClickable(false);
            isFabOpen = false;
        }else{
            boardAdd.startAnimation(fab_open);
            boardAdd.setClickable(true);
            isFabOpen = true;
        }
    }

}
