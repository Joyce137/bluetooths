package com.health.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.health.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepOneFragment extends Fragment {


    public SleepOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_one, null);
        return view;
    }

}
