package sakuraiandco.com.gtcollab.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_SEARCH_ACTIVITY;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_CALLING_ACTIVITY;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_TERM;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseSearchActivity;

/**
 * Created by kaliq on 10/17/2017.
 */

public class SearchResultsActivity extends AppCompatActivity {

    // data
    CourseDAO courseDAO;

    // context
    User user;
    Term term;
    Subject subject;

    int callingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: display a "loading" background to user
        courseDAO = new CourseDAO(new BaseDAO.Listener<Course>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(SearchResultsActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Course> courses) {
                startCourseSearchActivity(SearchResultsActivity.this, user, term, null, courses);
                finish();
            }
            @Override
            public void onObjectReady(Course course) {}
            @Override
            public void onObjectDeleted() {}
        });
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        callingActivity = intent.getIntExtra(EXTRA_CALLING_ACTIVITY, -1);
        user = intent.getParcelableExtra(EXTRA_USER); // TODO: null check
        term = intent.getParcelableExtra(EXTRA_TERM); // TODO: null check
        subject = intent.getParcelableExtra(EXTRA_SUBJECT); // TODO: null check
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Map<String, String> filters = new HashMap<>();
            filters.put(FILTER_TERM, String.valueOf(term.getId())); // TODO: only search current term?
            if (callingActivity == COURSE_SEARCH_ACTIVITY && subject != null) {
                filters.put(FILTER_SUBJECT, String.valueOf(subject.getId()));
            }
            courseDAO.getBySearchAndFilters(searchQuery, filters);
        } else {
            // TODO: error handling
        }
    }

}
