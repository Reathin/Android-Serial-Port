package com.rair.serialport;

public class ByteUtil {

    public static byte[] hexStringToByteArray(String s) {
        if (s.length() < 2) {
            s = "0" + s;
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String hexBytesToString(byte[] hexBytes) {
        char[] hexChars = new char[hexBytes.length * 2];
        for (int j = 0; j < hexBytes.length; j++) {
            int v = hexBytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
