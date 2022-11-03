package com.example.reflex_traing_device_3_0.comm;

import com.clj.fastble.data.BleDevice;

public interface Observer {

    void disConnected(BleDevice bleDevice);
}
