package com.example.carlo.swagathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    private static  String TAG= "MainActivity";

    static {
        if(OpenCVLoader.initDebug()){
            Log.i(TAG , "OpenCV: Loaded Successfully");
        }else{
            Log.i(TAG , "OpenCV: ERROR");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
