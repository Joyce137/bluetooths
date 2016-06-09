package com.health.app.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.health.app.R;
import com.health.app.util.Validate;
import com.mslibs.utils.MsStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepMonthFragment extends Fragment {

    TextView textStep,textType,textTarget,textNum,textK;
    ImageView imageReward;
    LineChartView chart;
    private LineChartData data;
    public StepMonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_month, null);

        chart = (LineChartView)view.findViewById(R.id.chart);
        textStep  = (TextView)view.findViewById(R.id.textStep);
        textType  = (TextView)view.findViewById(R.id.textType);
        textTarget  = (TextView)view.findViewById(R.id.textTarget);
        textNum  = (TextView)view.findViewById(R.id.textNum);
        textK  = (TextView)view.findViewById(R.id.textK);
        imageReward  = (ImageView)view.findViewById(R.id.imageReward);

        initData();
        chart.setOnValueTouchListener(new ValueTouchListener());
        return view;
    }

    private void initData() {
        ArrayList<String> hearts = new ArrayList<String>();
        for(int i = 0;i< ExampleData.monthSteps.length;i++){
            hearts.add(""+ExampleData.monthSteps[i]);
        }

        generateTempoData(hearts);
    }

    private void generateTempoData(ArrayList<String> hearts) {
        float minHeight = 120;
        float maxHeight = 180;
        float tempoRange = 11;


        int numValues = 24;
        if(hearts != null){
            numValues = hearts.size();
        }

        Line line;
        List<PointValue> values;
        List<Line> lines = new ArrayList<Line>();

        float multiple = 2f;
        // 7,1.4   8,1.2   5,2.0  4,2.5  3,3.3  1,9
        float result = getMax(hearts);
        int max = 0;

        if(result > 1000000){
            max = 100000;
            result= result/100000;
        }else  if(result > 100000){
            max = 10000;
            result= result/10000;
        }else  if(result > 10000){
            max = 1000;
            result= result/1000;
        }else  if(result > 1000){
            max = 100;
            result= result/100;
        }else  if(result > 100){
            max = 10;
            result= result/10;
        }

        int s = 1;

        s = (int) result+1;

//	        	if(s == 4){
//	        		multiple = 2.5f;
//	        	}else if(s == 5){
//	        		multiple = 2.0f;
//	        	}else if(s == 3){
//	        		multiple = 3.3f;
//	        	}else if(s == 2){
//	        		multiple = 5.0f;
//	        	}else if(s == 1){
//	        		multiple = 9f;
//	        	}else if(s == 6){
//	        		multiple = 1.6f;
//	        	}else if(s == 7){
//	        		multiple = 1.4f;
//	        	}else if(s == 8){
//	        		multiple = 1.2f;
//	        	}else if(s == 9){
//	        		multiple = 1.0f;
//	        	}else{
//	        		multiple = 1.0f;
//	        	}
        multiple = 10f/s;

        Log.e("max", result+"--------"+((int) result+1)+"-------"+multiple);


        values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            float realTempo = (float) (MsStringUtils.str2double(hearts.get(i)) );
            float revertedTempo = multiple*realTempo/max;
            values.add(new PointValue(i, revertedTempo));
        }
        line = new Line(values);
        line.setColor(getActivity().getResources().getColor(R.color.text_color3));
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(true);
        line.setPointRadius(2);
        line.setStrokeWidth(1);
        lines.add(line);

//预警线
        values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            float realTempo = 150f;
            float revertedTempo = multiple*realTempo/10;
            values.add(new PointValue(i, revertedTempo));
        }
        line = new Line(values);
        line.setColor(getActivity().getResources().getColor(R.color.red_highlight));
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setHasPoints(false);
        line.setPointRadius(2);

        line.setStrokeWidth(1);
        lines.add(line);

        data = new LineChartData(lines);

        List<AxisValue> axisValues2 = new ArrayList<AxisValue>();
        for (float i = 0; i < hearts.size(); i ++) {
            axisValues2.add(new AxisValue(i).setLabel(Validate.subZeroAndDot(i+"")));
        }
        Axis distanceAxis = new Axis(axisValues2);
//        distanceAxis.setName("Time");
        distanceAxis.setAutoGenerated(false);
//        distanceAxis.setTextColor(ChartUtils.COLOR_ORANGE);
        distanceAxis.setMaxLabelChars(5);
        distanceAxis.setFormatter(new SimpleAxisValueFormatter());
        distanceAxis.setHasLines(false);
        distanceAxis.setTextSize(12);
//        distanceAxis.setHasTiltedLabels(true);
        data.setAxisXBottom(distanceAxis);

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        for (float i = 0; i < 11; i += 1) {
            String str = Validate.subZeroAndDot(""+(i*s/10));
            Log.e("generateTempoData: ", str );


            axisValues.add(new AxisValue(i).setLabel(str));
        }

        Axis tempoAxis = new Axis(axisValues).setHasLines(true).setMaxLabelChars(1).setTextSize(12);

        if(getMax(hearts) > 1000000){
            tempoAxis.setName("十万步");
        }else  if(getMax(hearts) > 100000){
            tempoAxis.setName("万步");
        }else  if(getMax(hearts) > 10000){
            tempoAxis.setName("千步");
        }else  if(getMax(hearts) > 1000){
            tempoAxis.setName("百步");
        }else  if(getMax(hearts) > 100){
            tempoAxis.setName("十步");
        }
        data.setAxisYLeft(tempoAxis);

        Axis axisYr = new Axis().setHasLines(true);
        List<AxisValue> axisValuesYr = new ArrayList<AxisValue>();

        for (float i = 0; i < 11; i += 1f) {
            axisValuesYr.add(new AxisValue(i).setLabel( Validate.subZeroAndDot(""+i)));
        }
        axisYr.setValues(axisValuesYr);
        axisYr.setName("卡路里");
        axisYr.setTextColor(Color.parseColor("#fec85d"));
        axisYr.setTextSize(12);
        axisYr.setMaxLabelChars(1);
        data.setAxisYRight(axisYr);


        chart.setLineChartData(data);

        Viewport v = chart.getMaximumViewport();
        v.set(v.left, tempoRange, v.right, 0);
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private String formatMinutes(float value) {
        StringBuilder sb = new StringBuilder();

        // translate value to seconds, for example
        int valueInSeconds = (int) (value * 60);
        int minutes = (int) Math.floor(valueInSeconds / 60);
        int seconds = (int) valueInSeconds % 60;

        sb.append(String.valueOf(minutes)).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(String.valueOf(seconds));
        return sb.toString();
    }

    private int getMax(ArrayList<String> hearts) {
        int result = 0;

        ArrayList<Integer> strings = new ArrayList<Integer>();
        for(int i = 0;i< hearts.size();i++){
            strings.add(Integer.valueOf(hearts.get(i)));
        }
        result = Collections.max(strings);
        return  result;
    }
    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "步数：" + ExampleData.monthSteps[pointIndex], Toast.LENGTH_SHORT).show();
        }

        //+"，日期："+Validate.subZeroAndDot(""+(value.getX()+1))
        @Override
        public void onValueDeselected() {

        }
    }
}
