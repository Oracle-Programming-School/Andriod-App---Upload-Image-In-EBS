package com.example.sapphire;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {


    protected void showPopup(String pMessage) {
        Toast.makeText(getApplicationContext(), pMessage, Toast.LENGTH_LONG).show();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Handle Button Code
        Button l_MainLoginButton = findViewById(R.id.MainLoginButton);
        l_MainLoginButton.setOnClickListener(new View.OnClickListener() {
           // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                EditText l_MainUserName = findViewById(R.id.editusername);




                if (l_MainUserName.getText().toString().length() == 0) {
                    l_MainUserName.setError("User name is required!");
                    showPopup("User name is required!");
                    return;
                }

                EditText l_MainPassword = findViewById(R.id.editpassword);
                if (l_MainPassword.getText().toString().length() == 0) {
                    l_MainPassword.setError("Password is required!");
                    showPopup("Password is required!");
                    return;
                }


                ConnectionHelper ch = new ConnectionHelper();

                if (!ch.isUserAuthenticated(l_MainUserName.getText().toString(), l_MainPassword.getText().toString())) {
                    showPopup("Invalid username/Password.");
                    return;
                }


                startActivity(new Intent(MainActivity.this, SecondActivity.class));


            }
        });




    }

}

