package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class SerialPort {

    private static final String TAG = "SerialPort";

    static {
        System.loadLibrary("serial_port");
    }

    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudRate, int flags) throws SecurityException, IOException {
        //检查访问权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                // 没有读/写权限，尝试对文件进行提权
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                throw new SecurityException();
            }
        }
        //不要删除或重命名字段mFd:原生方法close()使用了该字段
        FileDescriptor mFd = open(device.getAbsolutePath(), baudRate, flags);
        if (mFd == null) {
            Log.i(TAG, "open method return null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    /**
     * 打开串口
     *
     * @param path     设备路径
     * @param baudRate 波特率
     * @param flags    标记
     * @return FileDescriptor
     */
    private native static FileDescriptor open(String path, int baudRate, int flags);

    /**
     * 关闭串口
     */
    public native void close();

    /**
     * 获取输入输出流
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * 关闭IO流
     */
    public void closeIOStream() {
        try {
            mFileInputStream.close();
            mFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        mFileInputStream = null;
        mFileOutputStream = null;
    }

}
