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
import sakuraiandco.com.gtcollab.adapters.SubjectSearchAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Subject;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.SubjectDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_CALLING_ACTIVITY;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.SUBJECT_SEARCH_ACTIVITY;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseSearchActivity;

public class SubjectSearchActivity extends AppCompatActivity {

    // data
    SubjectDAO subjectDAO;

    // adapter
    SubjectSearchAdapter subjectSearchAdapter;

    // layout manager
    LinearLayoutManager linearLayoutManager;

    // view
    TextView textNoSubjectsFound;
    RecyclerView subjectsRecyclerView;

    // context
    User user;
    Term term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_search);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        subjectDAO = new SubjectDAO(new BaseDAO.Listener<Subject>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(SubjectSearchActivity.this, "SubjectDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Subject> subjects) {
                onSubjectListReady(subjects);
            }
            @Override
            public void onObjectReady(Subject subject) {}
        });

        // adapter
        subjectSearchAdapter = new SubjectSearchAdapter(new AdapterListener<Subject>() {
            @Override
            public void onClick(Subject subject) {
                startCourseSearchActivity(SubjectSearchActivity.this, user, term, subject, null);
            }
        });

        // layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        // view
        textNoSubjectsFound = (TextView) findViewById(R.id.text_no_subjects_found);
        subjectsRecyclerView = (RecyclerView) findViewById(R.id.subjects_recycler_view);
        subjectsRecyclerView.setAdapter(subjectSearchAdapter);
        subjectsRecyclerView.setLayoutManager(linearLayoutManager);
        subjectsRecyclerView.setHasFixedSize(true); // TODO: optimization
        subjectsRecyclerView.addOnScrollListener(new PaginationScrollListener<>(linearLayoutManager, subjectDAO));

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
        if (term != null) {
            getSupportActionBar().setTitle(term.getName() + " Subjects");
            Map<String, String> filters = new HashMap<>();
            filters.put(FILTER_TERM, String.valueOf(term.getId()));
            subjectDAO.getByFilters(filters);
        } else {
            Toast.makeText(this, "No term context", Toast.LENGTH_SHORT).show(); // TODO: error handling
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
            intent.putExtra(EXTRA_CALLING_ACTIVITY, SUBJECT_SEARCH_ACTIVITY);
            intent.putExtra(EXTRA_USER, user);
            intent.putExtra(EXTRA_TERM, term);
        }
        super.startActivity(intent);
    }


    public void onSubjectListReady(List<Subject> subjects) {
        subjectSearchAdapter.addData(subjects);
        if (!subjectSearchAdapter.getData().isEmpty()) {
            textNoSubjectsFound.setVisibility(View.GONE);
        } else {
            textNoSubjectsFound.setVisibility(View.VISIBLE);
        }
    }

}
