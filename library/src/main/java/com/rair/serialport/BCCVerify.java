package com.rair.serialport;

/**
 * @author Rair
 * @date 2018/1/5
 * <p>
 * desc:BCC校验
 */
public class BCCVerify {

    /**
     * 计算BCC
     *
     * @param data 数据报文
     */
    private static String bccVal(byte[] data) {
        String ret = "";
        byte[] BCC = new byte[1];
        for (int i = 0; i < data.length; i++) {
            BCC[0] = (byte) (BCC[0] ^ data[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }

    /**
     * 计算BCC并转为byte数组
     *
     * @param data 数据报文
     */
    public static byte[] calcBccBytes(byte[] data) {
        String bccVal = bccVal(data);
        return ByteUtil.hexStringToByteArray(bccVal);
    }
}