package com.sejin.project.chat_project.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.sejin.project.chat_project.LoginActivity;
import com.sejin.project.chat_project.R;

public class SettingPreferenceFragment extends PreferenceFragmentCompat {


    SharedPreferences prefs;
    Preference logout;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference);

        logout = findPreference("logout");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(),"logout",Toast.LENGTH_LONG).show();
                SharedPreferences sf = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE); // FIXME 조심 되는지 나중에 꼭 확인할것 레이아웃 중첩되는지 안되는지
                SharedPreferences.Editor editor = sf.edit();
                editor.putBoolean("auto",false);
                editor.commit();

                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                return false;
            }
        });

    }


}
