package sakuraiandco.com.gtcollab.activities;

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

import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.AdapterListener;
import sakuraiandco.com.gtcollab.adapters.TermAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.TermDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseActvitiy;

public class TermListActivity extends AppCompatActivity {

    // data
    TermDAO termDAO;

    // adapter
    TermAdapter termAdapter;

    // layout manager
    LinearLayoutManager linearLayoutManager;

    // view
    TextView textNoTermsFound; // TODO: is this possible?
    RecyclerView termsRecyclerView;

    // context
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        termDAO = new TermDAO(new BaseDAO.Listener<Term>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(TermListActivity.this, "TermDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Term> terms) {
                onTermListReady(terms);
            }
            @Override
            public void onObjectReady(Term term) {
                onTermObjectReady(term);
            }
            @Override
            public void onObjectDeleted() {}
        });

        // adapter
        termAdapter = new TermAdapter(new AdapterListener<Term>() {
            @Override
            public void onClick(Term term) {
                startCourseActvitiy(TermListActivity.this, user, term, null);
            }
        });

        // layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        // view
        textNoTermsFound = findViewById(R.id.text_no_terms_found);
        termsRecyclerView = findViewById(R.id.terms_recycler_view);
        termsRecyclerView.setAdapter(termAdapter);
        termsRecyclerView.setLayoutManager(linearLayoutManager);
        termsRecyclerView.setHasFixedSize(true); // TODO: optimization
        termsRecyclerView.addOnScrollListener(new PaginationScrollListener<>(linearLayoutManager, termDAO));

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        user = intent.getParcelableExtra(EXTRA_USER);
        termDAO.getCurrent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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

    private void onTermListReady(List<Term> terms) {
        termAdapter.addData(terms);
        if (termAdapter.getData().isEmpty()) {
            textNoTermsFound.setVisibility(View.VISIBLE);
        } else {
            textNoTermsFound.setVisibility(View.GONE);
        }
    }

    private void onTermObjectReady(Term term) {
        termAdapter.setCurrentTerm(term);
        termDAO.getAll();
    }

}
