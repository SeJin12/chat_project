package com.sejin.project.chat_project.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.Retrofit.ImageInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private Uri imageURI, photoURI, albumURI;
    private static final int PICK_FROM_ALBUM = 0;
    private final int GALLERY_CODE = 11111; //requestCode 선택한 사진에 대한 요청값을 구분하는 용도
    private final int CAMERA_CODE = 22222; // requestCode 선택한 사진에 대한 요청값을 구분하는 용도
    private static final int MY_PERMISSION_CAMERA = 3333;
    private final int REQUEST_CAMERA_IMAGE_CROP = 4444;
    private final int REQUEST_ALBUM_IMAGE_CROP = 5555;

    String currentPhotopath; //실제 사진 파일 경로
    String imagePath;
    ImageInterface imageInterface;
    ImageView profile_image;
    TextView profile_email, profile_name , profile_phone,profile_region;
    Button profile_btn;

    String email,name,phone,region;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageInterface = Client.getClient().create(ImageInterface.class);

        profile_image = view.findViewById(R.id.profile_image);
        profile_btn = view.findViewById(R.id.profile_btn);
        profile_email = view.findViewById(R.id.profile_email);
        profile_name = view.findViewById(R.id.profile_name);
        profile_phone = view.findViewById(R.id.profile_phone);
        profile_region = view.findViewById(R.id.profile_region);

        SharedPreferences sf = this.getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        email = sf.getString("email", "");
        name = sf.getString("name", "");
        phone = sf.getString("phone", "");
        region = sf.getString("region", "");

        profile_email.setText(email);
        profile_name.setText(name);
        profile_phone.setText(phone);
        profile_region.setText(region);

        Call<ResponseBody> displayCall = imageInterface.displayImage(email);
        displayCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        profile_image.setImageBitmap(bmp);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"이미지 가져오기 실패",Toast.LENGTH_SHORT).show();
            }
        });


        final CharSequence[] Camera = { "앨범에서 사진 선택"};
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //퍼미션 체크 및 요청
                AlertDialog.Builder alterBuilder = new AlertDialog.Builder(getContext());
                // 전체 실행할 코드
                alterBuilder.setTitle("업로드할 이미지 선택");

                alterBuilder.setItems(Camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case PICK_FROM_ALBUM :
                                SelectGallery();
                                break;
                        }
                    }
                });
                AlertDialog dialog = alterBuilder.create();
                dialog.show();
            }
        });

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePath != null){
                    uploadImage(imagePath);
                }else{
                    Toast.makeText(getContext(),"변경할 이미지를 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    // 이미지형식 파일로 생성하는 코드
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());//이미지파일 이름형식 지정
        String mImageCaptureName = "JPEG_" + timeStamp + ".jpg"; //이미지파일 확장자명 지정
        File imageFile = null;
        File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "gyeom");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        imageFile = new File(dir, mImageCaptureName);
        currentPhotopath = imageFile.getAbsolutePath();
        return imageFile;
    }

    //갤러리에서 사진을 가져오는 경우
    private void SelectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, GALLERY_CODE); //오버라이드 onActivityResult()의 case 문 GALLERY_CODE 실행
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // 카메라로 사진을 찍거나 갤러리에서 사진 선택에 대한 사용자가 응답을 할경우  실행
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) { //사진을 선택했을경우 취소했을 경우는 RESULT_CANCLE
            switch (requestCode) {
                case GALLERY_CODE:
                    if (data.getData() != null) {
                        try {
                            photoURI = data.getData();
//                            cropImage_album();
                            SendPicture(photoURI);
                        } catch (Exception e) {
                            Log.e("TAKE_ALBKLSE", "sds");
                        }
                    }
                    break;
            }
        }
    }

    //사진을 이미지뷰로 전송
    private void SendPicture(Uri imgURI) {
        imagePath = getRealPathFromURI(imgURI);  //URI경로를 해당 파일의 실제 경로를 가져온다
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imagePath); // 선택된 이미지 파일의 Exif테그(어떠한 파일인지)를 읽는다
        } catch (Exception e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); //선택된 파일의 특정 Exif테그 값을 리턴한다
//        int exifDegree = exifOrientationToDegrees(exifOrientation); // 사진의 회전값을 받아오는 함수 실행

        BitmapFactory.Options options;

        try {

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
            profile_image.setImageBitmap(bitmap);//이미지 뷰에 비트맵 넣기
        } catch (Exception e) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                profile_image.setImageBitmap(bitmap);//이미지 뷰에 비트맵 넣기
            } catch (Exception a) {
                a.getStackTrace();
            }
        }
    }

    //사진의 절대 경로 구하기 , 이미지 서버로 전송할떄도 쓰임
    private String getRealPathFromURI(Uri contentURI) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContext().getContentResolver().query(contentURI, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    // 서버로 파일 업로드
    private void uploadImage(String absolutePath) {
        File file = new File(absolutePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("uploadFile", file.getName(), requestFile);
        RequestBody uemail = RequestBody.create(MediaType.parse("text/plain"),email);


        Call<Integer> call = imageInterface.uploadImage(uploadFile,uemail);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int responseCode = response.body();
                switch (responseCode){
                    case 200:
                        Toast.makeText(getContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                        break;
                    case 400:
                        Toast.makeText(getContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "서버와 연결 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
