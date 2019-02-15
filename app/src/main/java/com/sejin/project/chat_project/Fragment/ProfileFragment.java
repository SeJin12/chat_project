package com.sejin.project.chat_project.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sejin.project.chat_project.R;

public class ProfileFragment extends Fragment {

    TextView test;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        test = view.findViewById(R.id.test);

        SharedPreferences sf = this.getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);

        String email = sf.getString("email","");
        String name = sf.getString("name","");
        String phone = sf.getString("phone","");
        String region = sf.getString("region","");
        test.setText(email+" "+name+" "+phone+" "+region);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }
}
