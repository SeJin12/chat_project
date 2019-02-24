package com.sejin.project.chat_project.Activity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.sejin.project.chat_project.R;
import com.sejin.project.chat_project.Retrofit.BoardInterface;
import com.sejin.project.chat_project.Retrofit.Client;
import com.sejin.project.chat_project.VO.BoardVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBoardActivity extends AppCompatActivity implements OnMapReadyCallback {

    BoardInterface boardInterface;
    GoogleMap map;
    EditText title,content,search;
    Button ok;
    String name;
    Geocoder geocoder;
    LatLng latLng;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        boardInterface = Client.getClient().create(BoardInterface.class);
        title = findViewById(R.id.cboard_title);
        content = findViewById(R.id.cboard_content);
        search = findViewById(R.id.cboard_search);
        ok = findViewById(R.id.cboard_ok);

        SharedPreferences sf = getSharedPreferences("userinfo",MODE_PRIVATE);
        name = sf.getString("name","");

        setTitle("글 작성");
        // 뒤로가기 버튼
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cboard_map)).getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        geocoder = new Geocoder(this);

        if(map != null){
            latLng = new LatLng(37.566643,126.978279);
            CameraPosition position = new CameraPosition.Builder()
                    .target(latLng).zoom(16f).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ic_marker));
            markerOptions.position(latLng);
            markerOptions.title("서울시청");
            markerOptions.snippet("Tel:02-120");
            address = "서울시청"; // default value

            map.addMarker(markerOptions);

//            MyGeocodingThread thread = new MyGeocodingThread(latLng);
//            thread.start();
//
//            String address = "서울특별시 중구 서소문동 37-9";
//            MyReverseGeocodingThread reverseThread = new MyReverseGeocodingThread(address);
//            reverseThread.start();

            UiSettings uiSettings = map.getUiSettings(); // 지도 확대/축소(+ / -)
            uiSettings.setZoomControlsEnabled(true);

        }

        ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = search.getText().toString();
                List<Address> addressList = null;
                try{
                    addressList = geocoder.getFromLocationName(str,5);
                }catch (IOException e){
                    e.printStackTrace();
                }

                if(addressList.size() == 0){
                    Toast.makeText(getApplicationContext(),"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                }else{
                    String []splitStr = addressList.get(0).toString().split(",");
                    address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    // 좌표(위도, 경도) 생성
                    latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title(str);
                    mOptions2.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.ic_marker));
                    mOptions2.snippet(address);
                    mOptions2.position(latLng);
                    // 마커 추가
                    map.clear();
                    map.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }

            }
        });
    }

/*    class MyReverseGeocodingThread extends Thread {
        String address;
        public MyReverseGeocodingThread(String address){
            this.address = address;
        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(CreateBoardActivity.this);
            try{
                List<Address> results = geocoder.getFromLocationName(address,1);
                Address resultAddress = results.get(0);
                LatLng latLng = new LatLng(resultAddress.getLatitude(),resultAddress.getLongitude());

                Message msg = new Message();
                msg.what = 200;
                msg.obj = latLng;
                handler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class MyGeocodingThread extends Thread {
        LatLng latLng;

        public MyGeocodingThread(LatLng latLng){
            this.latLng = latLng;
        }

        @Override
        public void run() {
            Geocoder geocoder = new Geocoder(CreateBoardActivity.this);

            List<Address> addresses = null;
            String addressText = "";
            try{
                addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                Thread.sleep(500);

                if(addresses != null && addresses.size()>0){
                    Address address = addresses.get(0);
                    addressText = address.getAdminArea()+""+(address.getMaxAddressLineIndex()>0?address.getAddressLine(0):address.getLocality())+" ";

                    String txt = address.getSubLocality();

                    if(txt != null)
                        addressText += txt +" ";

                    addressText += address.getThoroughfare()+" "+address.getSubThoroughfare();

                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = addressText;
                    handler.sendMessage(msg);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case 100: {
                  Toast toast = Toast.makeText(CreateBoardActivity.this,(String)msg.obj,Toast.LENGTH_SHORT);
                  toast.show();
                  break;
              }
              case 200: {
                  MarkerOptions markerOptions = new MarkerOptions();
                  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
                  markerOptions.position((LatLng)msg.obj);
                  markerOptions.title("서울시립박물관");
                  map.addMarker(markerOptions);
              }
          }
      };
    };*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.board_create,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // 뒤로 가기
            case android.R.id.home:
                finish();
                return true;
            case R.id.board_create_menu:
                BoardVO vo = new BoardVO(title.getText().toString(),content.getText().toString(),name,String.valueOf(latLng.latitude),String.valueOf(latLng.longitude),address);
                Call<Map<String, String>> call = boardInterface.insertBoard(vo);
                call.enqueue(new Callback<Map<String, String>>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        Map<String,String> map = response.body();
                        if(map.get("code").equals("200")){
                            Toast.makeText(getApplicationContext(),"글 작성 완료.",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"글 작성 실패.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"항목 모두를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
