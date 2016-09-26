package com.example.probook.livestock;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnlogin, btnbrowse;
    TextView tvregister;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.search));
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AllAdds.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String useruserid = sharedPreferences.getString("userid","");

        //Toast.makeText(getApplicationContext(),useruserid,Toast.LENGTH_SHORT).show();

        tvregister = (TextView) findViewById(R.id.tvregister);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnbrowse = (Button) findViewById(R.id.btnbrowse);

        Browse();
        Login();
        Register();


    }


    public void Browse(){
       btnbrowse.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent browseintent = new Intent("com.example.probook.livestock.LivestockCriteria");
             startActivity(browseintent);
         }
       });
    }

    public void Login(){
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginintent = new Intent("com.example.probook.livestock.Login");
                startActivity(loginintent);
            }
        });
    }

    public void Register(){
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerintent = new Intent("com.example.probook.livestock.Register");
                startActivity(registerintent);
            }
        });
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent profileintent = new Intent("com.example.probook.livestock.Profile");
            startActivity(profileintent);
        }else if(id == R.id.action_viewadds){
            Intent addsintent = new Intent(getApplicationContext(),ViewAdds.class);
            startActivity(addsintent);
        }else if(id == R.id.action_logout){
            String function = "logout";

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String useruserid = sharedPreferences.getString("userid","");

            DBTasks dbt = new DBTasks(this);
            dbt.execute(function,useruserid);
        }else if(id == R.id.action_noprofile){
            Intent loginintent = new Intent("com.example.probook.livestock.Login");
            startActivity(loginintent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String useruserid = sharedPreferences.getString("userid","");

        if(useruserid.equals("")){
            menu.findItem(R.id.action_profile).setVisible(false);
            menu.findItem(R.id.action_viewadds).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
            menu.findItem(R.id.action_noprofile).setVisible(true);
        }else{
            menu.findItem(R.id.action_profile).setVisible(true);
            menu.findItem(R.id.action_viewadds).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(true);
            menu.findItem(R.id.action_noprofile).setVisible(false);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String useruserid = sharedPreferences.getString("userid","");

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeintent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(homeintent);
        } else if (id == R.id.nav_browse) {
            Intent browseintent = new Intent(getApplicationContext(),LivestockCriteria.class);
            startActivity(browseintent);
        } else if (id == R.id.nav_add) {

            if(useruserid.equals("")||useruserid.equals(null)) {
                Intent loginintent = new Intent(getApplicationContext(), Login.class);
                Toast.makeText(getApplicationContext(),"Please log in first",Toast.LENGTH_LONG).show();
                startActivity(loginintent);
            }else{
                Intent addintent = new Intent(getApplicationContext(), AdvertAdd.class);
                startActivity(addintent);
            }
        } else if (id == R.id.nav_exit) {
            finish();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
