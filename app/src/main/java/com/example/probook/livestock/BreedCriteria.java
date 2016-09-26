package com.example.probook.livestock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class BreedCriteria extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    String ltid,ltname,btname,btid;
    TextView tvltname;
    Button btnlocation;

    ConnectionDetector cd;

    //private static final String TAG = BreedCriteria.class.getSimpleName();

    private static String breedurl = "http://www.tusome.co.ke/livestock/allbreeds.php";

    ArrayList<HashMap<String, String>> breedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_criteria);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        breedlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvbtype);
        tvltname = (TextView)findViewById(R.id.tvltname);

        Bundle bundle = getIntent().getExtras();
        ltid = bundle.getString("ltid");
        ltname = bundle.getString("ltname");
        btnlocation = (Button) findViewById(R.id.btnlocation);

        tvltname.setText(ltname);

        new GetAllBreed().execute();

        getLocation();
    }

    public void getLocation(){
        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent breed = new Intent(BreedCriteria.this, LocationCriteria.class);

                btid = "";
                btname = "";

                breed.putExtra("ltid", ltid);
                breed.putExtra("ltname", ltname);
                breed.putExtra("btid", btid);
                breed.putExtra("btname", btname);

                breed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(breed);
            }
        });
    }

    private class GetAllBreed extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BreedCriteria.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String liveid = ltid;

            try {
                URL url = new URL(breedurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("ltid","UTF-8") +"="+ URLEncoder.encode(liveid,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                OS.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));

                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();

                JSONObject resultJSON = null;
                try {
                    resultJSON = new JSONObject(result);
                    JSONArray breeds = resultJSON.getJSONArray("breeddetails");


                    for (int i = 0; i < breeds.length(); i++) {
                        JSONObject obj = breeds.getJSONObject(i);

                        int btno = i+1;
                        String btid = obj.getString("btid");
                        String btname = obj.getString("btname");
                        String ltid = obj.getString("ltid");


                        HashMap<String, String> breed = new HashMap<>();


                        breed.put("btno", String.valueOf(btno));
                        breed.put("btid", btid);
                        breed.put("btname", btname);
                        breed.put("ltid", ltid);

                        breedlist.add(breed);
                        //Log.e(TAG, "Response from url: " + breedlist);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    BreedCriteria.this, breedlist,
                    R.layout.list_criteria, new String[]{"btno", "ltid", "btid", "btname"},
                    new int[]{R.id.tvposition, R.id.tvid, R.id.tvid2, R.id.tvname});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent breed = new Intent(BreedCriteria.this, LocationCriteria.class);

                    String btid = ((TextView) view.findViewById(R.id.tvid2)).getText().toString();
                    String ltid = ((TextView) view.findViewById(R.id.tvid)).getText().toString();
                    String btname = ((TextView) view.findViewById(R.id.tvname)).getText().toString();
                    String livename = tvltname.getText().toString();

                    breed.putExtra("ltid", ltid);
                    breed.putExtra("btid", btid);
                    breed.putExtra("ltname", livename);
                    breed.putExtra("btname", btname);

                    breed.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(breed);


                }
            });
        }

    }
}
