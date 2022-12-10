package com.example.reflex_traing_device_3_0;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.clj.fastble.data.BleDevice;

public class Utils {

    private static int[] CONNECTION_STATUS = {0,0,0,0};
    private static int devCount = 0;
    private static BleDevice bleDevice1;
    private static BleDevice bleDevice2;
    private static BleDevice bleDevice3;
    private static BleDevice bleDevice4;
    private static String bluetoothGattService = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    private static String characteristicRead   = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    private static String characteristicWrite  = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";

    public static int getDevCount() {
        return devCount;
    }
    public static void setDevCount(int devCount) {
        Utils.devCount = devCount;
    }

    public static int lookUpBLEDevice(BleDevice bleDevice) {
        if (bleDevice2 == bleDevice)
            return 1;
        if (bleDevice3 == bleDevice)
            return 2;
        if (bleDevice4 == bleDevice)
            return 3;
        return 0;
    }

    public static int getCONNECTION_STATUS(int devNo) {
        return CONNECTION_STATUS[devNo];
    }
    public static void setCONNECTION_STATUS(int devNo, int status) {
        switch (devNo) {
            case 0:
                CONNECTION_STATUS[0] = status;
                break;
            case 1:
                CONNECTION_STATUS[1] = status;
                break;
            case 2:
                CONNECTION_STATUS[3] = status;
                break;
            case 3:
                CONNECTION_STATUS[4] = status;
                break;
        }
    }

    public static BleDevice getBleDevice(int devNo) {
        switch (devNo) {
            case 1:
                return bleDevice2;
            case 2:
                return bleDevice3;
            case 3:
                return bleDevice4;
            default:
                return bleDevice1;
        }
    }
    public static void setBleDevice(BleDevice device) {
        if (CONNECTION_STATUS[0] == 0) {
            bleDevice1 = device;
            CONNECTION_STATUS[0] = 1;
        } else if (CONNECTION_STATUS[1] == 0) {
            bleDevice2 = device;
            CONNECTION_STATUS[1] = 1;
        } else if (CONNECTION_STATUS[2] == 0) {
            bleDevice3 = device;
            CONNECTION_STATUS[2] = 1;
        } else if (CONNECTION_STATUS[3] == 0) {
            bleDevice4 = device;
            CONNECTION_STATUS[3] = 1;
        }
    }

    public static String getBluetoothGattService() {
        return bluetoothGattService;
    }
    public static void setBluetoothGattService(String bluetoothGattServices) {

        bluetoothGattService = bluetoothGattServices;
    }

    public static String getCharacteristicRead() {
        return characteristicRead;
    }
    public static void setCharacteristicRead(String characteristics) {
        characteristicRead = characteristics;
    }

    public static String getCharacteristicWrite() {
        return characteristicWrite;
    }
    public static void setCharacteristicWrite(String characteristics) {
        characteristicWrite = characteristics;
    }

    public static void toast(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,0);
        toast.show();
    }

    public static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static String formatHexString(byte[] data, boolean addSpace) {
        if (data == null || data.length < 1)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
            if (addSpace)
                sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.trim();
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
