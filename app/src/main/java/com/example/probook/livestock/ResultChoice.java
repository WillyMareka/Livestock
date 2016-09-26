package com.example.probook.livestock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ResultChoice extends AppCompatActivity {

    TextView tvchoicename,tvlivebreed,tvlocation,tvphoneno,tvprice,tvchoicedesc;
    private NetworkImageView imageView;
    private ImageLoader imageLoader;

    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_choice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        imageView = (NetworkImageView) findViewById(R.id.imageView);

        tvchoicename = (TextView) findViewById(R.id.tvchoicename);
        tvlivebreed = (TextView) findViewById(R.id.tvlivebreed);
        tvlocation = (TextView) findViewById(R.id.tvlocation);
        tvphoneno = (TextView) findViewById(R.id.tvphoneno);
        tvprice = (TextView) findViewById(R.id.tvprice);
        tvchoicedesc = (TextView) findViewById(R.id.tvchoicedesc);

        Bundle bundle = getIntent().getExtras();
            String livename = bundle.getString("livename");
            String breedname = bundle.getString("breedname");
            String firstname = bundle.getString("firstname");
            String location = bundle.getString("location");
            String phoneno = bundle.getString("phoneno");
            String pricing = bundle.getString("pricing");
            String datesubmitted = bundle.getString("datesubmitted");
            String description = bundle.getString("description");
            String image = bundle.getString("image");

        //Toast.makeText(getApplicationContext(),""+image,Toast.LENGTH_LONG).show();

        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();
        imageLoader.get(image, ImageLoader.getImageListener(imageView,
                R.drawable.loading, R.drawable.noimagefound));
        imageView.setImageUrl(image, imageLoader);

        tvchoicename.setText("By " + firstname + " on " + datesubmitted);
        tvlivebreed.setText(livename + " - " + breedname);
        tvlocation.setText(location);
        tvphoneno.setText(phoneno);
        tvprice.setText("Kshs. " + pricing+" /= ");
        tvchoicedesc.setText(description);
    }


}
