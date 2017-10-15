package sakuraiandco.com.gtcollab.temp.constants;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kaliq on 9/9/2017.
 */

public class SingletonProvider {

    private static Context context;
    private static RequestQueue requestQueue;

    private SingletonProvider(){}

    public static synchronized RequestQueue getRequestQueue(){
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
        }
        return requestQueue;
    }

    public static synchronized Context getContext() {
        if (context == null) {
            throw new IllegalStateException("Context has not been set");
        }
        return context;
    }

    public static synchronized void setContext(Context context) {
        SingletonProvider.context = context.getApplicationContext();
    }

}
