package com.sejin.project.chat_project.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.Retrofit.UserInterface;
import com.sejin.project.chat_project.VO.UserVO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    private RelativeLayout layout;
    UserInterface userinterface;
    EditText join_email_edit,join_pw_edit,join_name_edit,join_phone_edit,join_region_edit;
    Button join_join_btn,join_cancel_btn,join_checkemail_btn;
    String uemail,upw,uname,uphone,uregion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        layout = findViewById(R.id.layout_join);
        userinterface = Client.getClient().create(UserInterface.class);

        join_email_edit = findViewById(R.id.join_email_edit);
        join_pw_edit = findViewById(R.id.join_pw_edit);
        join_name_edit = findViewById(R.id.join_name_edit);
        join_phone_edit = findViewById(R.id.join_phone_edit);
        join_region_edit = findViewById(R.id.join_region_edit);
        join_join_btn = findViewById(R.id.join_join_btn);
        join_cancel_btn = findViewById(R.id.join_cancel_btn);
        join_checkemail_btn = findViewById(R.id.join_checkemail_btn);

        layout.setOnClickListener(new View.OnClickListener() { // 배경화면 클릭 시, 소프트 키보드를 없애준다.
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(join_email_edit.getWindowToken(),0);
                imm.hideSoftInputFromWindow(join_pw_edit.getWindowToken(),0);
                imm.hideSoftInputFromWindow(join_name_edit.getWindowToken(),0);
                imm.hideSoftInputFromWindow(join_phone_edit.getWindowToken(),0);
                imm.hideSoftInputFromWindow(join_region_edit.getWindowToken(),0);
            }
        });


    }

    public void onClickJoinComplete(View view) {
        uemail = join_email_edit.getText().toString();
        upw = join_pw_edit.getText().toString();
        uname = join_name_edit.getText().toString();
        uphone = join_phone_edit.getText().toString();
        uregion = join_region_edit.getText().toString();

        UserVO vo = new UserVO(uemail,upw,uname,uphone,uregion);

        Call<Map<String,String>> call = userinterface.insert(vo);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String,String> map = response.body();
                if(map.get("code").equals("200")){
                    Toast.makeText(getApplicationContext(),"회원가입에 성공하셨습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"회원가입 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"항목 모두를 입력해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickJoinCancel(View view) {
        finish();
    }

    public void onClickJoinEmailCheck(View view) {
        uemail = join_email_edit.getText().toString();
        UserVO vo = new UserVO();
        vo.setUemail(uemail);

        Call<UserVO> call = userinterface.checkEmail(vo);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                Toast.makeText(getApplicationContext(),"사용할 수 없습니다.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"사용할 수 있는 이메일입니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
