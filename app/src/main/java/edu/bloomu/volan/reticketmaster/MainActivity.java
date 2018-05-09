package edu.bloomu.volan.reticketmaster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class allows the user to move to the create account activity and log in to the applicatioin.
 *
 */
public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "font.otf");
        TextView textView = findViewById(R.id.appName);
        textView.setTypeface(myTypeFace);
    }

    /**
     * This method allows the user to move to the create account activity
     * @param view
     */
    public void create_NewAccount(View view)
    {

        Intent intent = new Intent(getApplicationContext(), CreateNewAccount.class);
        startActivity(intent);
    }

    /**
     * This method allows the user to login to the application
     * @param view
     */
    public void login(View view)
    {
        EditText username = (EditText) findViewById(R.id.username);
        String usernameString = username.getText().toString();
        EditText password = (EditText) findViewById(R.id.password);
        String passwordString = username.getText().toString();

        String databasePassword = databaseHelper.searchPassword(usernameString);

        if(passwordString.equals(databasePassword))
        {
            Intent intentLoggedIn = new Intent(getApplicationContext(),LoggedIn.class);
            intentLoggedIn.putExtra("Username",usernameString);
            username.setText("");
            password.setText("");
            startActivity(intentLoggedIn);
        }
        else
        {
            Toast.makeText(MainActivity.this,"Username & Password do not match!",
                    Toast.LENGTH_SHORT).show();
        }
    }



}
