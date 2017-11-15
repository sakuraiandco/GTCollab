package sakuraiandco.com.gtcollab.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.AdapterListener;
import sakuraiandco.com.gtcollab.adapters.CourseListAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.TermDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_SHARED_PREFERENCES;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_MEMBERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_TERM;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.forceDeviceTokenRefresh;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.login;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.logout;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseActvitiy;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startSubjectSearchActivity;

public class CourseListActivity extends AppCompatActivity {

    // data
    TermDAO termDAO;
    CourseDAO courseDAO;

    // saved data
    SharedPreferences prefs;

    // adapter
    CourseListAdapter courseListAdapter;

    // view
    TextView textNoCoursesFound;
    RecyclerView coursesRecyclerView;
    FloatingActionButton fab;

    // context
    User user;
    Term term;

    // variables
    private String userId;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // device registration
        forceDeviceTokenRefresh(); // TODO: ASSUMES CourseListActivity is launcher activity; best place to put this? LoginActivity instead?

        // data
        termDAO = new TermDAO(new BaseDAO.Listener<Term>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseListActivity.this, "TermDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Term> terms) {}
            @Override
            public void onObjectReady(Term term) {
                onTermObjectReady(term);
            }
        });
        courseDAO = new CourseDAO(new BaseDAO.Listener<Course>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseListActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Course> courses) {
                onCourseListReady(courses);
            }
            @Override
            public void onObjectReady(Course course) {}
        });

        // saved data
        prefs = getSharedPreferences(DEFAULT_SHARED_PREFERENCES, 0);
        userId = prefs.getString(CURRENT_USER, null); // TODO: not needed? how to use this to verify authentication?
        authToken = prefs.getString(AUTH_TOKEN, null); // TODO: not needed? how to use this to verify authentication?


        // adapter
        // TODO: put in handleIntent()?
        Intent intent = getIntent();
        user = intent.getParcelableExtra(EXTRA_USER);
        term = intent.getParcelableExtra(EXTRA_TERM);
        courseListAdapter = new CourseListAdapter(new AdapterListener<Course>() {
            @Override
            public void onClick(Course course) {
                startCourseActvitiy(CourseListActivity.this, user, term, course);
            }
        });

        // retrieve data
        handleIntent(intent);

        // view
        textNoCoursesFound = findViewById(R.id.text_no_courses_found);
        coursesRecyclerView = findViewById(R.id.courses_recycler_view);
        coursesRecyclerView.setAdapter(courseListAdapter);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false));
        coursesRecyclerView.setHasFixedSize(true); // TODO: optimization
        // TODO: pagination needed for course list?
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (term != null) { // Wait for current term to be loaded
                    startSubjectSearchActivity(CourseListActivity.this, user, term);
                } else {
                    Toast.makeText(CourseListActivity.this, "Server busy", Toast.LENGTH_SHORT).show(); // TODO: error handling
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (userId != null && authToken != null) {
            if (term != null) {
                getCourses();
            } else {
                termDAO.getCurrent();
            }
        } else {
            login(this);
        }

        // TODO: update course status (e.g. new activity bubble)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getCourses() {
        Map<String, String> filters = new HashMap<>();
        filters.put(FILTER_TERM, String.valueOf(term.getId()));
        filters.put(FILTER_MEMBERS, String.valueOf(user.getId()));
        courseDAO.getByFilters(filters);
    }

    private void onTermObjectReady(Term term) {
        this.term = term;
        getCourses();
    }

    private void onCourseListReady(List<Course> courses) {
        courseListAdapter.setData(courses);
        if (courseListAdapter.getData().isEmpty()) {
            textNoCoursesFound.setVisibility(View.VISIBLE);
        } else {
            textNoCoursesFound.setVisibility(View.GONE);
        }
    }

}
