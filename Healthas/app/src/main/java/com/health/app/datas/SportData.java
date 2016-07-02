package com.health.app.datas;

/**
 * Created by CaoRuijuan on 5/19/16.
 */
public class SportData extends Data{

    public static int SIZE = 12;
    public byte date;
    public byte month;
    public byte[] steps = new byte[2];
    public byte properSportTimes;
    public byte strongSportTimes;
    public byte sportStrength;
    public byte validSportTimes;

    public SportData(){
        header[0] = 0x3c;
        header[1] = -61;
        type = 0x02;
        length = 0x07;
    }

    public byte[] toBytes(){
        byte[] data = new byte[Statics.dateLength];
        System.arraycopy(header,0,data,0,2);
        data[2] = type;
        data[3] = length;
        data[4] = date;
        data[5] = month;
        System.arraycopy(steps,6,data,0,2);
        data[8] = properSportTimes;
        data[9] = strongSportTimes;
        data[10] = sportStrength;
        data[11] = validSportTimes;

        return data;
    }

    public static SportData toData(byte[] dataBytes){
        SportData data = new SportData();
        System.arraycopy(dataBytes,0,data.header,0,2);
        data.type = dataBytes[2];
        data.length = dataBytes[3];
        data.date = dataBytes[4];
        data.month = dataBytes[5];
        System.arraycopy(dataBytes,6,data.steps,0,2);
        data.properSportTimes = dataBytes[8];
        data.strongSportTimes = dataBytes[9];
        data.sportStrength = dataBytes[10];
        data.validSportTimes = dataBytes[11];
        return data;
    }
}
