package com.example.ustc.newbluetooth.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class TimeData extends Data{

    public byte second;
    public byte minite;
    public byte hour;
    public byte day;
    public byte month;
    public byte[] year = new byte[2];

    public TimeData(){
        header[0] = 0x3c;
        header[1] = -61;
        type = 0x01;
        length = 0x07;
    }

    public byte[] toBytes(){
        byte[] data = new byte[Statics.dateLength];
        System.arraycopy(header,0,data,0,2);
        data[2] = type;
        data[3] = length;
        data[4] = second;
        data[5] = minite;
        data[6] = hour;
        data[7] = day;
        data[8] = month;
        System.arraycopy(year,9,data,0,2);
        return data;
    }

    public static TimeData toData(byte[] dataBytes){
        TimeData data = new TimeData();
        System.arraycopy(dataBytes,0,data.header,0,2);
        data.type = dataBytes[2];
        data.length = dataBytes[3];
        data.second = dataBytes[4];
        data.minite = dataBytes[5];
        data.hour = dataBytes[6];
        data.day = dataBytes[7];
        data.month = dataBytes[8];
        System.arraycopy(dataBytes,9,data.year,0,2);
        return data;
    }
}
