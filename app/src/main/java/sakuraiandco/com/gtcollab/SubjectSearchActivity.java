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

import sakuraiandco.com.gtcollab.adapters.SubjectSearchAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.rest.SubjectDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.TERM;
import static sakuraiandco.com.gtcollab.constants.Constants.CURR_TERM;

public class SubjectSearchActivity extends AppCompatActivity implements DAOListener<Subject>,SubjectSearchAdapter.Listener {

    SubjectDAO subjectDAO;

    TextView textNoSubjectsFound;
    RecyclerView subjectsRecyclerView;
    SubjectSearchAdapter subjectSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_search);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        subjectDAO = new SubjectDAO(this);

        // adapter
        subjectSearchAdapter = new SubjectSearchAdapter(this);

        // view
        textNoSubjectsFound = (TextView) findViewById(R.id.text_no_subjects_found);
        subjectsRecyclerView = (RecyclerView) findViewById(R.id.subjects_recycler_view);
        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false));
        subjectsRecyclerView.setHasFixedSize(true); // TODO: optimization
        subjectsRecyclerView.setAdapter(subjectSearchAdapter);

        // retrieve data
        Map<String, String> filters = new HashMap<>();
        filters.put(TERM, CURR_TERM);
        subjectDAO.getByFilters(filters);
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
    public void onListReady(List<Subject> subjects) {
        if (!subjects.isEmpty()) {
            textNoSubjectsFound.setVisibility(View.GONE);
            subjectSearchAdapter.setData(subjects);
        } else {
            textNoSubjectsFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onObjectReady(Subject subject) {}

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "SubjectDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
    }

    @Override
    public void onClickSubjectAdapter(View view, int objectId) {
        Intent courseSearchActivityIntent = new Intent(this, CourseSearchActivity.class);
        courseSearchActivityIntent.putExtra(SUBJECT, String.valueOf(objectId));
        startActivity(courseSearchActivityIntent);
    }

}
