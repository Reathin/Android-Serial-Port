package com.rair.android_serial_port;

import android.util.Log;

import com.rairmmd.serialport.ByteUtil;
import com.rairmmd.serialport.OnDataReceiverListener;

import android_serialport_api.SerialPortController;

/**
 * Created by Rair on 2017/9/25.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */

public class Test {

    public static void main(String... args) {
        SerialPortController machineControl = new SerialPortController("/dev/ttys", 9600);
        boolean openCOM = machineControl.openCOM();
        if (openCOM) {
            machineControl.setOnDataReceiverListener(new OnDataReceiverListener() {
                @Override
                public void onDataReceiver(byte[] buffer, int size) {
                    Log.i("Rair", ByteUtil.hexBytesToString(buffer));
                }
            });
            machineControl.sendCMD(new byte[0x00]);
        } else {
            Log.i("Rair", "打开串口失败");
        }
    }
}
