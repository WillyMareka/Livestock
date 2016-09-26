package com.example.probook.livestock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText usernameText;
    EditText passwordText;
    TextView tvregister;

    AlertDialog.Builder builder;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        builder = new AlertDialog.Builder(Login.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        tvregister = (TextView)findViewById(R.id.tvregister);

        usernameText = (EditText) findViewById(R.id.etusername);
        passwordText = (EditText) findViewById(R.id.etpass);

        Register();
    }

    public void userLogin(View view)
    {
        String function = "login";

        String username,password;

        username = usernameText.getText().toString();
        password = passwordText.getText().toString();



        if(username.equals("") || password.equals("")) {
            builder.setTitle("Error");
            builder.setMessage("Please enter all fields");
            builder.setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            // Register.this.finish();
                        }
                    });
            builder.create();
            builder.show();

        }else{


            DBTasks dbt = new DBTasks(this);
            dbt.execute(function,username, password);
        }





    }


    public void Register(){
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toregister = new Intent(Login.this, Register.class);
                startActivity(toregister);
            }
        });
    }

}
