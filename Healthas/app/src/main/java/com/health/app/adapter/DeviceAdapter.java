package com.health.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.app.R;
import com.health.app.device.Bind2Activity;
import com.health.app.type.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 6/9/16.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{
    List<DeviceBean> devicesBeans = new ArrayList<DeviceBean>();
    private Context context;

    public DeviceAdapter(Context context, List<DeviceBean> devicesBeans) {
        this.context = context;
        this.devicesBeans = devicesBeans;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_scan_device_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceBean p = devicesBeans.get(position);
        String bleName = p.getName();
        if(bleName==null)
            bleName = "未知";
        holder.nameText.setText(bleName);
        holder.addressText.setText(p.getAddress());
        holder.mContext = context;
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                   Context context = v.getContext();
//                   Intent intent = new Intent(context, FriendsDetailActivity.class);
//                   context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return devicesBeans == null ? 0 : devicesBeans.size();
    }

    // 重写的自定义ViewHolder
    public  class ViewHolder
            extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView addressText;

        public Context mContext;
        public ViewHolder(final View v) {
            super(v);

            nameText = (TextView) v.findViewById(R.id.scan_device_name);
            addressText=(TextView)v.findViewById(R.id.scan_device_address);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeviceBean bean=new DeviceBean(nameText.getText().toString(),addressText.getText().toString());
                    ((Bind2Activity)mContext).startActivity(v,bean);
                }
            });
        }
    }
}