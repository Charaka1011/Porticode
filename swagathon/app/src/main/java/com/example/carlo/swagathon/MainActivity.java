package com.example.carlo.swagathon;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    boolean buttonClick = false;
    int score = 0;
    JavaCameraView myCamera;
    Mat mRgba, imgGrey, imgCanny, threshed;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    myCamera.enableView();
                    break;
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
            super.onManagerConnected(status);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myCamera = (JavaCameraView) findViewById(R.id.java_camera_view);
        myCamera.setVisibility(SurfaceView.VISIBLE);
        myCamera.setCvCameraViewListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //costum font
        final TextView tx = (TextView)findViewById(R.id.textView);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "ARCADECLASSIC.TTF");
        tx.setTypeface(custom_font);

        // Add a listener to the Capture button
        final ImageButton captureButton = (ImageButton) findViewById(R.id.imageButton);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!buttonClick){
                            buttonClick = true;
                            Mat m = new Mat();
                            Core.extractChannel(imgCanny, m, 0);
                            score = Core.countNonZero(m);
                            if(score <= 2000){
                                score = 0;
                            }
                            //captureButton.setText("RESET");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    buttonClick = false;
                                }
                            }, 200);


                        }else{
                            buttonClick = false;
                        }
                        tx.setText("" + score);
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myCamera != null) {
            myCamera.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myCamera != null) {
            myCamera.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onPause();
        if (OpenCVLoader.initDebug()) {
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        threshed = new Mat(height,width, CvType.CV_8UC1);
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGrey = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
//        Imgproc.cvtColor(mRgba, imgGrey, Imgproc.COLOR_RGB2GRAY);
//        Imgproc.Canny(imgGrey, imgCanny, 100, 150);
//        return imgCanny;
        if(buttonClick) {
            return procCameraFrame(mRgba);
        }else{
            return mRgba;
        }
    }

    public Mat procCameraFrame(Mat Rgba){
        Imgproc.cvtColor(Rgba, imgGrey, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGrey, imgCanny, 100, 150);
//        Imgproc.adaptiveThreshold(imgGrey, threshed, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 75, 5);
        return imgCanny;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}

