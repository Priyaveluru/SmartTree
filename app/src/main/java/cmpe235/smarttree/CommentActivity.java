package cmpe235.smarttree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    //This class handles the comment functionality
    private RatingBar ratingBar;
    private Button submit;
    private  DBConnector dbConnector= new DBConnector(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //set up the rating bar
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //set up a listener to listen rating bar changing
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        //set up the submit button
        submit = (Button) findViewById(R.id.submit);
        //set up the listener on submit button
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.submit){

            EditText comment = (EditText) findViewById(R.id.comment);
            String feedback = comment.getText().toString();
            String ranking = String.valueOf(ratingBar.getRating());
            String username = getIntent().getStringExtra("username");

            CommentDetails comment1 = new CommentDetails();
            comment1.setComment(feedback);
            comment1.setRanking(ranking);
            comment1.setUsername(username);

            dbConnector.insertComment(comment1);

            Intent intent = new Intent(CommentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
