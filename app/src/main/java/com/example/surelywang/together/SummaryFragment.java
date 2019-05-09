package com.example.surelywang.together;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryFragment extends Fragment {

    View v;
    Context context;
    Fragment fragment;
    private FragmentManager fragmentManager;
    Handler handler;
    ImageView chart;
    boolean global = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_summary_fragment, container, false);
        context = container.getContext();
        handler = new Handler();

        chart = v.findViewById(R.id.summary_chart);


        // Initialize spinner
        //get the spinner from the xml.
        Spinner dropdown = v.findViewById(R.id.selections);
        //create a list of items for the spinner.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.options, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        dropdown.setAdapter(adapter);
        dropdown.setSelection(2);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (global) {
                    global = false;
                } else {
                    chart.setImageResource(R.drawable.trends2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return v;
    }

}