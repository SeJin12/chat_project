package com.sejin.project.chat_project.Retrofit;

import com.sejin.project.chat_project.VO.UserVO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInterface {

    @POST("user/check.do")
    Call<UserVO> checkLogin(@Body UserVO vo);

    @POST("user/insert.do")
    Call<Map<String,String>> insert(@Body UserVO vo);

    @POST("user/checkEmail.do")
    Call<UserVO> checkEmail(@Body UserVO vo);

}
