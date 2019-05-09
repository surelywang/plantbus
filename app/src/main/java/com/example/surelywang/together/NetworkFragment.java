package com.example.surelywang.together;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.surelywang.plantbus.R;

public class NetworkFragment extends Fragment {
    View v;
    Context context;
    Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_network_fragment, container, false);
        context = container.getContext();
        return v;
    }

}
