package com.example.ustc.newbluetooth.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class NightHeartRataData extends Data{

    public byte size;
    public byte data1;
    public byte data2;
    public byte data3;
    public byte data4;
    public byte data5;
    public byte data6;
    public byte data7;

    public NightHeartRataData(){
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

        return data;
    }

    public static NightHeartRataData toData(byte[] dataBytes){
        NightHeartRataData data = new NightHeartRataData();
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
        return data;
    }
}
