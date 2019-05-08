package com.pinguo.camera360;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class JNIActivity extends AppCompatActivity {

    private TextView jni_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        jni_string.setText(Hello.getStringFromNative() + "/n" + Hello.getStringFromJNI());
    }

}
