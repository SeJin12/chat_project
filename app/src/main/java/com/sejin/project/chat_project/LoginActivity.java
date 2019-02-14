package com.sejin.project.chat_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.Retrofit.UserInterface;
import com.sejin.project.chat_project.VO.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edit_email, edit_password;
    Button loginbtn, joinbtn;
    String uemail, upw;
    UserInterface userinterface;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = findViewById(R.id.layout_login);
        userinterface = Client.getClient().create(UserInterface.class);

        edit_email =  findViewById(R.id.login_email_edittext);
        edit_password = findViewById(R.id.login_password_edittext);
        loginbtn = findViewById(R.id.login_login_btn);
        joinbtn = findViewById(R.id.login_join_btn);

        layout.setOnClickListener(new View.OnClickListener() { // 배경화면 클릭 시, 소프트 키보드를 없애준다.
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_email.getWindowToken(),0);
                imm.hideSoftInputFromWindow(edit_password.getWindowToken(),0);
            }
        });

    }

    public void onClickLogin(View view) {
        uemail = edit_email.getText().toString();
        upw = edit_password.getText().toString();
        UserVO vo = new UserVO(uemail,upw);

        Call<UserVO> call = userinterface.checkLogin(vo);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickJoin(View view) {
        Intent intent = new Intent(LoginActivity.this,JoinActivity.class);
        startActivity(intent);
    }
}