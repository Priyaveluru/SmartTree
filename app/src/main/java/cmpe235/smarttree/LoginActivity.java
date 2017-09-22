package cmpe235.smarttree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Login functionality is implemented in this class

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
    }
    public void gotoSignup(View view){
          //If the user clicks on signup button it will be redirected to signup page
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn){

            //get all the parameters that user enter
            EditText emailAddress = (EditText) findViewById(R.id.email_login);
            String email = emailAddress.getText().toString();
            EditText pass = (EditText) findViewById(R.id.password_login);
            String password = pass.getText().toString();

            DBConnector dbConnector = new DBConnector(this);

            String password1 = dbConnector.login(email);
             //Comparing Database password with entered password
            if(password.equals(password1)){

                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                String username = dbConnector.getUsername(password);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();
            }

        }
    }
}
