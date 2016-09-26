package com.example.probook.livestock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ImageAdd extends AppCompatActivity {

    Button btncanceladd,btnback,btntakepic,btnsubmit;
    String ltname,btname,ltid,btid,price,description;
    private static final String TAG = ImageAdd.class.getSimpleName();

    public static final String UPLOAD_URL = "http://www.tusome.co.ke/livestock/adverts/addadvert.php";

    private int PICK_IMAGE_REQUEST = 1;

    private ProgressDialog pDialog;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btncanceladd = (Button)findViewById(R.id.btncanceladd);
        btnback = (Button)findViewById(R.id.btnback);
        btntakepic = (Button)findViewById(R.id.btntakepic);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);

        imageView = (ImageView)findViewById(R.id.ivadvertphoto);

        Bundle bundle = getIntent().getExtras();
        ltname = bundle.getString("ltname");
        btname = bundle.getString("btname");
        ltid = bundle.getString("ltid");
        btid = bundle.getString("btid");
        price = bundle.getString("price");
        description = bundle.getString("description");


        CancelAdd();
        PriceDescription();
        showFileChooser();
        uploadImage();
    }


    public void CancelAdd(){
        btncanceladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tobreed = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(tobreed);
            }
        });
    }

    public void PriceDescription(){
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topd = new Intent(getApplicationContext(), PriceDescription.class);

                topd.putExtra("ltid", ltid);
                topd.putExtra("btid", btid);
                topd.putExtra("ltname", ltname);
                topd.putExtra("btname", btname);

                //Toast.makeText(getApplicationContext(),"ltname "+ltname,Toast.LENGTH_SHORT).show();
                topd.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(topd);
            }
        });
    }



    private void showFileChooser() {
        btntakepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                class UploadImage extends AsyncTask<Bitmap,Void,String>{



                    RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog = new ProgressDialog(ImageAdd.this);
                pDialog.setMessage("Uploading...");
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String useruserid = sharedPreferences.getString("userid","");
                String userphonenumber = sharedPreferences.getString("userpnumber","");
                String userlocation = sharedPreferences.getString("userlocation","");
                String livestockid = ltid;
                String breedid = btid;
                String newprice = price;
                String newdescription = description;

                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();


                data.put("userid", useruserid);
                data.put("userpnumber", userphonenumber);
                data.put("ltid", livestockid);
                data.put("btid", breedid);
                data.put("location", userlocation);
                data.put("price", newprice);
                data.put("description", newdescription);
                  data.put("image", uploadImage);


                String result = rh.sendPostRequest(UPLOAD_URL,data);

    Log.e(TAG, "Result: " + result);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
            }
        });
    }



}
