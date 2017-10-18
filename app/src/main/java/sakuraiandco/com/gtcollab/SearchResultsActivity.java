package sakuraiandco.com.gtcollab;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.SEARCH_RESULTS;
import static sakuraiandco.com.gtcollab.constants.Arguments.TERM;
import static sakuraiandco.com.gtcollab.constants.Constants.CURR_TERM;

/**
 * Created by kaliq on 10/17/2017.
 */

public class SearchResultsActivity extends AppCompatActivity implements DAOListener<Course> {

    CourseDAO courseDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseDAO = new CourseDAO(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d("SEARCH RESULTS TEST", "MADE IT HERE");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Map<String, String> filters = new HashMap<>();
            filters.put(TERM, CURR_TERM); // TODO: only search current term?
            courseDAO.getBySearchAndFilters(query, filters);
        }
    }

    @Override
    public void onListReady(List<Course> courses) {
        Intent courseSearchActivityIntent = new Intent(this, CourseSearchActivity.class);
        courseSearchActivityIntent.putParcelableArrayListExtra(SEARCH_RESULTS, new ArrayList<Course>(courses));
        startActivity(courseSearchActivityIntent);
    }

    @Override
    public void onObjectReady(Course course) {}

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
    }
}
