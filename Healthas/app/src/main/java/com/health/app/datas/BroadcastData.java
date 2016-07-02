package com.health.app.datas;

/**
 * Created by CaoRuijuan on 6/21/16.
 */
public class BroadcastData {
    public static final int SIZE = 62;
    public byte[] header = new byte[12];
    public byte[] address = new byte[6];
    public byte report_pluse;
    public byte pluse_freq;
    public byte flag_power;
    public byte battery;
    public byte broken_state;
    public byte fall_act;
    public byte sport_level;
    public byte[] step = new byte[2];
    public byte tx_power;
    public byte sos_state;
    public byte[] others = new byte[33];

    public static BroadcastData getData(byte[] bytes)
    {
        BroadcastData data = new BroadcastData();
        System.arraycopy(bytes,0,data.header,0,12);
        System.arraycopy(bytes,12,data.address,0,6);
        data.report_pluse = bytes[18];
        data.pluse_freq = bytes[19];
        data.flag_power = bytes[20];
        data.battery = bytes[21];
        data.broken_state = bytes[22];
        data.fall_act = bytes[23];
        data.sport_level = bytes[24];
        System.arraycopy(bytes,25,data.step,0,2);
        data.tx_power = bytes[27];
        data.sos_state = bytes[28];
        System.arraycopy(bytes,29,data.others,0,33);
        return data;
    }
}
