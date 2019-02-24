package com.sejin.project.chat_project.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sejin.project.chat_project.Activity.LoginActivity;
import com.sejin.project.chat_project.R;

public class SettingFragment extends ListFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] datas = {"공지사항","로그아웃","Github"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,datas);
        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String item = l.getItemAtPosition(position).toString();
        if(item.equals("로그아웃")){
            SharedPreferences sf = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            sf.edit().clear().commit();
            Intent intent = new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else if(item.equals("Github")){
            String uri = "https://github.com/SeJin12/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
//        Toast.makeText(getContext(),text+" "+position+" "+id,Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }


    @Override
    public void onPause() {
        super.onPause();

    }

}
