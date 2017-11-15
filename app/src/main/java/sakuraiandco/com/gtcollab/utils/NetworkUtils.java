package sakuraiandco.com.gtcollab.utils;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
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

    public interface VolleyResponseListener extends Response.Listener<JSONObject>, Response.ErrorListener {}

    public static void getRequest(String url, VolleyResponseListener callback) {
        getRequest(url, null, callback);
    }

    public static void getRequest(String url, Map<String, String> queryParams, VolleyResponseListener callback) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        if (queryParams != null) {
            for (Map.Entry<String, String> e : queryParams.entrySet()) {
                builder.appendQueryParameter(e.getKey(), e.getValue());
            }
        }
        String finalUrl = builder.build().toString();
        baseRequest(Request.Method.GET, finalUrl, null, callback);
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
                String authToken = SingletonProvider.getContext().getSharedPreferences(Arguments.DEFAULT_SHARED_PREFERENCES, 0).getString(Arguments.AUTH_TOKEN, null);
                if (authToken != null) {
                    headers.put(Constants.AUTH_HEADER, "Token " + authToken);
                }
                return headers;
            }
        };
        SingletonProvider.getRequestQueue().add(r);
    }

}
