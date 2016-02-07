package com.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.utils.LockscreenService;
import com.example.utils.LockscreenUtils;


public class TopNavFragment extends Fragment {

    LockscreenUtils utils;
    Button lockButton;


    public TopNavFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new LockscreenUtils();
        Activity a = getActivity();
        a.startService(new Intent(a, LockscreenService.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_nav, container, false);
        lockButton=(Button) v.findViewById(R.id.lock_button);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                Intent i = new Intent(activity, LockScreenActivity.class);
                i.putExtra("state", savedInstanceState);
                startActivity(i);

            }
        });
        return v;
    }

}
