package cmpe235.smarttree;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.CAMERA;


/**
 * Created by priya on 9/18/2017.
 */

public class CameraActivity  extends Activity{
    //The photo_share/Video Capturing functionality and also sharing the captured resources functionality is implemented in this class

    // Activity request codes
    private boolean permissionToRecordAccepted = false;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int REQUEST_CAMERA=1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri; // file url to store image/videoshare


    private Button btnRecordVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(CameraActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }
        //Set share button
        Button share_btnvideo=(Button)findViewById(R.id.sharevideo);
        //Set share button click listener
        share_btnvideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareVideo();
            }
        });
        /**

         /**
         * Record videoshare button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record videoshare
                new recordVideo().execute("");
            }
        });

       // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(CameraActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }
    public void onRequestPermissionsResult(int requestCode,String permission[], int grantResults[]){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length>0){
                    boolean cameraAccepted =grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(CameraActivity.this,"Permission granted",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(CameraActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both  permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }

    }


    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public void displayAlertMessage(String message , DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(CameraActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Recording videoshare
     */
    private class recordVideo extends AsyncTask<String, String, String> {

        @Override
        protected String  doInBackground(String... params) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                File fileUri = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/videoshare.mp4");

                Uri videoUri = Uri.fromFile(fileUri);

                // set videoshare quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri); // set the image file
                // name

                // start the videoshare capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
                return null;
        }
    }


    public void shareVideo() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("videoshare/mp4");
        share.putExtra(Intent.EXTRA_SUBJECT, "Video");
        //Set a location on user extra storage SD card, file name "videoshare.mp4" which use for save videoshare.
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/videoshare.mp4");
        Uri video = Uri.fromFile(mediaFile);
        //Set get photo_share file Uri
        share.putExtra(Intent.EXTRA_STREAM, video);
        //Show a dialog to show feedback
        startActivity(Intent.createChooser(share, "Share Video!"));
    }

    /* * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

       if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // videoshare successfully recorded


            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled videoshare recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record videoshare
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record videoshare", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }





    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/videoshare
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / videoshare
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


}
