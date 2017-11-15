package cmpe235.smarttree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by priya on 9/19/2017.
 */

public class MainActivity extends AppCompatActivity {
    //This class contains a Navigation bar for 6 different functionalities

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Nearby = (Button) findViewById(R.id.Nearby);
        Button About=(Button) findViewById(R.id.About);
        Button Comment=(Button) findViewById(R.id.Comment);
        Button photo_video=(Button)findViewById(R.id.photo_video);
        Button Interact=(Button)findViewById(R.id.Interact);
        Button Audio=(Button)findViewById(R.id.Audio);
        Button Photo=(Button)findViewById(R.id.Photo);
        //On clicking Nearby button it will be redirected to MapsActivity class
        Nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);

            }
        });
        //On clicking About button it will be redirected to AboutActivity class
        About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InteractActivity.class);
                startActivity(i);
            }
        });
        //On clicking Interact button it will be redirected to InteractActivity class
        Interact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });
        //On Clicking Comment button it will be redirected to CommentActivity class
        Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CommentActivity.class);
                startActivity(i);
            }
        });
        //On Clicking photo_video button it will be redirected to CameraActivity class
        photo_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(i);
            }
        });
        Audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AudioActivity.class);
                startActivity(i);
            }
        });
        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PhotoActivity.class);
                startActivity(i);
            }
        });



    }
    }


