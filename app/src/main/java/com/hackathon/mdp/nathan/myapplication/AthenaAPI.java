package com.hackathon.mdp.nathan.myapplication;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AthenaAPI {

    private String key;
    private String secret;
    private String version;
    private String practiceid;
    private String base_url;
    private String token;

    // http://stackoverflow.com/q/507602
    private static final Map<String, String> auth_prefixes;
    static {
        Map<String, String> tempMap = new HashMap<String, String>();
        tempMap.put("v1", "/oauth");
        tempMap.put("preview1", "/oauthpreview");
        tempMap.put("openpreview1", "/oauthopenpreview");
        auth_prefixes = Collections.unmodifiableMap(tempMap);
    }

    /**
     * Connect to the specified API version using key and secret.
     *
     * @param version API version to access
     * @param key     client key (also known as ID)
     * @param secret  client secret
     * @throws Exception from authentication
     */
    public AthenaAPI(String version, String key, String secret) throws Exception {
        this(version, key, secret, "");
    }

    /**
     * Connect to the specified API version using key and secret.
     *
     * @param version    API version to access
     * @param key        client key (also known as ID)
     * @param secret     client secret
     * @param practiceid practice ID to use
     * @throws Exception from authentication
     */
    public AthenaAPI(String version, String key, String secret, String practiceid) throws Exception {
        this.version = version;
        this.key = key;
        this.secret = secret;
        this.practiceid = practiceid;
        this.base_url = "https://api.athenahealth.com";

        authenticate();
    }

    /**
     * Perform the steps of basic authentication.
     */
    private void authenticate() throws Exception {

        HashMap<String, String> auth_prefixes = new HashMap<String, String>();
        auth_prefixes.put("v1", "/oauth");
        auth_prefixes.put("preview1", "/oauthpreview");
        auth_prefixes.put("openpreview1", "/oauthopenpreview");
        try {
            token = new GetAuthToken().execute(auth_prefixes).get();
        } catch (Exception e) {
            Log.e("error", "didn't get auth token");
        }
    }

    /**
     * Join arguments into a valid path.
     *
     * @param args parts of the path to join
     * @return the joined path
     */
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

    /**
     * Convert parameters into a URL query string.
     *
     * @param parameters keys and values to encode
     * @return the query string
     */
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


    /**
     * Make the API call.
     *
     * This method abstracts away the connection, streams, and readers necessary to make an HTTP
     * request.  It also adds in the Authorization header and token.
     *
     * @param verb       HTTP method to use
     * @param path       URI to find
     * @param parameters key-value pairs of request parameters
     * @param headers    key-value pairs of request headers
     * @param secondcall true if this is the retried request
     * @return the JSON-decoded response
     * @throws Exception
     */
    private Object call(String verb, String path, Map<String, String> parameters, Map<String, String> headers, boolean secondcall) throws Exception {
        Object retVal = new CallAPI().execute(token, verb, path, parameters, headers, version, key, secret, practiceid).get();
        return retVal;
    }


    /**
     * Perform a GET request.
     *
     * @param path URI to access
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object GET(String path) throws Exception {
        return GET(path, null, null);
    }

    /**
     * Perform a GET request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object GET(String path, Map<String, String> parameters) throws Exception {
        return GET(path, parameters, null);
    }

    /**
     * Perform a GET request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @param headers    the request headers
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object GET(String path, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        String query = "";
        if (parameters != null) {
            query = "?" + urlencode(parameters);
        }
        System.out.println(path+query);
        return call("GET", path + query, null, headers, false);
    }


    /**
     * Perform a POST request.
     *
     * @param path URI to access
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object POST(String path) throws Exception {
        return POST(path, null, null);
    }

    /**
     * Perform a POST request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object POST(String path, Map<String, String> parameters) throws Exception {
        return POST(path, parameters, null);
    }

    /**
     * Perform a POST request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @param headers    the request headers
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object POST(String path, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        return call("POST", path, parameters, headers, false);
    }


    /**
     * Perform a PUT request.
     *
     * @param path URI to access
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object PUT(String path) throws Exception {
        return PUT(path, null, null);
    }

    /**
     * Perform a PUT request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object PUT(String path, Map<String, String> parameters) throws Exception {
        return PUT(path, parameters, null);
    }

    /**
     * Perform a PUT request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @param headers    the request headers
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object PUT(String path, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        return call("PUT", path, parameters, headers, false);
    }


    /**
     * Perform a DELETE request.
     *
     * @param path URI to access
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object DELETE(String path) throws Exception {
        return DELETE(path, null, null);
    }

    /**
     * Perform a DELETE request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object DELETE(String path, Map<String, String> parameters) throws Exception {
        return DELETE(path, parameters, null);
    }

    /**
     * Perform a DELETE request.
     *
     * @param path       URI to access
     * @param parameters the request parameters
     * @param headers    the request headers
     * @return the JSON-decoded response
     * @throws Exception
     */
    public Object DELETE(String path, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        String query = "";
        if (parameters != null) {
            query = "?" + urlencode(parameters);
        }
        return call("DELETE", path + query, null, headers, false);
    }

    /**
     * Returns the current access token
     *
     * @return the access token
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the practice ID to use for requests.
     *
     * @param practiceid the new practiceid
     */
    public void setPracticeID(String practiceid) {
        this.practiceid = practiceid;
    }

    /**
     * Returns the practice ID currently in use.
     *
     * @return the practice ID
     */
    public String getPracticeID() {
        return this.practiceid;
    }
}
