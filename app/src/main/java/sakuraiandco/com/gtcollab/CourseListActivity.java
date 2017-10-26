package sakuraiandco.com.gtcollab;

import android.content.Intent;
import android.os.Bundle;
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

import sakuraiandco.com.gtcollab.adapters.CourseListAdapter;
import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN_FILE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.MEMBERS;

public class CourseListActivity extends AppCompatActivity implements DAOListener<Course>,CourseListAdapter.Listener {

    CourseDAO courseDAO;

    TextView textNoCoursesFound;
    RecyclerView coursesRecyclerView;
    CourseListAdapter courseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        courseDAO = new CourseDAO(this);

        // adapter
        courseListAdapter = new CourseListAdapter(this);

        // view
        textNoCoursesFound = (TextView) findViewById(R.id.text_no_courses_found);
        coursesRecyclerView = (RecyclerView) findViewById(R.id.courses_recycler_view);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false));
        coursesRecyclerView.setHasFixedSize(true); // TODO: optimization
        coursesRecyclerView.setAdapter(courseListAdapter);

        // retrieve data
        String userId = getSharedPreferences(AUTH_TOKEN_FILE, 0).getString(CURRENT_USER, null);
        if (userId != null) {
            Map<String, String> filters = new HashMap<>();
            filters.put(MEMBERS, userId);
            courseDAO.getByFilters(filters);
        } else {
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
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
            case R.id.action_add_course:
                Intent subjectSearchActivity = new Intent(this, SubjectSearchActivity.class);
                startActivity(subjectSearchActivity);
                return true;
            case R.id.action_logout:
                // delete auth token on logout
                this.getSharedPreferences(Arguments.AUTH_TOKEN_FILE, 0).edit().remove(Arguments.AUTH_TOKEN).apply();
                Intent loginActivityIntent = new Intent(this, LoginActivity.class);
                startActivity(loginActivityIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListReady(List<Course> courses) {
        if (!courses.isEmpty()) {
            textNoCoursesFound.setVisibility(View.GONE);
            courseListAdapter.setData(courses);
        } else {
            textNoCoursesFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onObjectReady(Course course) {}

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
    }

    @Override
    public void onClickCourseAdapter(View view, int objectId) {
        Intent courseActivityIntent = new Intent(this, CourseActivity.class);
        courseActivityIntent.putExtra(COURSE_ID, String.valueOf(objectId));
        startActivity(courseActivityIntent);
    }
}
