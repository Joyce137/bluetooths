package com.health.app.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.app.R;
import com.health.app.widget.PagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonitorFragment extends Fragment implements View.OnClickListener{


    Button btnOne,btnWeek,btnMonth,btn1,btn2,btn3;
    TextView textHeart,textSleep,textStep;
    View viewLine1,viewLine2,viewLine3;
    Button selectBtn;
    Button selectBtn2;
    TextView selectText;
    View selectView;
    ViewPager viewpager;
    public static List<Fragment> fragments;
    Fragment heartOne,heartWeek,heartMonth;
    Fragment sleepOne,sleepWeek,sleepMonth;
    Fragment stepOne,stepWeek,stepMonth;
    private CircularFragmentPagerAdapter adapter;
    FragmentManager fragmentManager;
    private ViewPager.OnPageChangeListener listener;
    PagerIndicator pagerIndicator;
    public MonitorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_monitor, null);

        initView(view);

        setSelect(btnOne);

        return view;
    }

    private void initView(View view) {
        viewpager = (ViewPager)view.findViewById(R.id.viewpager);
        pagerIndicator = (PagerIndicator)view.findViewById(R.id.pagerIndicator);
        pagerIndicator.update(getActivity());
        textStep =  (TextView)view.findViewById(R.id.textStep);
        textSleep =  (TextView)view.findViewById(R.id.textSleep);
        textHeart =  (TextView)view.findViewById(R.id.textHeart);
        btnOne = (Button)view.findViewById(R.id.btnOne);
        btnWeek = (Button)view.findViewById(R.id.btnWeek);
        btnMonth = (Button)view.findViewById(R.id.btnMonth);
        viewLine1 = view.findViewById(R.id.viewLine1);
        viewLine2 = view.findViewById(R.id.viewLine2);
        viewLine3 = view.findViewById(R.id.viewLine3);

        btn1 = (Button)view.findViewById(R.id.btn1);
        btn2 = (Button)view.findViewById(R.id.btn2);
        btn3 = (Button)view.findViewById(R.id.btn3);

        btnOne.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMonth.setOnClickListener(this);

        setSelect2(textHeart,viewLine1);

        fragmentManager = getChildFragmentManager();
        setFragments();
        adapter = new CircularFragmentPagerAdapter(getChildFragmentManager(), fragments);

        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        pagerIndicator.setPagerCountForGray(3);
        setSelect2(btn1);
//        pagerIndicator.setPagerIndex(0);
        FragmentChangeListener fragmentChangeListener = new FragmentChangeListener();
        viewpager.setOnPageChangeListener(fragmentChangeListener);
    }
    public void setSelect(Button button){
        if(selectBtn != null){
            selectBtn.setSelected(false);
        }
        selectBtn = button;
        selectBtn.setSelected(true);
    }
    public void setSelect2(Button button){
        if(selectBtn2 != null){
            selectBtn2.setSelected(false);
        }
        selectBtn2 = button;
        selectBtn2.setSelected(true);
    }
    public void setSelect2(TextView textView,View view){
        if(selectText != null){
            selectText.setTextColor(getActivity().getResources().getColor(R.color.gray666));
            selectView.setVisibility(View.GONE);
        }
        selectView = view;
        selectText = textView;

        selectText.setTextColor(getActivity().getResources().getColor(R.color.text_color));
        selectView.setVisibility(View.VISIBLE);
    }
    public void setFragments(){
        fragments = new ArrayList<Fragment>();

//        heartOne,heartWeek,heartMonth;
        heartOne = new HeartOneFragment();
        heartWeek = new HeartWeekFragment();
        heartMonth = new HeartMonthFragment();
        sleepOne = new SleepOneFragment();
        sleepWeek = new SleepWeekFragment();
        sleepMonth = new SleepMonthFragment();
        stepOne = new StepOneFragment();
        stepWeek = new StepWeekFragment();
        stepMonth = new StepMonthFragment();

        fragments.add(heartOne);
        fragments.add(sleepOne);
        fragments.add(stepOne);
        fragments.add(heartWeek);
        fragments.add(sleepWeek);
        fragments.add(stepWeek);
        fragments.add(heartMonth);
        fragments.add(sleepMonth);
        fragments.add(stepMonth);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOne:
                setSelect(btnOne);
                setSelect2(textHeart,viewLine1);
                viewpager.setCurrentItem(0);
                setSelect2(btn1);
                break;
            case R.id.btnWeek:
                setSelect(btnWeek);
                setSelect2(textHeart,viewLine1);
                viewpager.setCurrentItem(3);
                setSelect2(btn1);
                break;
            case R.id.btnMonth:
                setSelect2(textHeart,viewLine1);
                setSelect(btnMonth);
                viewpager.setCurrentItem(6);
                setSelect2(btn1);
                break;
        }
    }
    /**
     * ViewPager 页面改变监听器
     */
    private final class FragmentChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (null != listener) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (null != listener) {
                listener.onPageSelected(position);
            }
            viewpager.setCurrentItem(position);
            if(position == 0 ){
                setSelect2(textHeart,viewLine1);
                setSelect(btnOne);
                setSelect2(btn1);
            }else if( position == 1 ){
                setSelect(btnOne);
                setSelect2(textSleep,viewLine2);
                setSelect2(btn2);
            }else if( position == 2 ){
                setSelect(btnOne);
                setSelect2(textStep,viewLine3);
                setSelect2(btn3);
            }else if( position == 3 ){
                setSelect2(textHeart,viewLine1);
                setSelect(btnWeek);
                setSelect2(btn1);
            }else if( position == 4 ){
                setSelect2(btn2);
                setSelect2(textSleep,viewLine2);
                setSelect(btnWeek);
            }else if( position == 5 ){
                setSelect2(btn3);
                setSelect2(textStep,viewLine3);
                setSelect(btnWeek);
            }else if(position == 6){
                setSelect2(textHeart,viewLine1);
                setSelect2(btn1);
                setSelect(btnMonth);
            }else if(position == 7){
                setSelect2(btn2);
                pagerIndicator.setPagerIndex(1);
                setSelect(btnMonth);
            }else if(position == 8){
                setSelect2(textStep,viewLine3);
                setSelect2(btn3);
                setSelect(btnMonth);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (null != listener) {
                listener.onPageScrollStateChanged(state);
            }
        }
    }

}
