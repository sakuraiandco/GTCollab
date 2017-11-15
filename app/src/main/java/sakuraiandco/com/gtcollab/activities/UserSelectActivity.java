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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.UserSelectAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_RESULT_CODE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SELECTED_USERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_COURSES_AS_MEMBER;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.getUserIDs;

public class UserSelectActivity extends AppCompatActivity {

    // data
    UserDAO userDAO;

    // adapter
    UserSelectAdapter userSelectAdapter;

    // layout manager
    LinearLayoutManager linearLayoutManager;

    //view
    TextView textNoUsersFound;
    RecyclerView usersRecyclerView;

    // context
    User user;
    Term term;
    Course course;
    List<User> selectedUsers;
    List<Integer> selectedUsersIDs;

    // variables
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        userDAO = new UserDAO(new BaseDAO.Listener<User>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(UserSelectActivity.this, "UserDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<User> users) {
                onUserListReady(users);
            }
            @Override
            public void onObjectReady(User user) {}
        });

        // adapter
        userSelectAdapter = new UserSelectAdapter();

        // layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        // view
        textNoUsersFound = findViewById(R.id.text_no_users_found);
        usersRecyclerView = findViewById(R.id.users_recycler_view);
        usersRecyclerView.setAdapter(userSelectAdapter);
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        usersRecyclerView.setHasFixedSize(true); // TODO: optimization
        usersRecyclerView.addOnScrollListener(new PaginationScrollListener<>(linearLayoutManager, userDAO));

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
        course = intent.getParcelableExtra(EXTRA_COURSE);
        selectedUsers = intent.getParcelableArrayListExtra(EXTRA_SELECTED_USERS);
        selectedUsersIDs = getUserIDs(selectedUsers);

        Map<String, String> filters = new HashMap<>();
        if (course != null) {
            filters.put(FILTER_COURSES_AS_MEMBER, String.valueOf(course.getId()));
            title = course.getShortName();
        } else {
            Toast.makeText(this, "No context", Toast.LENGTH_SHORT).show(); // TODO: error handling
        }
        getSupportActionBar().setTitle(title);
        userDAO.getByFilters(filters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        setResult(DEFAULT_RESULT_CODE, intent);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.submit:
                intent.putParcelableArrayListExtra(EXTRA_SELECTED_USERS, new ArrayList<>(userSelectAdapter.getSelected()));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onUserListReady(List<User> users) {
        userSelectAdapter.addData(users);
        if (!selectedUsersIDs.isEmpty()) {
            for (User u : users) {
                if (selectedUsersIDs.contains(u.getId())) {
                    selectedUsersIDs.remove((Integer) u.getId());
                    userSelectAdapter.addSelected(u);
                }
            }
        }
        if (!userSelectAdapter.getData().isEmpty()) {
            textNoUsersFound.setVisibility(View.GONE);
        } else {
            textNoUsersFound.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle(title + " - " + users.size() + " Members");
    }

    public void onClickUser(User user) {
        // TODO: show user profile?
    }
}
