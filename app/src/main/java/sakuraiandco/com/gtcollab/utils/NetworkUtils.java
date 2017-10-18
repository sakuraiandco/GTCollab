package sakuraiandco.com.gtcollab.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.constants.Constants;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;

/**
 * Created by kaliq on 10/15/2017.
 */

public class NetworkUtils {

    public static void getRequest(String url, VolleyResponseListener callback) {
        getRequest(url, null, callback);
    }

    public static void getRequest(String url, Map<String, String> queryParams, VolleyResponseListener callback) {
        String queryString = "";
        if (queryParams != null) {
            for (Map.Entry e : queryParams.entrySet()) {
                queryString += e.getKey() + "=" + e.getValue() + "&";
            }
            if (!queryString.isEmpty()) {
                queryString = "?" + queryString.substring(0, queryString.length()-1); // remove trailing '&'
            }
        }
        baseRequest(Request.Method.GET, url + queryString, null, callback);
    }

    public static void postRequest(String url, JSONObject body, VolleyResponseListener callback) {
        baseRequest(Request.Method.POST, url, body, callback);
    }

    public static void patchRequest(String url, JSONObject body, VolleyResponseListener callback) {
        baseRequest(Request.Method.PATCH, url, body, callback);
    }

    public static void deleteRequest(String url, VolleyResponseListener callback) {
        baseRequest(Request.Method.DELETE, url, null, callback);
    }

    private static void baseRequest(int method, String url, JSONObject body, VolleyResponseListener callback) {
        JsonObjectRequest r = new JsonObjectRequest(method, url, body, callback, callback) { // TODO: can body be null?
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                String authToken = SingletonProvider.getContext().getSharedPreferences(Arguments.AUTH_TOKEN_FILE, 0).getString(Arguments.AUTH_TOKEN, null);
                if (authToken != null) {
                    headers.put(Constants.AUTH_HEADER, "Token " + authToken);
                }
                return headers;
            }
        };
        SingletonProvider.getRequestQueue().add(r);
    }

}
