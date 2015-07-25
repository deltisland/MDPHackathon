package com.hackathon.mdp.nathan.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CallAPI extends AsyncTask<Object, Void, Object> {

    @Override
    protected Object doInBackground(Object... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String base_url = "https://api.athenahealth.com";


        String token = (String) params[0];
        String verb = (String) params[1];
        String path = (String) params[2];
        HashMap<String, String> parameters = (HashMap<String, String>) params[3];
        HashMap<String, String> headers = (HashMap<String, String>) params[4];
        String version = (String) params[5];
        String key = (String) params[6];
        String secret = (String) params[7];
        String practiceid = (String) params[8];

        String JSONReturnString;
        Object response = null;
        try {

            // Join up a url and open a connection
            URL url = new URL(AthenaUtil.path_join(base_url, version, practiceid, path));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(verb);

            // Set the Authorization header using the token, then do the rest of the headers
            conn.setRequestProperty("Authorization", "Bearer " + token);
            if (headers != null) {
                for (Map.Entry<String, String> pair : headers.entrySet()) {
                    conn.setRequestProperty(pair.getKey(), pair.getValue());
                }
            }

            // Set the request parameters, if there are any
            if (parameters != null) {
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(AthenaUtil.urlencode(parameters));
                wr.flush();
                wr.close();
            }

            // Read the input stream into a String
            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                JSONReturnString = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            JSONReturnString = buffer.toString();

            // If it won't parse as an object, it'll parse as an array.

            try {
                response = new JSONObject(JSONReturnString.toString());
            }
            catch (JSONException e) {
                response = new JSONArray(JSONReturnString.toString());
            }

        }catch (Exception e) {
            Log.e("CallAPI", "Error ", e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
    }
}