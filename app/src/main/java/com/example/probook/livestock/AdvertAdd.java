package com.example.probook.livestock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdvertAdd extends AppCompatActivity {

    Button btnlivecancel;
    private ProgressDialog pDialog;
    private ListView lv;

    ConnectionDetector cd;

    private static String liveurl = "http://www.tusome.co.ke/livestock/alllivestock.php";

    ArrayList<HashMap<String, String>> livestocklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnlivecancel = (Button)findViewById(R.id.btnlivecancel);

        livestocklist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvaddlive);

        new GetAllLivestock().execute();

        CancelAdd();
    }


   public void CancelAdd(){
       btnlivecancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent cancelintent = new Intent(getApplicationContext(),MainActivity.class);
               startActivity(cancelintent);
           }
       });
   }

    private class GetAllLivestock extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AdvertAdd.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(liveurl);

            //Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray livestock = jsonObj.getJSONArray("livestockdetails");

                    // looping through All Contacts
                    for (int i = 0; i < livestock.length(); i++) {
                        JSONObject obj = livestock.getJSONObject(i);

                        int ltno = i+1;
                        String ltid = obj.getString("ltid");
                        String ltname = obj.getString("ltname");


                        HashMap<String, String> live = new HashMap<>();


                        live.put("ltno", String.valueOf(ltno));
                        live.put("ltid", ltid);
                        live.put("ltname", ltname);

                        // adding contact to adverts list
                        livestocklist.add(live);
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

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(

                    AdvertAdd.this, livestocklist,

                    R.layout.list_criteria, new String[]{"ltno", "ltid", "ltname"},
                    new int[]{R.id.tvposition, R.id.tvid, R.id.tvname});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent lives = new Intent(AdvertAdd.this, BreedAdd.class);

                    String ltid = ((TextView) view.findViewById(R.id.tvid)).getText().toString();
                    String ltname = ((TextView) view.findViewById(R.id.tvname)).getText().toString();

                    lives.putExtra("ltid", ltid);
                    lives.putExtra("ltname", ltname);

                    lives.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(lives);


                }
            });
        }

    }

}
