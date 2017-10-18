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

import sakuraiandco.com.gtcollab.adapters.UserListAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSES_AS_MEMBER;
import static sakuraiandco.com.gtcollab.constants.Arguments.GROUP;
import static sakuraiandco.com.gtcollab.constants.Arguments.GROUPS_AS_MEMBER;
import static sakuraiandco.com.gtcollab.constants.Arguments.MEETING;
import static sakuraiandco.com.gtcollab.constants.Arguments.MEETINGS_AS_MEMBER;
import static sakuraiandco.com.gtcollab.constants.Arguments.TITLE;

public class UserListActivity extends AppCompatActivity implements DAOListener<User>,UserListAdapter.Listener {

    UserDAO userDAO;

    TextView textNoUsersFound;
    RecyclerView usersRecyclerView;
    UserListAdapter userListAdapter;

    String actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        userDAO = new UserDAO(this);

        // adapter
        userListAdapter = new UserListAdapter(this);

        // view
        textNoUsersFound = (TextView) findViewById(R.id.text_no_users_found);
        usersRecyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getBaseContext(), LinearLayoutManager.VERTICAL, false));
        usersRecyclerView.setHasFixedSize(true); // TODO: optimization
        usersRecyclerView.setAdapter(userListAdapter);

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String title = intent.getStringExtra(TITLE);
        String courseId = intent.getStringExtra(COURSE);
        String groupId = intent.getStringExtra(GROUP);
        String meetingId = intent.getStringExtra(MEETING);

        actionBarTitle = title;

        Map<String, String> filters = new HashMap<>();
        if (courseId != null) {
            filters.put(COURSES_AS_MEMBER, courseId);
        } else if (groupId != null) {
            filters.put(GROUPS_AS_MEMBER, groupId);
        } else if (meetingId != null) {
            filters.put(MEETINGS_AS_MEMBER, meetingId);
        } else {
            Toast.makeText(this, "No context", Toast.LENGTH_SHORT).show(); // TODO: error handling
        }
        userDAO.getByFilters(filters);
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

    @Override
    public void onListReady(List<User> users) {
        if (!users.isEmpty()) {
            textNoUsersFound.setVisibility(View.GONE);
            userListAdapter.setData(users);
        } else {
            textNoUsersFound.setVisibility(View.VISIBLE);
        }
        actionBarTitle += " - " + users.size() + " Members";
        getSupportActionBar().setTitle(actionBarTitle);
    }

    @Override
    public void onObjectReady(User user) {}

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "UserDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
    }

    @Override
    public void onClickUserAdapter(View view, int objectId) {
        // TODO: show user profile?
    }
}
