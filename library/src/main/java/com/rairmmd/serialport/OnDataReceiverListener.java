package com.rairmmd.serialport;

/**
 * @author Rair
 * @date 2017/10/25
 * <p>
 * desc:数据接收回调
 */

public interface OnDataReceiverListener {

    /**
     * 接收数据
     *
     * @param buffer 收到的字节数组
     * @param size   长度
     */
    void onDataReceiver(byte[] buffer, int size);
}
