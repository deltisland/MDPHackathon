package com.hackathon.mdp.nathan.myapplication;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GetAuthToken extends AsyncTask<Object, Void, String> {

    static String auth_token;

    private String path_join(String ... args) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String arg : args) {
            String current = arg.replaceAll("^/+|/+$", "");

            // Skip empty strings
            if (current.isEmpty()) {
                continue;
            }

            if (first) {
                first = false;
            }
            else {
                sb.append("/");
            }

            sb.append(current);
        }

        return sb.toString();
    }

    private String urlencode(Map<?, ?> parameters) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        String encoding = "UTF-8";
        for (Map.Entry pair : parameters.entrySet()) {
            String k = pair.getKey().toString();
            String v = pair.getValue().toString();
            String current = URLEncoder.encode(k, encoding) + "=" + URLEncoder.encode(v, encoding);

            if (first) {
                first = false;
            }
            else {
                sb.append("&");
            }
            sb.append(current);
        }

        return sb.toString();
    }

    @Override
    protected String doInBackground(Object... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String key = PrivateKeys.key;
        String secret = PrivateKeys.secret;
        String version = "preview1";
        String practiceid = "00000";
        String base_url = "https://api.athenahealth.com";

        String token = null;
        try {
            HashMap<String, String> auth_prefixes = (HashMap<String, String>) params[0];

            URL url = new URL(path_join(base_url, auth_prefixes.get(version), "/token"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //http://stackoverflow.com/questions/7360403/base-64-encode-and-decode-example-code
            String auth = Base64.encodeToString((key + ":" + secret).getBytes(), Base64.DEFAULT);

            conn.setRequestProperty("Authorization", "Basic " + auth);

            conn.setDoOutput(true);
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("grant_type", "client_credentials");

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlencode(parameters));
            wr.flush();
            wr.close();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            JSONObject response = new JSONObject(sb.toString());
            token = response.get("access_token").toString();
        }catch (Exception e) {
            Log.e("GetAuthToken", "Error ", e);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
					Log.e("GetAuthToken", "Error closing stream", e);
                }
            }
        }

        return token;
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);
    }
}