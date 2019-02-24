package com.sejin.project.chat_project.Retrofit;

import com.sejin.project.chat_project.VO.BoardVO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BoardInterface {

    @GET("board/read.do")
    Call<List<BoardVO>> readAllBoard();

    @POST("board/insert.do")
    Call<Map<String,String>> insertBoard(@Body BoardVO vo);

    @POST("board/readBoard.do")
    Call<BoardVO> readBoard(@Body BoardVO vo);

    @POST("board/upViewCount.do")
    Call<Map<String,String>> upViewCount(@Body BoardVO vo);

}
