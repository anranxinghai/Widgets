package com.pinguo.camera360;

public class Hello {

    static {
        System.loadLibrary("hello");
    }

    public native static String getStringFromNative();
    public native static String getStringFromJNI();
}
