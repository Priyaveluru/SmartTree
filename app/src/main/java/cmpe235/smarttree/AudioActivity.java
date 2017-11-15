package cmpe235.smarttree;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;

public class AudioActivity extends AppCompatActivity {

    Button playBtn,stopBtn,recordBtn;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private static String TAG = "AudioActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        playBtn = (Button) findViewById(R.id.button_play);
        stopBtn = (Button) findViewById(R.id.button_stop);
        recordBtn = (Button) findViewById(R.id.button_record);
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/record_audio_sample" +
                ".3gp";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(AudioActivity.this,"Permission is granted",Toast.LENGTH_LONG).show();

            }
            else{
                requestPermission();
            }
        }

    }
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(AudioActivity.this, RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);
    }
    public void onRequestPermissionsResult(int requestCode,String permission[], int grantResults[]){
        switch(requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length>0){
                    boolean cameraAccepted =grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(AudioActivity.this,"Permission granted",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(AudioActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both  permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},REQUEST_RECORD_AUDIO_PERMISSION);
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
    public void displayAlertMessage(String message , DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(AudioActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    public void recordAudio(View view) {

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/record_audio_sample" +
                ".3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recordBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Recording audio started", Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Public function handle stop audio
     *
     */
    public void onClick(View v) {

        if (v.getId() == R.id.button_stop) {

            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Public function handle play audio
     */
    public void playAudio(View view) {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/record_audio_sample" +
                ".3gp";
        int YOUR_REQUEST_CODE = 200; // could be something else..
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //check if permission request is necessary
        {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, YOUR_REQUEST_CODE);
        }
        MediaPlayer m = new MediaPlayer();

        try {
            m.setDataSource(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            m.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.start();
        Toast.makeText(getApplicationContext(), "Playing my audio", Toast.LENGTH_LONG).show();
    }

    /**
     * Public function handle share audio
     * @param view
     */
    public void shareAudio(View view) {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/record_audio_sample" +
                ".3gp";
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(outputFile));
        try {
            startActivity(Intent.createChooser(share, "Share audio"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


}
