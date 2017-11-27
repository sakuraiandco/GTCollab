package sakuraiandco.com.gtcollab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_REQUEST_CODE;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_RESULT_CODE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SELECTED_USERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.getUserIDs;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.getUserNames;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.joinStrings;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseActvitiy;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startUserSelectActivityForResult;

public class CreateGroupActivity extends AppCompatActivity {

    // data
    GroupDAO groupDAO;

    //view
    EditText editGroupName;
    Button buttonAddMembers;
    TextView textGroupMembers;
    Button buttonCreateGroup;

    // context
    User user;
    Term term;
    Course course;

    // variables
    List<User> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group );

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CreateGroupActivity.this, "GroupDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling - handle server-side validation error
            }
            @Override
            public void onListReady(List<Group> groups) {}
            @Override
            public void onObjectReady(Group group) {
                Toast.makeText(CreateGroupActivity.this, "Created new group: " + group.getName(), Toast.LENGTH_SHORT).show();
                startCourseActvitiy(CreateGroupActivity.this, user, term, course, TAB_GROUPS);
            }
            @Override
            public void onObjectDeleted() {}
        });

        // TODO reorganize
        members = new ArrayList<>();

        // view
        editGroupName = findViewById(R.id.edit_group_name);
        buttonAddMembers = findViewById(R.id.button_add_members);
        textGroupMembers = findViewById(R.id.text_group_members);
        buttonCreateGroup = findViewById(R.id.button_create_group);

        // handle intent
        handleIntent(getIntent());

        buttonAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserSelectActivityForResult(CreateGroupActivity.this, user, term, course, members, DEFAULT_REQUEST_CODE);
            }
        });

        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(editGroupName)) { // TODO: validate fields in order they appear on the screen
                    Group m = Group.builder()
                            .name(editGroupName.getText().toString().trim())
                            .courseId(course.getId())
                            .members(getUserIDs(members))
                            .build();
                    groupDAO.create(m);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        user = intent.getParcelableExtra(EXTRA_USER);
        term = intent.getParcelableExtra(EXTRA_TERM);
        course = intent.getParcelableExtra(EXTRA_COURSE);
        List<User> members = intent.getParcelableArrayListExtra(EXTRA_SELECTED_USERS);
        if (members != null) {
            this.members = members;
//            textGroupMembers.setText(joinStrings(getUserNames(members), "\n")); // TODO display users on UI
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEFAULT_REQUEST_CODE) {
            if (resultCode == DEFAULT_RESULT_CODE) {
                ArrayList<User> temp = data.getParcelableArrayListExtra(EXTRA_SELECTED_USERS);
                if (temp != null) {
                    members = temp;
//                    textGroupMembers.setText(joinStrings(getUserNames(members), "\n")); // TODO display users on UI
                }
            }
        }
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

}
