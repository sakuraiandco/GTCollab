package sakuraiandco.com.gtcollab;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.adapters.CourseSearchAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.SEARCH_RESULTS;
import static sakuraiandco.com.gtcollab.constants.Arguments.SUBJECT;

public class CourseSearchActivity extends AppCompatActivity implements DAOListener<Course>,CourseSearchAdapter.Listener {

    CourseDAO courseDAO;

    TextView textNoCoursesFound;
    RecyclerView coursesRecyclerView;
    CourseSearchAdapter courseSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        courseDAO = new CourseDAO(this);

        // adapter
        courseSearchAdapter = new CourseSearchAdapter(this);

        // view
        textNoCoursesFound = (TextView) findViewById(R.id.text_no_courses_found);
        coursesRecyclerView = (RecyclerView) findViewById(R.id.courses_recycler_view);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false));
        coursesRecyclerView.setHasFixedSize(true); // TODO: optimization
        coursesRecyclerView.setAdapter(courseSearchAdapter);

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        List<Course> courses = intent.getParcelableArrayListExtra(SEARCH_RESULTS);
        if (courses != null) {
            courseSearchAdapter.setData(courses);
        } else {
            String subject = intent.getStringExtra(SUBJECT);
            if (subject != null) {
                Map<String, String> filters = new HashMap<>();
                filters.put(SUBJECT, subject);
                courseDAO.getByFilters(filters);
            } else {
                Toast.makeText(this, "No subject", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onListReady(List<Course> courses) {
        if (!courses.isEmpty()) {
            textNoCoursesFound.setVisibility(View.GONE);
            courseSearchAdapter.setData(courses);
        } else {
            textNoCoursesFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onObjectReady(Course course) {
        Toast.makeText(this, "Joined " + course.getSubjectCode() + " " + course.getCourseNumber(), Toast.LENGTH_SHORT).show();
        Intent courseListActivityIntent = new Intent(this, CourseListActivity.class);
        startActivity(courseListActivityIntent);
    }

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
    }

    @Override
    public void onClickCourseAdapter(View view, int objectId) {
        courseDAO.joinCourse(objectId);
    }

}
