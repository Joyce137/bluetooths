package com.health.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.health.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends Fragment {

    PullToRefreshListView listview;
    TextView textSport,textHealth,textMsg;
    View viewLine1,viewLine2,viewLine3;
    TextView  texTag1,texTag2,texTag3;

    TextView selectText,selectTag;
    View selectView;

    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_msg, null);

        initView(view);
        setSelect(textSport,viewLine1,texTag1);
        return view;
    }

    private void initView(View view) {
        listview = (PullToRefreshListView)view.findViewById(R.id.listview);
        textSport = (TextView)view.findViewById(R.id.textSport);
        textHealth = (TextView)view.findViewById(R.id.textHealth);
        textMsg = (TextView)view.findViewById(R.id.textMsg);
        viewLine1 = view.findViewById(R.id.viewLine1);
        viewLine2 = view.findViewById(R.id.viewLine2);
        viewLine3 = view.findViewById(R.id.viewLine3);
        texTag1 = (TextView)view.findViewById(R.id.texTag1);
        texTag2 = (TextView)view.findViewById(R.id.texTag2);
        texTag3 = (TextView)view.findViewById(R.id.texTag3);
        textSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(textSport,viewLine1,texTag1);
            }
        });
        textHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(textHealth,viewLine2,texTag2);
            }
        });
        textMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(textMsg,viewLine3,texTag3);
            }
        });
    }

    public void setSelect(TextView textView,View view,TextView textTag){
        if(selectText != null){
            selectText.setTextColor(getResources().getColor(R.color.gray333));
            selectTag.setVisibility(View.GONE);
            selectView.setVisibility(View.GONE);
        }

        selectText = textView;
        selectView = view;
        selectTag = textTag;

        selectText.setTextColor(getResources().getColor(R.color.text_color3));
        selectTag.setVisibility(View.VISIBLE);
        selectView.setVisibility(View.VISIBLE);
    }

}
