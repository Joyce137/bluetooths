package com.health.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.app.R;
import com.health.app.health.HealthInfoActivity;
import com.health.app.health.ViewActivity1;
import com.health.app.health.ViewActivity2;
import com.health.app.health.ViewActivity3;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements View.OnClickListener {

    LinearLayout llayoutInfo;
    LinearLayout llayoutHeight,llayoutKG,llayoutBMI,llayoutSugar,llayoutPressure,llayoutFat,llayoutHB;
    TextView textHeight,textKG,textBMI,textSugar,textPressure;
    TextView textTime1,textTime2,textTime3,textTime4,textTime5,textTime6,textTime7;
    ImageView imageHeight,imageKG,imageBMI,imageSugar,imagePressure,imageFat,imageHB;
    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_health, null);


        initView(view);
        return view;
    }

    private void initView(View view) {
        llayoutHeight = (LinearLayout)view.findViewById(R.id.llayoutHeight);
        llayoutKG = (LinearLayout)view.findViewById(R.id.llayoutKG);
        llayoutBMI = (LinearLayout)view.findViewById(R.id.llayoutBMI);
        llayoutSugar = (LinearLayout)view.findViewById(R.id.llayoutSugar);
        llayoutPressure = (LinearLayout)view.findViewById(R.id.llayoutPressure);
        llayoutFat = (LinearLayout)view.findViewById(R.id.llayoutFat);
        llayoutHB = (LinearLayout)view.findViewById(R.id.llayoutHB);
        llayoutInfo = (LinearLayout)view.findViewById(R.id.llayoutInfo);

        llayoutHeight.setOnClickListener(this);
        llayoutKG.setOnClickListener(this);
        llayoutBMI.setOnClickListener(this);
        llayoutSugar.setOnClickListener(this);
        llayoutPressure.setOnClickListener(this);
        llayoutFat.setOnClickListener(this);
        llayoutHB.setOnClickListener(this);

        llayoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), HealthInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
//        llayoutHeight,llayoutKG,llayoutBMI,llayoutSugar,llayoutPressure,llayoutFat,llayoutHB;
        Intent intent = new Intent();
        switch (view.getId()){

            case R.id.llayoutHeight:
                intent.setClass(getActivity(), ViewActivity1.class);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutKG:
                intent.setClass(getActivity(), ViewActivity2.class);
                intent.putExtra("type",2);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutBMI:
                intent.setClass(getActivity(), ViewActivity2.class);
                intent.putExtra("type",3);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutSugar:
                intent.setClass(getActivity(), ViewActivity2.class);
                intent.putExtra("type",4);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutPressure:
                intent.setClass(getActivity(), ViewActivity2.class);
                intent.putExtra("type",5);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutFat:
                intent.setClass(getActivity(), ViewActivity3.class);
                getActivity().startActivity(intent);
                break;
            case R.id.llayoutHB:
                intent.setClass(getActivity(), ViewActivity2.class);
                intent.putExtra("type",6);
                getActivity().startActivity(intent);
                break;
        }
    }
}
