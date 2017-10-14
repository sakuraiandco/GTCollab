package sakuraiandco.com.gtcollab;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 10/14/17.
 */

public class RequestHandler {
    private String baseURL;
    private String authorization;

    public RequestHandler(String baseURL, String authorization) {
        this.baseURL = baseURL;
        this.authorization = authorization;
    }

    public Request getRequest(String path, Response.Listener<JSONObject> listener) {
        return getRequest(path, "GET", listener);
    }

    public Request getRequest(String path, String requestMethod, Response.Listener<JSONObject> listener) {
        return getRequest(path, requestMethod, null, listener);
    }

    public Request getRequest(String path, String requestMethod, HashMap<String, String> params, Response.Listener<JSONObject> listener) {
        return getRequest(path, requestMethod, params, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // default error listener
                Log.e("error", error.toString());
            }
        });
    }

    public Request getRequest(String path, String requestMethod, HashMap<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        int method = Request.Method.GET;
        try {
            method = (int) Request.Method.class.getField(requestMethod).get(null);
        } catch (Exception error) {
            Log.e("Error", error.toString());
        }

        String url = this.baseURL.concat(path);

        JSONObject jsonRequest = params == null ? null : new JSONObject(params);

        return new JsonObjectRequest (method, url, jsonRequest, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", authorization);
                return headers;
            }
        };
    }

}
