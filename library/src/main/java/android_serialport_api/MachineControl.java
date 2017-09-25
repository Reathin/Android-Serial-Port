package android_serialport_api;

import android.util.Log;

import com.rair.serialport.ByteUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MachineControl {

    private static SerialPortFinder mSerialPortFinder = new SerialPortFinder();

    private String mDeviceName;
    private int mBaudRate;
    private SerialPort mSerialPort;

    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadCOMThread mReadCOMThread;

    // 枚举所有串口的设备名。
    public static String[] getCOMList() {
        return mSerialPortFinder.getAllDevicesPath();
    }

    // sample devName = "/dev/ttyS3"，baudRate = 9600。
    public MachineControl(String devName, int baudRate) {
        this.mDeviceName = devName;
        this.mBaudRate = baudRate;
        this.mSerialPort = null;
    }

    // 打开串口
    public boolean openCOM() {
        Log.i("MachineControl", "openCOM");
        String[] comList = getCOMList();
        for (String comname : comList) {
            System.out.println(comname);
        }
        if (this.mSerialPort == null) {
            try {
                System.out.println("设备名：" + mDeviceName + ",波特率:" + mBaudRate);
                this.mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
                this.mOutputStream = mSerialPort.getOutputStream();
                this.mInputStream = mSerialPort.getInputStream();
                mReadCOMThread = new ReadCOMThread();// 读取串口数据
                mReadCOMThread.setName("Machine.ReadCOMThread");
                mReadCOMThread.start();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                this.mSerialPort = null;
            }
        }
        return false;
    }

    /**
     * 关闭串口
     */
    public void closeCOM() {
        Log.i("MachineControl", "closeCOM");
        if (this.mSerialPort != null) {
            mReadCOMThread.interrupt();
            this.mSerialPort.closeIOStream();
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
    }

    private int retryCount = 3; //重试次数


    private String Res = "";

    /**
     * 发送报文
     *
     * @param TOut 重试次数
     * @param data 报文
     * @return
     */
    private boolean sendCMD(int TOut, byte[] data) {
        Res = null;
        try {
            mOutputStream.write(data);
            mOutputStream.flush();
            int i = TOut;
            while (i > 0) {
                Thread.sleep(500);
                System.out.println("sleep 1s");
                if (Res != null) return true;
                i--;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取返回结果
     *
     * @return
     */
    public String getRes() {
        return Res;
    }

    private class ReadCOMThread extends Thread {
        @Override
        public void run() {
            int size;
            while (!isInterrupted()) {
                try {
                    byte[] buffer = new byte[16];
                    if (mInputStream == null)
                        break;
                    mOutputStream.write(buffer);
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        Res = ByteUtil.hexBytesToString(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
