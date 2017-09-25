package android_serialport_api;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class KeyBoardControl {
	public static final byte KeyCode_0 = 0x00;
	public static final byte KeyCode_1 = 0x01;
	public static final byte KeyCode_2 = 0x02;
	public static final byte KeyCode_3 = 0x03;
	public static final byte KeyCode_4 = 0x04;
	public static final byte KeyCode_5 = 0x05;
	public static final byte KeyCode_6 = 0x06;
	public static final byte KeyCode_7 = 0x07;
	public static final byte KeyCode_8 = 0x08;
	public static final byte KeyCode_9 = 0x09;
	public static final byte KeyCode_Enter = 0x0A;
	public static final byte KeyCode_Cancel = 0x0B;

	private static SerialPortFinder mSerialPortFinder = new SerialPortFinder();

	private Handler mNotifyHandler = null;
	private int mNotifyCode;

	private String mDeviceName;
	private int mBaudRate;
	private SerialPort mSerialPort;

	// private OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadCOMThread mReadCOMThread;

	// 枚举所有串口的设备名。
	public static String[] getCOMList() {
		return mSerialPortFinder.getAllDevicesPath();
	}

	// sample devName = "/dev/ttyS3"，baudRate = 9600。
	public KeyBoardControl(String devName, int baudRate) {
		this.mDeviceName = devName;
		this.mBaudRate = baudRate;

		this.mSerialPort = null;
	}

	public void setOnKeyCodeListener(Handler handler, int notify_code) {
		this.mNotifyHandler = handler;
		this.mNotifyCode = notify_code;
	}

	// 打开串口
	public boolean openCOM() {
		if (TextUtils.isEmpty(mDeviceName))
			return false;

		Log.i("ComKeyBoardControl", "openCOM");

		if (this.mSerialPort == null) {
			try {
				this.mSerialPort = new SerialPort(new File(mDeviceName), mBaudRate, 0);
				// this.mOutputStream = mSerialPort.getOutputStream();
				this.mInputStream = mSerialPort.getInputStream();

				mReadCOMThread = new ReadCOMThread();// 读取串口数据
				mReadCOMThread.setName("KeyBoardControl.ReadCOMThread");
				mReadCOMThread.start();

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				this.mSerialPort = null;
			}
		}

		return false;
	}

	// 关闭串口。
	public void closeCOM() {
		if (TextUtils.isEmpty(mDeviceName))
			return;

		Log.i("KeyBoardControl", "closeCOM");

		if (this.mSerialPort != null) {
			mReadCOMThread.interrupt();

			this.mSerialPort.closeIOStream();
			this.mSerialPort.close();
			this.mSerialPort = null;
		}
	}

	private int start = 0;// 开始接收
	private byte[] Receive = null;// 接收串口数据
	private int Res = -1;
	private byte[] Content = null;// 有效数据内容
	private byte[] buffer;

	private class ReadCOMThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				int size;
				try {
					buffer = new byte[1];
					if (mInputStream == null)
						break;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer);
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

	private void onDataReceived(byte[] buffer) {
		Log.i("KeyBoardControl", "KeyCode=" + buffer[0]);
		if (this.mNotifyHandler != null) {
			int key_code = buffer[0];
			Message msg = this.mNotifyHandler.obtainMessage(this.mNotifyCode, key_code, 0);
			this.mNotifyHandler.sendMessage(msg);
		}
	}
}
