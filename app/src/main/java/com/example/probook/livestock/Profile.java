package com.example.probook.livestock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    TextView tvuserfname,tvuserlocation,tvuserusername,tvuserpnumber,tvownadds;
    Button btnadd;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        tvuserfname = (TextView)findViewById(R.id.tvuserfname);
        tvuserlocation = (TextView)findViewById(R.id.tvuserlocation);
        tvuserusername = (TextView)findViewById(R.id.tvuserusername);
        tvuserpnumber = (TextView)findViewById(R.id.tvuserpnumber);
        tvownadds = (TextView)findViewById(R.id.tvownadds);

        btnadd = (Button)findViewById(R.id.btnadd);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String useruserid = sharedPreferences.getString("userid","");
        String userfname = sharedPreferences.getString("userfname","");
        String userlocation = sharedPreferences.getString("userlocation","");
        String userpnumber = sharedPreferences.getString("userpnumber","");
        String userusername = sharedPreferences.getString("userusername","");
        String useradcount = sharedPreferences.getString("useradcount","");

        //Toast.makeText(getApplicationContext(),useruserid,Toast.LENGTH_SHORT).show();

        tvuserfname.setText(userfname);
        tvuserlocation.setText(userlocation);
        tvuserusername.setText(userusername);
        tvuserpnumber.setText(userpnumber);
        tvownadds.setText(useradcount);

        toAddAdvert();

    }

    public void toAddAdvert(){
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toadvert = new Intent(Profile.this,AdvertAdd.class);
                startActivity(toadvert);
            }
        });
    }

}
