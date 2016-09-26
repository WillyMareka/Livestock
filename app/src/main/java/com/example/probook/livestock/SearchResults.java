package com.example.probook.livestock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResults extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    String ltid,btid,location;
    TextView tvnoadvert;
    String count = "";

    ConnectionDetector cd;

    private static final String TAG = SearchResults.class.getSimpleName();



    private static String searchurl = "http://www.tusome.co.ke/livestock/advertsearch.php";

    ArrayList<HashMap<String, String>> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        tvnoadvert = (TextView) findViewById(R.id.tvnoadvert);

        searchList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvsearch);

        Bundle bundle = getIntent().getExtras();
        ltid = bundle.getString("ltid");
        btid = bundle.getString("btid");
        location = bundle.getString("location");

        tvnoadvert.setText(count);

        new GetAllCriteria().execute();
    }

    private class GetAllCriteria extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SearchResults.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                URL url = new URL(searchurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("ltid","UTF-8") +"="+ URLEncoder.encode(ltid,"UTF-8")+"&"+
                        URLEncoder.encode("btid","UTF-8") +"="+ URLEncoder.encode(btid,"UTF-8")+"&"+
                        URLEncoder.encode("location","UTF-8") +"="+ URLEncoder.encode(location,"UTF-8");

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
                    JSONArray breeds = resultJSON.getJSONArray("adsearchdetails");

                    for (int i = 0; i < breeds.length(); i++) {
                        JSONObject obj = breeds.getJSONObject(i);

                        String adid = obj.getString("adid");
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


                        HashMap<String, String> search = new HashMap<>();

                        //breed.put("btno", String.valueOf(btno));
                        search.put("adid", adid);
                        search.put("fname", fname);
                        search.put("ltname", ltname);
                        search.put("btname", btname);
                        search.put("location", location);
                        search.put("phonenumber", phonenumber);
                        search.put("price", price);
                        search.put("description", description);
                        search.put("adimage", adimage);
                        search.put("dateentered", dateentered);
                        search.put("count", count);


                        searchList.add(search);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.e(TAG, "Results: " + searchList);

            if (count.equals("")||count.equals(null)){
                ((TextView)findViewById(R.id.tvnoadvert)).setText("Results found: 0");
            }else{
                ((TextView)findViewById(R.id.tvnoadvert)).setText("Results found: " + count);
            }



            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    SearchResults.this, searchList,
                    R.layout.list_alladds, new String[]{"ltname", "btname", "fname", "location", "phonenumber", "price", "dateentered", "description"},
                    new int[]{ R.id.tvltname, R.id.tvbtname, R.id.tvfname, R.id.tvlocation, R.id.tvphoneno, R.id.tvprice, R.id.tvdatesubmitted, R.id.tvdescription});


            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent adds = new Intent(SearchResults.this, ResultChoice.class);

                    String livename = ((TextView) view.findViewById(R.id.tvltname)).getText().toString();
                    String breedname = ((TextView) view.findViewById(R.id.tvbtname)).getText().toString();
                    String firstname = ((TextView) view.findViewById(R.id.tvfname)).getText().toString();
                    String location = ((TextView) view.findViewById(R.id.tvlocation)).getText().toString();
                    String phoneno = ((TextView) view.findViewById(R.id.tvphoneno)).getText().toString();
                    String pricing = ((TextView) view.findViewById(R.id.tvprice)).getText().toString();
                    String datesub = ((TextView) view.findViewById(R.id.tvdatesubmitted)).getText().toString();
                    String description = ((TextView) view.findViewById(R.id.tvdescription)).getText().toString();

                    adds.putExtra("livename", livename);
                    adds.putExtra("breedname", breedname);
                    adds.putExtra("firstname", firstname);
                    adds.putExtra("location", location);
                    adds.putExtra("phoneno", phoneno);
                    adds.putExtra("pricing", pricing);
                    adds.putExtra("datesubmitted", datesub);
                    adds.putExtra("description", description);

                    adds.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(adds);


                }
            });
        }

    }
}
