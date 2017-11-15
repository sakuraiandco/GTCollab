package sakuraiandco.com.gtcollab.activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.AdapterListener;
import sakuraiandco.com.gtcollab.adapters.CourseSearchAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_SEARCH_ACTIVITY;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_CALLING_ACTIVITY;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SEARCH_RESULTS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_SUBJECT;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseListActivity;

public class CourseSearchActivity extends AppCompatActivity {

    // data
    CourseDAO courseDAO;

    // adapter
    CourseSearchAdapter courseSearchAdapter;

    // layout manager
    LinearLayoutManager linearLayoutManager;

    // view
    TextView textNoCoursesFound;
    RecyclerView coursesRecyclerView;

    // context
    User user;
    Term term;
    Subject subject;
    List<Course> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        courseDAO = new CourseDAO(new BaseDAO.Listener<Course>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseSearchActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Course> courses) {
                onCourseListReady(courses);
            }
            @Override
            public void onObjectReady(Course course) {
                onCourseObjectReady(course);
            }
        });

        // adapter
        courseSearchAdapter = new CourseSearchAdapter(new AdapterListener<Course>() {
            @Override
            public void onClick(Course course) {
                if (course.getMembers().contains(user.getId())) {
                    Toast.makeText(CourseSearchActivity.this, "Already joined " + course.getShortName(), Toast.LENGTH_SHORT).show();
                    startCourseListActivity(CourseSearchActivity.this, user, term);
                } else {
                    courseDAO.joinCourse(course.getId()); // TODO: show dialog first?
                }
            }
        });

        // layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        // view
        textNoCoursesFound = findViewById(R.id.text_no_courses_found);
        coursesRecyclerView = findViewById(R.id.courses_recycler_view);
        coursesRecyclerView.setAdapter(courseSearchAdapter);
        coursesRecyclerView.setLayoutManager(linearLayoutManager);
        coursesRecyclerView.setHasFixedSize(true); // TODO: optimization
        coursesRecyclerView.addOnScrollListener(new PaginationScrollListener<>(linearLayoutManager, courseDAO));

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        user = intent.getParcelableExtra(EXTRA_USER);
        term = intent.getParcelableExtra(EXTRA_TERM);
        subject = intent.getParcelableExtra(EXTRA_SUBJECT);
        searchResults = intent.getParcelableArrayListExtra(EXTRA_SEARCH_RESULTS);

        if (searchResults != null) {
            courseSearchAdapter.setData(searchResults);
        } else {
            if (subject != null) {
                getSupportActionBar().setTitle(subject.getCode() + " Courses");
                Map<String, String> filters = new HashMap<>();
                filters.put(FILTER_SUBJECT, String.valueOf(subject.getId()));
                courseDAO.getByFilters(filters);
            } else {
                Toast.makeText(this, "No subject context", Toast.LENGTH_SHORT).show(); // TODO: error handling
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // TODO: use NavUtils instead?
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra(EXTRA_CALLING_ACTIVITY, COURSE_SEARCH_ACTIVITY);
            intent.putExtra(EXTRA_USER, user);
            intent.putExtra(EXTRA_TERM, term);
            intent.putExtra(EXTRA_SUBJECT, subject);
        }
        super.startActivity(intent);
    }

    private void onCourseListReady(List<Course> courses) {
        courseSearchAdapter.addData(courses);
        if (courseSearchAdapter.getData().isEmpty()) {
            textNoCoursesFound.setVisibility(View.VISIBLE);
        } else {
            textNoCoursesFound.setVisibility(View.GONE);
        }
    }

    private void onCourseObjectReady(Course course) {
        Toast.makeText(CourseSearchActivity.this, "Joined " + course.getShortName(), Toast.LENGTH_SHORT).show();
        startCourseListActivity(CourseSearchActivity.this, user, term);
    }

}
