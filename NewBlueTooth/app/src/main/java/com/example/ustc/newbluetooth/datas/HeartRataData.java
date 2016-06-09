package com.example.ustc.newbluetooth.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class HeartRataData extends Data{

    public byte day;
    public byte month;
    public byte size;
    public byte data1;
    public byte data2;
    public byte data3;
    public byte data4;
    public byte data5;
    public byte data6;
    public byte data7;

    public HeartRataData(){
        header[0] = 0x3c;
        header[1] = -61;
        type = 0x05;
        length = 0x0e;
    }

    public byte[] toBytes(){
        byte[] data = new byte[Statics.dateLength];
        System.arraycopy(header,0,data,0,2);
        data[2] = type;
        data[3] = length;
        data[4] = day;
        data[5] = month;
        data[6] = size;
        data[7] = data1;
        data[8] = data2;
        data[9] = data3;
        data[10] = data4;
        data[11] = data5;
        data[12] = data6;
        data[13] = data7;

        return data;
    }

    public static HeartRataData toData(byte[] dataBytes){
        HeartRataData data = new HeartRataData();
        System.arraycopy(dataBytes,0,data.header,0,2);
        data.type = dataBytes[2];
        data.length = dataBytes[3];
        data.day = dataBytes[4];
        data.month = dataBytes[5];
        data.size = dataBytes[6];
        data.data1 = dataBytes[7];
        data.data2 = dataBytes[8];
        data.data3 = dataBytes[9];
        data.data4 = dataBytes[10];
        data.data5 = dataBytes[11];
        data.data6 = dataBytes[12];
        data.data7 = dataBytes[13];

        return data;
    }
}
