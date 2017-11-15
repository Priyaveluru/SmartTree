package cmpe235.smarttree;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
 * Created by priya on 10/25/2017.
 */

public class PhotoActivity extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private Uri fileUri;
    private static final int REQUEST_CAMERA=1;
    private Button btnCapturePicture;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);

        //Set share button
        Button share_btn=(Button)findViewById(R.id.share);

        //Set share button click listener
        share_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareImage();
            }
        });

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
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
    public void onRequestPermissionsResult(int requestCode,String permission[], int grantResults[]){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length>0){
                    boolean cameraAccepted =grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(PhotoActivity.this,"Permission granted",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(PhotoActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
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
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "picture.jpg"));

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public void displayAlertMessage(String message , DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(PhotoActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    public void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpg");

        //Set a location on user extra storage SD card, file name "picture.jpg" which use for save photo_share.
        Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "picture.jpg"));
        //Set get photo_share file Uri
        share.putExtra(Intent.EXTRA_STREAM, fileUri);
        //Show a dialog to show feedback
        startActivity(Intent.createChooser(share, "Share Image!"));
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




    /* * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
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
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


}


