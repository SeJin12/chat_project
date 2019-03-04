package com.sejin.project.chat_project.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImageInterface {

    @Multipart
    @POST("image/upload")
    Call<Integer> uploadImage(@Part MultipartBody.Part uploadFile, @Part("email") RequestBody email);

    @GET("image/display")
    Call<ResponseBody> displayImage(@Query("email") String email);

}
