package com.health.app.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class NightHeartRateData extends Data{

    public static int SIZE = 12;
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

    public NightHeartRateData(){
        header[0] = 0x3c;
        header[1] = -61;
        type = 0x04;
        length = 0x0c;
    }

    public byte[] toBytes(){
        byte[] data = new byte[Statics.dateLength];
        System.arraycopy(header,0,data,0,2);
        data[2] = type;
        data[3] = length;
        data[4] = size;
        data[5] = data1;
        data[6] = data2;
        data[7] = data3;
        data[8] = data4;
        data[9] = data5;
        data[10] = data6;
        data[11] = data7;
        data[12] = data8;
        data[13] = data9;
        data[14] = data10;
        data[15] = data11;
        data[16] = data12;

        return data;
    }

    public static NightHeartRateData toData(byte[] dataBytes){
        NightHeartRateData data = new NightHeartRateData();
        System.arraycopy(dataBytes,0,data.header,0,2);
        data.type = dataBytes[2];
        data.length = dataBytes[3];
        data.size = dataBytes[4];
        data.data1 = dataBytes[5];
        data.data2 = dataBytes[6];
        data.data3 = dataBytes[7];
        data.data4 = dataBytes[8];
        data.data5 = dataBytes[9];
        data.data6 = dataBytes[10];
        data.data7 = dataBytes[11];
        data.data8 = dataBytes[12];
        data.data9 = dataBytes[13];
        data.data10 = dataBytes[14];
        data.data11 = dataBytes[15];
        data.data12 = dataBytes[16];

        return data;
    }
}
