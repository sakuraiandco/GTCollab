package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ContextSingleton contextSingleton;
    private RequestQueue requestQueue;
    private RequestHandler requestHandler;
    private RecyclerView rvCourses;
    private CourseAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCourses = (RecyclerView) findViewById(R.id.courseList);

        adapter = new CourseAdapter(this);
        rvCourses.setAdapter(adapter);
        rvCourses.setLayoutManager(new LinearLayoutManager(this));

        contextSingleton = ContextSingleton.getInstance(this.getApplicationContext());
        requestQueue = contextSingleton.getRequestQueue();
        requestHandler = Singleton.getRequestHandler();
        context = this;

        populateCourses();

    }

    private void populateCourses() {
        HashMap<String, String> params = new HashMap<>(1);
        params.put("subject_term", "1");

        final Request courseListRequest = requestHandler.getRequest("courses", "GET", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray courses;
                        try {
                            courses = response.getJSONArray("results");
                            for (int i = 0; i < courses.length(); i++) {
                                JSONObject courseJSON = courses.getJSONObject(i);
                                Course course = new Course(courseJSON, context);
                                adapter.addCourse(course);
                            }
                        } catch (JSONException error) {
                            Log.e("error", error.toString());
                        }
                    }
                });

        requestQueue.add(courseListRequest);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
