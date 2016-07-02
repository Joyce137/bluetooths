package com.health.app.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class HeartRateData extends Data{
    public static int SIZE = 14;
    public byte date;
    public byte month;
    public byte size;
    public byte data1;
    public byte data2;
    public byte data3;
    public byte data4;
    public byte data5;
    public byte data6;
    public byte data7;
    public byte data8;
    public byte data9;
    public byte data10;
    public byte data11;
    public byte data12;

    public HeartRateData(){
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
        data[4] = date;
        data[5] = month;
        data[6] = size;
        data[7] = data1;
        data[8] = data2;
        data[9] = data3;
        data[10] = data4;
        data[11] = data5;
        data[12] = data6;
        data[13] = data7;
        data[14] = data8;
        data[15] = data9;
        data[16] = data10;
        data[17] = data11;
        data[18] = data12;

        return data;
    }

    public static HeartRateData toData(byte[] dataBytes){
        HeartRateData data = new HeartRateData();
        System.arraycopy(dataBytes,0,data.header,0,2);
        data.type = dataBytes[2];
        data.length = dataBytes[3];
        data.date = dataBytes[4];
        data.month = dataBytes[5];
        data.size = dataBytes[6];
        data.data1 = dataBytes[7];
        data.data2 = dataBytes[8];
        data.data3 = dataBytes[9];
        data.data4 = dataBytes[10];
        data.data5 = dataBytes[11];
        data.data6 = dataBytes[12];
        data.data7 = dataBytes[13];
        data.data8 = dataBytes[14];
        data.data9 = dataBytes[15];
        data.data10 = dataBytes[16];
        data.data11 = dataBytes[17];
        data.data12 = dataBytes[18];

        return data;
    }
}
