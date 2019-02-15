package com.sejin.project.chat_project;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.sejin.project.chat_project.Fragment.BoardFragment;
import com.sejin.project.chat_project.Fragment.ChatFragment;
import com.sejin.project.chat_project.Fragment.ProfileFragment;
import com.sejin.project.chat_project.Fragment.SettingPreferenceFragment;

public class MainActivity extends AppCompatActivity {

    FragmentTabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        host = findViewById(R.id.tabhost);
        host.setup(this,getSupportFragmentManager(),R.id.content1);

        TabHost.TabSpec spec1 = host.newTabSpec("tab1");
        spec1.setIndicator(null,ResourcesCompat.getDrawable(getResources(),R.drawable.tab_person,null)); // label is tab name.
        host.addTab(spec1,ProfileFragment.class,null);

        TabHost.TabSpec spec2 = host.newTabSpec("tab2");
        spec2.setIndicator(null,ResourcesCompat.getDrawable(getResources(),R.drawable.tab_chat,null));
        host.addTab(spec2,ChatFragment.class,null);

        TabHost.TabSpec spec3 = host.newTabSpec("tab3");
        spec3.setIndicator(null,ResourcesCompat.getDrawable(getResources(),R.drawable.tab_board,null));
        host.addTab(spec3,BoardFragment.class,null);

        TabHost.TabSpec spec4 = host.newTabSpec("tab4");
        spec4.setIndicator(null,ResourcesCompat.getDrawable(getResources(),R.drawable.tab_settings,null));
        host.addTab(spec4,SettingPreferenceFragment.class,null);


    }
}
