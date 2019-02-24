package com.sejin.project.chat_project.Retrofit;

import com.sejin.project.chat_project.VO.ReplyVO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReplyInterface {

    @POST("reply/insert")
    Call<Map<String,String>> insert(@Body ReplyVO vo);

    @POST("reply/read")
    Call<List<ReplyVO>> readReply(@Body ReplyVO vo);

}
