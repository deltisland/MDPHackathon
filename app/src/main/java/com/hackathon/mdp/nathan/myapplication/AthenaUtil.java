package com.hackathon.mdp.nathan.myapplication;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by christopherjwang on 7/24/15.
 */
public class AthenaUtil {
    public static String path_join(String ... args) {
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

    public static String urlencode(Map<?, ?> parameters) {
        String retVal = null;

        try {
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

            retVal = sb.toString();
        } catch (Exception e) {
            Log.e("urlencode", e.getMessage());
        }

        if (retVal == null) {
            Log.e("AthenaUtil", "urlencode errored out");
        }

        return retVal;

    }
}
