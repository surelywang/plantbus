package com.example.surelywang.together;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.example.surelywang.plantbus.R;

public class TimeFragment extends Fragment {

    View v;
    Context context;
    Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_time_fragment, container, false);
        context = container.getContext();

//        Button button = (Button) v.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), DestinationActivity.class);
//                startActivity(intent);
//            }
//        });

        return v;
    }
}