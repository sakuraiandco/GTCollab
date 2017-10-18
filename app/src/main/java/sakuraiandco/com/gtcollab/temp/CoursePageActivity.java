package sakuraiandco.com.gtcollab.temp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

public class CoursePageActivity extends AppCompatActivity{

    private ContextSingleton contextSingleton;
    private RequestQueue requestQueue;
    private RequestHandler requestHandler;
    private RecyclerView recyclerView;
    private MeetingAdapter meetingAdapter;
    private GroupAdapter groupAdapter;
    private Context context;


    private int courseID;
    private String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        recyclerView = (RecyclerView) findViewById(R.id.meetingList);

        meetingAdapter = new MeetingAdapter(this);
        groupAdapter = new GroupAdapter(this);

        final ToggleButton displayToggle = (ToggleButton) findViewById(R.id.courseDisplayToggle);

        displayToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (groupAdapter.isEmpty()) {
                        populateGroups();
                    }
                    recyclerView.setAdapter(groupAdapter);
                } else {
                    if (meetingAdapter.isEmpty()) {
                        populateMeetings();
                    }
                    recyclerView.setAdapter(meetingAdapter);

                }
            }
        });

        recyclerView.setAdapter(meetingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID", -1);
        courseName = intent.getStringExtra("courseName");

        ((TextView) this.findViewById(R.id.courseTitle)).setText(courseName);

        contextSingleton = ContextSingleton.getInstance(this.getApplicationContext());
        requestQueue = contextSingleton.getRequestQueue();
        requestHandler = Singleton.getRequestHandler();
        context = this;


        // TODO: change to take into account current "tab"
        ((ImageButton) this.findViewById(R.id.addMeetingButton)).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (displayToggle.isChecked()) {
                    // add group
                } else {
                    Intent intent = new Intent(context, AddMeetingActivity.class);
                    intent.putExtra("courseID", courseID);
                    intent.putExtra("courseName", courseName);
                    context.startActivity(intent);
                }
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

    private void populateGroups() {
        new GroupDAO(new DAOListener<Group>() {
            @Override
            public void onListReady(List<Group> groups) {
                groupAdapter.setGroups(groups);
            }
            @Override
            public void onObjectReady(Group group) {}

            @Override
            public void onDAOError(BaseDAO.Error error) {
                Log.e("GroupDAO error", error.toString());
            }
        }).getAll();
    }

}
