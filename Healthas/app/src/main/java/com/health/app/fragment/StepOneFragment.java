package com.health.app.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.health.app.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepOneFragment extends Fragment {
    ColumnChartView chart;
    private ColumnChartData data;
    TextView textStep,textType,textTarget,textNum,textK;
    ImageView imageReward;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;

    public StepOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_one, null);
        initView(view);

        return view;
    }
    private void initView(View view) {
        chart = (ColumnChartView)view.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        textStep  = (TextView)view.findViewById(R.id.textStep);
        textType  = (TextView)view.findViewById(R.id.textType);
        textTarget  = (TextView)view.findViewById(R.id.textTarget);
        textNum  = (TextView)view.findViewById(R.id.textNum);
        textK  = (TextView)view.findViewById(R.id.textK);
        imageReward  = (ImageView)view.findViewById(R.id.imageReward);
        initData();
    }
    private void initData() {

        int numSubcolumns = 2;
        int numColumns = 7;
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
//               for (int j = 0; j < numSubcolumns; ++j) {
//                   values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
//               }
            values.add(new SubcolumnValue((float)0.5 * 50f + 5, Color.parseColor("#47c7fb")));
            values.add(new SubcolumnValue((float)0.3 * 50f + 5, Color.parseColor("#fec85d")));

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis();
            List<AxisValue> axisValues = new ArrayList<AxisValue>();

            AxisValue axisValue1 = new AxisValue(0, "周一".toCharArray());
            AxisValue axisValue2 = new AxisValue(1, "周二".toCharArray());
            AxisValue axisValue3 = new AxisValue(2, "周三".toCharArray());
            AxisValue axisValue4 = new AxisValue(3, "周四".toCharArray());
            AxisValue axisValue5 = new AxisValue(4, "周五".toCharArray());
            AxisValue axisValue6 = new AxisValue(5, "周六".toCharArray());
            AxisValue axisValue7 = new AxisValue(6, "周七".toCharArray());
            axisValues.add(axisValue1);
            axisValues.add(axisValue2);
            axisValues.add(axisValue3);
            axisValues.add(axisValue4);
            axisValues.add(axisValue5);
            axisValues.add(axisValue6);
            axisValues.add(axisValue7);
            axisX.setValues(axisValues);

            Axis axisY = new Axis().setHasLines(true);
//            List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
//
//            axisY.setValues(axisValuesY);


            Axis axisYr = new Axis().setHasLines(true);
            if (hasAxesNames) {
//                   axisX.setName("Axis X");
                axisY.setName("步");
                axisY.setTextColor(Color.parseColor("#47c7fb"));
                axisYr.setName("卡路里");
                axisYr.setTextColor(Color.parseColor("#fec85d"));
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            data.setAxisYRight(axisYr);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setColumnChartData(data);

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

    }

}
