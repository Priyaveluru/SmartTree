package cmpe235.smarttree;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ReviewComments extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button review;
    private DBConnector dbConnector = new DBConnector(this);
    String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //set up the submit button
        //set up the listener on submit button

    TextView t2 = (TextView) findViewById(R.id.textView2);
    List<String> feedback = dbConnector.getAllComments();
        t2.setText("");
        if (feedback != null && !feedback.isEmpty()) {
            int i = 0;
            while (i < feedback.size()) {
                t2.append( "  -"+feedback.get(i) + "\n\n");
                //System.out.println(feedback.get(i));
                i++;
            }
        }


}


}
