package sakuraiandco.com.gtcollab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rvContacts = (RecyclerView) findViewById(R.id.courseList);

        // Create adapter passing in the sample user data
        final CourseAdapter adapter = new CourseAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        MySingleton singleton = MySingleton.getInstance(this.getApplicationContext());

        final RequestQueue requestQueue = singleton.getRequestQueue();
        final RequestHandler requestHandler = singleton.getRequestHandler();

        HashMap<String, String> params = new HashMap<>(1);
        params.put("subject_term", "1");

        final Request userListRequest = requestHandler.getRequest("courses", "GET", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray courses;
                        try {
                            courses = response.getJSONArray("results");
                            for (int i = 0; i < courses.length(); i++) {
                                JSONObject courseJSON = courses.getJSONObject(i);
                                Course course = new Course(courseJSON.getInt("id"), courseJSON.getString("name"), courseJSON.getJSONArray("members"));
                                adapter.addCourse(course);
                            }
                        } catch (JSONException error) {
                            Log.e("error", error.toString());
                        }
                    }
                });

        final Button button = (Button) findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("testing", "clicked");
                requestQueue.add(userListRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
