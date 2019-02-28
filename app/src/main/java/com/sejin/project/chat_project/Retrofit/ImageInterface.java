package com.sejin.project.chat_project.Retrofit;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageInterface {

    @Multipart
    @POST("/image/upload")
    Call<String> uploadImage(@Part MultipartBody.Part file);

}
