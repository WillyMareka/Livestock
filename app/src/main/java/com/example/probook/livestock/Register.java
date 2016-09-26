package com.example.probook.livestock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText etname,etlocation,etpnumber,etusername,etpass1,etpass2;
    AlertDialog.Builder builder;

    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        builder = new AlertDialog.Builder(Register.this);

        etname = (EditText) findViewById(R.id.etname);
        etlocation = (EditText) findViewById(R.id.etlocation);
        etpnumber = (EditText) findViewById(R.id.etpnumber);
        etusername = (EditText) findViewById(R.id.etusername);
        etpass1 = (EditText) findViewById(R.id.etpass1);
        etpass2 = (EditText) findViewById(R.id.etpass2);



    }


    public void userReg(View view){
        String function,name,location,phonenumber,username,password,password2;

                name = etname.getText().toString();
                location = etlocation.getText().toString();
                phonenumber = String.valueOf(etpnumber.getText().toString());;
                username = etusername.getText().toString();
                password = etpass1.getText().toString();
                password2 = etpass2.getText().toString();

        if(name.equals("") || location.equals("")|| phonenumber.equals("")|| username.equals("")|| password.equals("")|| password2.equals("")){
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
            if(password.equals(password2)){
                function = "register";
                DBTasks dbt = new DBTasks(this);
                dbt.execute(function,name,location,phonenumber,username,password);

                finish();
            }else{
                builder.setTitle("Error");
                builder.setMessage("Passwords do not match...");
                builder.setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Register.this.finish();
                            }
                        });
                builder.create();
                builder.show();
                etpass1.setText("");
                etpass2.setText("");

            }
        }



    }



}
