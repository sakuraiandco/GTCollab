package sakuraiandco.com.gtcollab;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alex on 10/14/17.
 */

class MySingleton {
    private static MySingleton instance;
    private static Context context;
    private RequestQueue requestQueue;
    private RequestHandler requestHandler;

    // TODO: consider moving variables elsewhere for organization, and changing authorization to be user specific
    private static String baseURL = "https://secure-headland-60131.herokuapp.com/api/";
    private static String authorization = "Token ae58c6766f9152f8ffe0a143382f4121fbf6e3cb";

    private MySingleton(Context context) {
        MySingleton.context = context.getApplicationContext();
        this.requestQueue = getRequestQueue();
        this.requestHandler = getRequestHandler();
    }


    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public RequestHandler getRequestHandler() {
        if (requestHandler == null) {
            requestHandler = new RequestHandler(baseURL, authorization);
        }
        return requestHandler;
    }

}
