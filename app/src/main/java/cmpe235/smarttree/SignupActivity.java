package cmpe235.smarttree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    //To Signup for Smart Tree application we created this class

    Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupBtn = (Button) findViewById(R.id.button_signup);
        signupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button_signup){

            //get all the parameters that user enter
            EditText email = (EditText) findViewById(R.id.email_signup);
            String emailAddress = email.getText().toString();
            EditText pass = (EditText) findViewById(R.id.password_signup);
            String password = pass.getText().toString();
            EditText name = (EditText) findViewById(R.id.name_signup);
            String username = name.getText().toString();
            EditText phone = (EditText) findViewById(R.id.phone_signup);
            String userPhone = phone.getText().toString();

            //put all the parameters into User object
            UserDetails user = new UserDetails();
            user.setEmail(emailAddress);
            user.setPassword(password);
            user.setUsername(username);
            user.setPhone(userPhone);

            //get DB connection
            DBConnector dbConnector = new DBConnector(this);
            dbConnector.insertUser(user);
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();//finish this activity and the user cannot go back to sign up page

        }
    }
}
