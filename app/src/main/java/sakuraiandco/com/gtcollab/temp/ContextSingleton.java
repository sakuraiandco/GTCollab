package sakuraiandco.com.gtcollab.temp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alex on 10/14/17.
 */

class ContextSingleton {
    private static ContextSingleton instance;
    private static Context context;

    private RequestQueue requestQueue;

    private ContextSingleton(Context context) {
        ContextSingleton.context = context.getApplicationContext();
        this.requestQueue = getRequestQueue();
    }

    public static synchronized ContextSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ContextSingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

}
