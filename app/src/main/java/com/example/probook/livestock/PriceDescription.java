package com.example.probook.livestock;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PriceDescription extends AppCompatActivity {

    Button btncanceladd,btnbreed,btnnext;
    TextView tvltname,tvbtname;
    EditText etprice,etdescription;
    String ltname,btname,ltid,btid;
    AlertDialog.Builder builder;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_description);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder = new AlertDialog.Builder(PriceDescription.this);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        btncanceladd = (Button)findViewById(R.id.btncanceladd);
        btnbreed = (Button)findViewById(R.id.btnbreed);
        btnnext = (Button)findViewById(R.id.btnnext);

        tvltname = (TextView)findViewById(R.id.tvltname);
        tvbtname = (TextView)findViewById(R.id.tvbtname);

        etprice = (EditText)findViewById(R.id.etprice);
        etdescription = (EditText)findViewById(R.id.etdescription);

        Bundle bundle = getIntent().getExtras();

        btname = bundle.getString("btname");
        ltid = bundle.getString("ltid");
        btid = bundle.getString("btid");
        ltname = bundle.getString("ltname");

        tvltname.setText(ltname);
        if(ltname.equals("") && btname.equals("")){
            tvbtname.setText("");
        }else {
            tvbtname.setText(" > " + btname);
        }

        BreedAdd();
        CancelAdd();
        NextSection();

    }

    public void NextSection(){
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pricing,describing;

                pricing = etprice.getText().toString();
                describing = etdescription.getText().toString();

                if(pricing.equals("") || describing.equals("")){
                    builder.setTitle("Error");
                    builder.setMessage("Please enter both price and description");
                    builder.setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                }
                            });
                    builder.create();
                    builder.show();
                }else {
                    Intent toimage = new Intent(getApplicationContext(), ImageAdd.class);

                    String price = etprice.getText().toString();
                    String description = etdescription.getText().toString();

                    toimage.putExtra("ltid", ltid);
                    toimage.putExtra("btid", btid);
                    toimage.putExtra("ltname", ltname);
                    toimage.putExtra("btname", btname);
                    toimage.putExtra("price", price);
                    toimage.putExtra("description", description);

                    toimage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(toimage);
                }
            }
        });
    }

    public void BreedAdd(){
        btnbreed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tobreed = new Intent(getApplicationContext(), BreedAdd.class);

                tobreed.putExtra("ltid", ltid);
                tobreed.putExtra("ltname", ltname);

                tobreed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tobreed);
            }
        });
    }

    public void CancelAdd(){
        btncanceladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelintent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(cancelintent);
            }
        });
    }







}
