package com.health.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.app.MainApplication;
import com.health.app.R;
import com.health.app.user.AboutusActivity;
import com.health.app.user.DeviceListActivity;
import com.health.app.user.DeviceSettingtActivity;
import com.health.app.user.TargetActivity;
import com.health.app.user.UserInfoActivity;
import com.health.app.user.UserPwdUpateActivity;
import com.health.app.user.UserSignActivity;
import com.health.app.util.Preferences;
import com.health.app.widget.RoundAngleImageView;


public class LeftFragment extends Fragment implements View.OnClickListener{
    RoundAngleImageView imageUser;
    TextView textName,textDevice,textMyDevice;
    Button btnOut;
    LinearLayout llayoutDevice,llayoutTarget,llayoutPwd,llayoutAbout,llayoutDeList;
    private OnFragmentInteractionListener mListener;
    MainApplication mApp;

    BroadcastReceiver leftBroadcastReceiver;
    LocalBroadcastManager broadcastManager;
    public LeftFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = MainApplication.getInstance();

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        leftBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                if(intent.getAction().equals(Preferences.BROADCAST_ACTION.USER_UPDATE)){

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Preferences.BROADCAST_ACTION.USER_UPDATE);
        broadcastManager.registerReceiver(leftBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(leftBroadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_manage, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        imageUser = (RoundAngleImageView) view.findViewById(R.id.imageUser);
        textName = (TextView) view.findViewById(R.id.textName);
        textMyDevice = (TextView) view.findViewById(R.id.textMyDevice);
        textDevice = (TextView) view.findViewById(R.id.textDevice);
        btnOut = (Button) view.findViewById(R.id.btnOut);
        llayoutDevice = (LinearLayout) view.findViewById(R.id.llayoutDevice);
        llayoutTarget = (LinearLayout) view.findViewById(R.id.llayoutTarget);
        llayoutPwd = (LinearLayout) view.findViewById(R.id.llayoutPwd);
        llayoutAbout = (LinearLayout) view.findViewById(R.id.llayoutAbout);
        llayoutDeList = (LinearLayout) view.findViewById(R.id.llayoutDeList);
        btnOut.setOnClickListener(this);
        llayoutDevice.setOnClickListener(this);
        llayoutTarget.setOnClickListener(this);
        imageUser.setOnClickListener(this);
        llayoutAbout.setOnClickListener(this);
        llayoutDeList.setOnClickListener(this);
        textMyDevice.setOnClickListener(this);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnOut:
                mApp.setPreference(Preferences.LOCAL.TOKEN,"");
                Intent intent = new Intent();
                intent.setClass(getActivity(), UserSignActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;

            case R.id.llayoutDevice:

                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), DeviceSettingtActivity.class);
                getActivity().startActivity(intent2);

                break;
            case R.id.llayoutTarget:

                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), TargetActivity.class);
                getActivity().startActivity(intent3);

                break;
            case R.id.imageUser:

                Intent intent4 = new Intent();
                intent4.setClass(getActivity(), UserInfoActivity.class);
                getActivity().startActivity(intent4);

                break;
            case R.id.llayoutAbout:

                Intent intent5 = new Intent();
                intent5.setClass(getActivity(), AboutusActivity.class);
                getActivity().startActivity(intent5);

                break;
            case R.id.llayoutDeList:
                Log.e("llayoutList", "onClick:llayoutList " );
                Intent intent6 = new Intent();
                intent6.setClass(getActivity(), DeviceListActivity.class);
                getActivity().startActivity(intent6);

                break;
            case R.id.textMyDevice:
                Log.e("textMyDevice", "onClick:textMyDevice " );
                Intent intent7 = new Intent();
                intent7.setClass(getActivity(), DeviceListActivity.class);
                getActivity().startActivity(intent7);

                break;

            default:
                break;
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
