package edu.bloomu.volan.reticketmaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class allows the user to create and account and stores all the users information in the
 * database
 * Created by Volan on 11/13/17.
 */

public class CreateNewAccount extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account);

    }

    public void onCreateNewAccount(View view)
    {
        if(view.getId() == R.id.register_button)
        {
            EditText firstName = (EditText) findViewById(R.id.first_name);
            EditText lastName = (EditText) findViewById(R.id.last_name);
            EditText email = (EditText) findViewById(R.id.email);
            EditText username = (EditText) findViewById(R.id.username);
            EditText password = (EditText) findViewById(R.id.password);
            EditText confirm_password = (EditText) findViewById(R.id.confirm_password);
            EditText phoneNumber = (EditText) findViewById(R.id.phone);

            String firstNameString = firstName.getText().toString();
            String lastNameString = lastName.getText().toString();
            String emailString = email.getText().toString();
            String usernameString = username.getText().toString();
            String passwordString = password.getText().toString();
            String confirmPasswordString = confirm_password.getText().toString();
            String phoneNumberString = phoneNumber.getText().toString();
            Integer phoneNumberValue = Integer.valueOf(phoneNumberString);


            if(!passwordString.equals(confirmPasswordString))
            {
                Toast.makeText(CreateNewAccount.this,"Password do not match!",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                //insert information into database
                 UserInformation userInformation = new UserInformation();
                 userInformation.setFirstName(firstNameString);
                 userInformation.setLastName(lastNameString);
                 userInformation.setEmail(emailString);
                 userInformation.setUsername(usernameString);
                 userInformation.setPassword(passwordString);
                 userInformation.setPhoneNumber(phoneNumberValue);

                 databaseHelper.insertUserInformation(userInformation);
                 firstName.setText("");
                 lastName.setText("");
                 email.setText("");
                 username.setText("");
                 password.setText("");
                 confirm_password.setText("");
                 phoneNumber.setText("");
                Toast.makeText(CreateNewAccount.this,"Account has been created! Return" +
                                " to homepage to log in",
                        Toast.LENGTH_LONG).show();


            }
        }
    }
}

