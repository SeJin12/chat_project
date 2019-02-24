package com.sejin.project.chat_project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sejin.project.chat_project.Adpater.ReplyAdapter;
import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.Retrofit.ReplyInterface;
import com.sejin.project.chat_project.VO.ReplyVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;

    ReplyInterface replyInterface;
    int bno, viewcnt;
    String title, content, writer, regdate, address;
    TextView reply_bno,reply_title,reply_content,reply_writer,reply_viewcount,reply_regdate;
    double latitude, longitude;
    LatLng latLng;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    EditText reply_content_edit;
    Button reply_add_btn;
    List<ReplyVO> list;
    ArrayList<ReplyVO> items;
    ReplyAdapter replyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        replyInterface = Client.getClient().create(ReplyInterface.class);
        reply_bno = findViewById(R.id.reply_no);
        reply_title = findViewById(R.id.reply_title);
        reply_content = findViewById(R.id.reply_content);
        reply_writer = findViewById(R.id.reply_writer);
        reply_viewcount = findViewById(R.id.reply_viewcount);
        reply_regdate = findViewById(R.id.reply_regdate);
        reply_content_edit = findViewById(R.id.reply_content_edit);
        reply_add_btn = findViewById(R.id.reply_add_btn);

        recyclerView = findViewById(R.id.recycler_reply_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();
        replyAdapter = new ReplyAdapter(items);
        recyclerView.setAdapter(replyAdapter);

        // 뒤로가기 버튼
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent intent = getIntent(); // 데이터 수신
        bno = intent.getExtras().getInt("bno");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");
        writer = intent.getExtras().getString("writer");
        regdate = intent.getExtras().getString("regdate");
        address = intent.getExtras().getString("address");
        viewcnt = intent.getExtras().getInt("viewcnt");
        latitude = Double.parseDouble(intent.getExtras().getString("latitude"));
        longitude = Double.parseDouble(intent.getExtras().getString("longitude"));
        latLng = new LatLng(latitude,longitude);

        setTitle(title);

        reply_bno.setText(bno+"");
        reply_title.setText(title);
        reply_content.setText(content);
        reply_regdate.setText(regdate);
        reply_viewcount.setText(viewcnt+"");
        reply_writer.setText(writer);

        viewReply();

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.reply_map)).getMapAsync(this);

        reply_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME 댓글 추가 및 화면 갱신
                SharedPreferences sf = getSharedPreferences("userinfo",MODE_PRIVATE);
                final String name = sf.getString("name","");
                ReplyVO v1 = new ReplyVO(bno,reply_content_edit.getText().toString(),name);
                Call<Map<String,String>> call = replyInterface.insert(v1);
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Map<String,String> map = response.body();
                        if(map.get("code").equals("200")){
                            Intent intent = new Intent(getApplicationContext(),ReplyActivity.class);
                            intent.putExtra("bno",bno);
                            intent.putExtra("title",title);
                            intent.putExtra("content",content);
                            intent.putExtra("writer",name);
                            intent.putExtra("regdate",regdate);
                            intent.putExtra("viewcnt",viewcnt);
                            intent.putExtra("latitude",String.valueOf(latitude));
                            intent.putExtra("longitude",String.valueOf(longitude));
                            intent.putExtra("address",address);
                            startActivity(intent);
                            overridePendingTransition(0,0); // 인텐트 이동 애니메이션 제거
//                            Toast.makeText(getApplicationContext(),"댓글 등록.",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"댓글 등록 실패.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {

                    }
                });
            }
        });

    }

    void viewReply(){
        ReplyVO viewVo = new ReplyVO(bno);
        Call<List<ReplyVO>> call = replyInterface.readReply(viewVo);
        call.enqueue(new Callback<List<ReplyVO>>() {
            @Override
            public void onResponse(Call<List<ReplyVO>> call, Response<List<ReplyVO>> response) {
                list = response.body();
                items.clear();
                if(list.size()!=0){
                    for(ReplyVO v : list){
                        v.setRegdate(v.getRegdate().substring(0,v.getRegdate().length()-2));
                        items.add(v);
                    }
                    replyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<ReplyVO>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(map!=null){
            CameraPosition position = new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_marker));
            markerOptions.position(latLng);
            markerOptions.title(address);

            map.addMarker(markerOptions);

            UiSettings uiSettings = map.getUiSettings(); // 지도 확대/축소(+ / -)
            uiSettings.setZoomControlsEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 뒤로 가기
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
