package sakuraiandco.com.gtcollab;

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

import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Arguments.SELECTED_USERS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;

public class CreateGroupActivity extends AppCompatActivity implements DAOListener<Group> {

    String courseId;

    GroupDAO groupDAO;

    EditText editGroupName;
    Button buttonAddMembers;
    TextView textGroupMembers;
    Button buttonCreateGroup;

    List<Integer> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group );

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        groupDAO = new GroupDAO(this);

        // TODO
//        String name;
        members = new ArrayList<>();

        // handle intent
        handleIntent(getIntent());

        // view
        editGroupName = (EditText) findViewById(R.id.edit_group_name);
        buttonAddMembers = (Button) findViewById(R.id.button_add_members);
        textGroupMembers = (TextView) findViewById(R.id.text_group_members);
        buttonCreateGroup = (Button) findViewById(R.id.button_create_group);

        buttonAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, UserSelectActivity.class);
                intent.putExtra(COURSE, courseId); // select users from course members
                startActivityForResult(intent, 0); // TODO: refactor request code into constant
            }
        });

        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(editGroupName)) { // TODO: validate fields in order they appear on the screen
                    Group m = Group.builder()
                            .name(editGroupName.getText().toString().trim())
                            .courseId(Integer.valueOf(courseId))
                            .members(members)
                            .build();
                    groupDAO.create(m);
                }
            }
        });
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(getString(R.string.error_field_required));
            return false;
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        courseId = intent.getStringExtra(COURSE);
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
        if (requestCode == 0) {
            if (resultCode == 0) {
                members = data.getIntegerArrayListExtra(SELECTED_USERS);
                textGroupMembers.setText(members.toString());
            }
        }
    }

    @Override
    public void onListReady(List<Group> groups) {}

    @Override
    public void onObjectReady(Group group) {
        Toast.makeText(this, "Created new group: " + group.getName(), Toast.LENGTH_SHORT).show();
        Intent courseActivityIntent = new Intent(CreateGroupActivity.this, CourseActivity.class);
        courseActivityIntent.putExtra(COURSE_ID, courseId);
        courseActivityIntent.putExtra(COURSE_TAB, TAB_GROUPS);
        startActivity(courseActivityIntent);
    }

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "GroupDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling - handle server-side validation error
    }

}
