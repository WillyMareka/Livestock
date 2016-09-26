package com.example.probook.livestock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

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
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ProBook on 8/16/2016.
 */
public class  DBTasks extends AsyncTask<String,Void,String> {

    Context context;

    DBTasks(Context ctx){
        context = ctx;
    }



    @Override
    protected String doInBackground(String... params) {
        String register_url = "http://www.tusome.co.ke/livestock/register.php";
        String login_url = "http://www.tusome.co.ke/livestock/login.php";
        String logout_url = "http://www.tusome.co.ke/livestock/logout.php";
        String function = params[0];

        if(function.equals("register")){

            String fname = params[1];
            String location = params[2];
            String phonenumber = params[3];
            String username = params[4];
            String password = params[5];

            try {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("fname","UTF-8") +"="+ URLEncoder.encode(fname,"UTF-8")+"&"+
                        URLEncoder.encode("location","UTF-8") +"="+ URLEncoder.encode(location,"UTF-8")+"&"+
                        URLEncoder.encode("phonenumber","UTF-8") +"="+ URLEncoder.encode(phonenumber,"UTF-8")+"&"+
                        URLEncoder.encode("username","UTF-8") +"="+ URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8") +"="+ URLEncoder.encode(password,"UTF-8");

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

                return result;

            } catch (MalformedURLException e) {
                return "URL Exception";
//                e.printStackTrace();
            } catch (IOException e) {
                return "Input/Output Error";
//                e.printStackTrace();
            }

        } else if(function.equals("login")) {
            String username = params[1];
            String password = params[2];

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

                String data = URLEncoder.encode("username","UTF-8") +"="+ URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8") +"="+ URLEncoder.encode(password,"UTF-8");

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
                    JSONObject userDetails = resultJSON.getJSONObject("userdetails");
                    String userid = userDetails.getString("userid");
                    String fname = userDetails.getString("name");
                    String location = userDetails.getString("location");
                    String phonenumber = userDetails.getString("phonenumber");
                    String usernam = userDetails.getString("username");
                    String adcount = userDetails.getString("adcount");

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("userid", userid);
                    editor.putString("userfname", fname);
                    editor.putString("userlocation", location);
                    editor.putString("userpnumber", phonenumber);
                    editor.putString("userusername", usernam);
                    editor.putString("useradcount", adcount);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;

            } catch (MalformedURLException e) {
                return "URL Exception";
                // e.printStackTrace();
            } catch (IOException e) {
                return "Input/Output Error";
                // e.printStackTrace();
            }

        }else if(function.equals("logout")){
            String useruserid = params[1];

            try {
                URL url = new URL(logout_url);
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

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userid", null);
                editor.putString("userfname", null);
                editor.putString("userlocation", null);
                editor.putString("userpnumber", null);
                editor.putString("userusername", null);
                editor.putString("useradcount", null);
                editor.apply();

                return result;

            } catch (MalformedURLException e) {
                return "URL Exception";
                // e.printStackTrace();
            } catch (IOException e) {
                return "Input/Output Error";
                // e.printStackTrace();
            }



        }

        return "Failed at a function";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean type = resultJSON.getBoolean("type");

            if (type == false)
            {
                String message = resultJSON.getString("message");
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(context,"Logged in successfully...",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
