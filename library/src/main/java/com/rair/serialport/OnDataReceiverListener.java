package com.rair.serialport;

/**
 * @author Rair
 * @date 2017/10/25
 * <p>
 * desc:
 */

public interface OnDataReceiverListener {

    /**
     * @param buffer 收到的字节数组
     * @param size   长度
     */
    void onDataReceiver(byte[] buffer, int size);
}
