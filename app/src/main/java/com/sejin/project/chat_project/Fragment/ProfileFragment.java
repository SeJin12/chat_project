package com.sejin.project.chat_project.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.Retrofit.ImageInterface;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    ImageInterface imageInterface;
    ImageView profile_image;
    TextView test;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageInterface = Client.getClient().create(ImageInterface.class);
        test = view.findViewById(R.id.test);
        profile_image = view.findViewById(R.id.profile_image);

        SharedPreferences sf = this.getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        String email = sf.getString("email","");
        String name = sf.getString("name","");
        String phone = sf.getString("phone","");
        String region = sf.getString("region","");
        test.setText(email+" "+name+" "+phone+" "+region);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

    }

    //FIXME 승준 깃허브보고 한번
    private void uploadImage(String imagePath){
        File file = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",file.getName(),requestFile);

        Call<String> call = imageInterface.uploadImage(body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            try{
                InputStream in = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                profile_image.setImageBitmap(img);

                uploadImage(data.getDataString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }
}
