package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CoursePageActivity extends AppCompatActivity {

    private ContextSingleton contextSingleton;
    private RequestQueue requestQueue;
    private RequestHandler requestHandler;
    private RecyclerView rvMeetings;
    private MeetingAdapter meetingAdapter;
    private Context context;


    private int courseID;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        rvMeetings = (RecyclerView) findViewById(R.id.meetingList);

        meetingAdapter = new MeetingAdapter(this);
        rvMeetings.setAdapter(meetingAdapter);
        rvMeetings.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID", -1);
        courseName = intent.getStringExtra("courseName");

        ((TextView) this.findViewById(R.id.courseTitle)).setText(courseName);

        Log.d("id", Integer.toString(courseID));

        contextSingleton = ContextSingleton.getInstance(this.getApplicationContext());
        requestQueue = contextSingleton.getRequestQueue();
        requestHandler = Singleton.getRequestHandler();
        context = this;

        ((ImageButton) this.findViewById(R.id.addMeetingButton)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMeetingActivity.class);
                intent.putExtra("courseID", courseID);
                intent.putExtra("courseName", courseName);
                context.startActivity(intent);
            }
        });


        populateMeetings();

        SearchView search = (SearchView) findViewById(R.id.meetingSearch);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                meetingAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    // TODO: change to upcoming meetings with user clock
    private void populateMeetings() {
        HashMap<String, String> params = new HashMap<>(1);
        params.put("course", Integer.toString(courseID));

        final Request request = requestHandler.getRequest("meetings", "GET", params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray meetings;
                        try {
                            meetings = response.getJSONArray("results");
                            for (int i = 0; i < meetings.length(); i++) {
                                JSONObject meetingJSON = meetings.getJSONObject(i);
                                Meeting meeting = new Meeting(meetingJSON, context);
                                meetingAdapter.addMeeting(meeting);
                            }
                            meetingAdapter.cacheMeetings();
                        } catch (JSONException error) {
                            Log.e("error", error.toString());
                        }
                    }
                });

        requestQueue.add(request);
    }

}
