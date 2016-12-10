package com.example.carlo.swagathon;

import android.app.Activity;
import android.graphics.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by Kumar on 10/12/2016.
 */

public class CameraActivity extends Activity {

    private Camera myCamera;
    private MainActivity mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create an instance of Camera
        myCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new MainActivity(this, myCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }


}
