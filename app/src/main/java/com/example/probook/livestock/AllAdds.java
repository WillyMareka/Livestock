package com.example.probook.livestock;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;




public class AllAdds extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    TextView tvnocount;
    String count;

    ConnectionDetector cd;

    private static String url = "http://www.tusome.co.ke/livestock/alladverts.php";
    private static final String TAG = HttpHandler.class.getSimpleName();
    private static String noimage =  "http://tusome.co.ke/livestock/adverts/noimage.jpg";

    ArrayList<HashMap<String, String>> advertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_adds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        advertList = new ArrayList<>();
        tvnocount = (TextView)findViewById(R.id.tvnocount);
        lv = (ListView) findViewById(R.id.lvalladds);



        new GetAllAdds().execute();

    }


    private class GetAllAdds extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AllAdds.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);



            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray adv = jsonObj.getJSONArray("advertdetails");

                    // looping through All Contacts
                    for (int i = 0; i < adv.length(); i++) {
                        JSONObject obj = adv.getJSONObject(i);

                        String adid = obj.getString("adid");
                        String userid = obj.getString("userid");
                        String fname = obj.getString("fname");
                        String ltname = obj.getString("ltname");
                        String btname = obj.getString("btname");
                        String location = obj.getString("location");
                        String phonenumber = obj.getString("phonenumber");
                        String price = obj.getString("price");
                        String description = obj.getString("description");
                        String adimage = obj.getString("adimage");
                        String dateentered = obj.getString("dateentered");
                        count = obj.getString("count");




                        HashMap<String, String> adverts = new HashMap<>();

                        adverts.put("adid", adid);
                        adverts.put("userid", userid);
                        adverts.put("fname", fname);
                        adverts.put("ltname", ltname);
                        adverts.put("btname", btname);
                        adverts.put("location", location);
                        adverts.put("phonenumber", phonenumber);
                        adverts.put("price", price);
                        adverts.put("description", description);
                        adverts.put("dateentered", dateentered);
                        adverts.put("adimage", adimage);
                        adverts.put("count", count);

                        // adding contact to adverts list
                        advertList.add(adverts);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }




        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ((TextView)findViewById(R.id.tvnocount)).setText("Results found: " + count);

            if (pDialog.isShowing())
                pDialog.dismiss();


            ListAdapter adapter = new SimpleAdapter(

                    AllAdds.this, advertList,

                    R.layout.list_alladds, new String[]{"ltname", "btname", "fname", "location", "phonenumber", "price", "dateentered", "description","adimage"},
                    new int[]{R.id.tvltname, R.id.tvbtname, R.id.tvfname, R.id.tvlocation, R.id.tvphoneno, R.id.tvprice, R.id.tvdatesubmitted, R.id.tvdescription, R.id.tvimageurl});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent adds = new Intent(AllAdds.this, ResultChoice.class);


                    String livename = ((TextView) view.findViewById(R.id.tvltname)).getText().toString();
                    String breedname = ((TextView) view.findViewById(R.id.tvbtname)).getText().toString();
                    String firstname = ((TextView) view.findViewById(R.id.tvfname)).getText().toString();
                    String location = ((TextView) view.findViewById(R.id.tvlocation)).getText().toString();
                    String phoneno = ((TextView) view.findViewById(R.id.tvphoneno)).getText().toString();
                    String pricing = ((TextView) view.findViewById(R.id.tvprice)).getText().toString();
                    String datesub = ((TextView) view.findViewById(R.id.tvdatesubmitted)).getText().toString();
                    String description = ((TextView) view.findViewById(R.id.tvdescription)).getText().toString();
                    String image = ((TextView) view.findViewById(R.id.tvimageurl)).getText().toString();

                    //Log.e(TAG, "adimage" + image);
                    adds.putExtra("livename", livename);
                    adds.putExtra("breedname", breedname);
                    adds.putExtra("firstname", firstname);
                    adds.putExtra("location", location);
                    adds.putExtra("phoneno", phoneno);
                    adds.putExtra("pricing", pricing);
                    adds.putExtra("datesubmitted", datesub);
                    adds.putExtra("description", description);
                    adds.putExtra("image", image);

                    adds.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(adds);


                }
            });
        }

    }



}
