package com.example.reflex_traing_device_3_0;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.clj.fastble.data.BleDevice;

public class Utils {

    private static int CONNECTION_STATUS = 0;
    private static BleDevice bleDevice;
    private static String bluetoothGattService = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    private static String characteristicRead = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    private static String characteristicWrite = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";

    public static int getCONNECTION_STATUS() { return CONNECTION_STATUS; }
    public static void setCONNECTION_STATUS(int connection_status) { CONNECTION_STATUS = connection_status; }

    public static BleDevice getBleDevice() {
        return bleDevice;
    }
    public static void setBleDevice(BleDevice device) { bleDevice = device; }


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

    public static byte[] intToByteArray(int a)
    {
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


}
