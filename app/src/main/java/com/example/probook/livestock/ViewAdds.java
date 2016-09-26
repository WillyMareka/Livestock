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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewAdds extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView lv;
    TextView tvownresults;
    String useruserid,count;
    Button btnadd;

    ConnectionDetector cd;

    private static final String TAG = SearchResults.class.getSimpleName();

    private static String ownaddurl = "http://www.tusome.co.ke/livestock/ownadds.php";

    ArrayList<HashMap<String, String>> ownlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_adds);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(this);
        if(cd.isConnected()){
        }else{
            Toast.makeText(getApplicationContext(),"The application requires internet connection...",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Please turn on for all functionalities",Toast.LENGTH_SHORT).show();
        }

        ownlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.lvownlist);
        btnadd = (Button)findViewById(R.id.btnadd);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        useruserid = sharedPreferences.getString("userid","");

        new GetOwnLivestock().execute();
        //Toast.makeText(getApplicationContext(),""+useruserid,Toast.LENGTH_SHORT).show();
        AddAdvert();

    }

    public void AddAdvert(){
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toliveintent = new Intent(getApplicationContext(),AdvertAdd.class);
                startActivity(toliveintent);
            }
        });
    }


    private class GetOwnLivestock extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewAdds.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                URL url = new URL(ownaddurl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("userid","UTF-8") +"="+ URLEncoder.encode(useruserid,"UTF-8");

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
                    JSONArray breeds = resultJSON.getJSONArray("ownadddetails");

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


                        ownlist.add(search);
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
            //Log.e(TAG, "Results: " + ownlist);

            ((TextView)findViewById(R.id.tvownresults)).setText("Results found: " + count);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(
                    ViewAdds.this, ownlist,
                    R.layout.list_alladds, new String[]{"ltname", "btname", "fname", "location", "phonenumber", "price", "dateentered", "description", "adimage"},
                    new int[]{ R.id.tvltname, R.id.tvbtname, R.id.tvfname, R.id.tvlocation, R.id.tvphoneno, R.id.tvprice, R.id.tvdatesubmitted, R.id.tvdescription, R.id.tvimageurl});


            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent adds = new Intent(ViewAdds.this, ResultChoice.class);

                    String livename = ((TextView) view.findViewById(R.id.tvltname)).getText().toString();
                    String breedname = ((TextView) view.findViewById(R.id.tvbtname)).getText().toString();
                    String firstname = ((TextView) view.findViewById(R.id.tvfname)).getText().toString();
                    String location = ((TextView) view.findViewById(R.id.tvlocation)).getText().toString();
                    String phoneno = ((TextView) view.findViewById(R.id.tvphoneno)).getText().toString();
                    String pricing = ((TextView) view.findViewById(R.id.tvprice)).getText().toString();
                    String datesub = ((TextView) view.findViewById(R.id.tvdatesubmitted)).getText().toString();
                    String description = ((TextView) view.findViewById(R.id.tvdescription)).getText().toString();
                    String image = ((TextView) view.findViewById(R.id.tvimageurl)).getText().toString();

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
