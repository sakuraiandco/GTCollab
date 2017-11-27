package sakuraiandco.com.gtcollab.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_REQUEST_CODE;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_RESULT_CODE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_SELECTED_USERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.getUserIDs;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseActvitiy;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startUserSelectActivityForResult;

public class CreateMeetingActivity extends AppCompatActivity {

    // data
    MeetingDAO meetingDAO;

    // view
    EditText editMeetingName;
    EditText editMeetingLocation;
    EditText editMeetingDescription;
    EditText editMeetingStartDate;
    EditText editMeetingStartTime;
    EditText editMeetingDuration;
    Button buttonCreateMeeting;

    // context
    User user;
    Term term;
    Course course;
    List<User> members;

    // variables
    DateTime now;
    LocalDate startDate;
    LocalTime startTime;

    // TODO: change validation so all error fields show error indication instead of one focused on
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CreateMeetingActivity.this, "MeetingDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling - handle server-side validation error
            }

            @Override
            public void onListReady(List<Meeting> meetings) {
            }

            @Override
            public void onObjectReady(Meeting meeting) {
                Toast.makeText(CreateMeetingActivity.this, "Created new meeting: " + meeting.getName(), Toast.LENGTH_SHORT).show();
                startCourseActvitiy(CreateMeetingActivity.this, user, term, course, TAB_MEETINGS);
            }

            @Override
            public void onObjectDeleted() {
            }
        });

        // TODO reorganize
        members = new ArrayList<>();

        // handle intent
        handleIntent(getIntent());

        // view
        editMeetingName = findViewById(R.id.edit_meeting_name);
        editMeetingLocation = findViewById(R.id.edit_meeting_location);
        editMeetingDescription = findViewById(R.id.edit_meeting_description);
        editMeetingStartDate = findViewById(R.id.edit_meeting_start_date);
        editMeetingStartTime = findViewById(R.id.edit_meeting_start_time);
        editMeetingDuration = findViewById(R.id.edit_meeting_duration);
        buttonCreateMeeting = findViewById(R.id.button_create_meeting);

        editMeetingStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePicker = new DatePickerDialog(
                            CreateMeetingActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    startDate = new LocalDate(year, month + 1, dayOfMonth);
                                    editMeetingStartDate.setText(startDate.toString("EEE MMM dd"));
                                    editMeetingStartDate.setError(null);
                                }
                            },
                            startDate.getYear(),
                            startDate.getMonthOfYear() - 1,
                            startDate.getDayOfMonth());
                    datePicker.setTitle("Select date");
                    datePicker.show();
                }
            }
        });

        editMeetingStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog timePicker = new TimePickerDialog(
                            CreateMeetingActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    startTime = new LocalTime(hourOfDay, minute);
                                    editMeetingStartTime.setText(startTime.toString("h:mm a"));
                                    editMeetingStartTime.setError(null);
                                }
                            },
                            startTime.getHourOfDay(),
                            startTime.getMinuteOfHour(),
                            false);
                    timePicker.setTitle("Select time");
                    timePicker.show();
                }
            }
        });

        buttonCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(editMeetingName)
                        && validate(editMeetingLocation)
                        && validate(editMeetingStartDate)
                        && validate(editMeetingStartTime)
                        && validate(editMeetingDuration)
                        && validate(editMeetingDescription)) { // TODO: validate fields in order they appear on the screen
                    Meeting m = Meeting.builder()
                            .name(editMeetingName.getText().toString().trim())
                            .location(editMeetingLocation.getText().toString().trim())
                            .description(editMeetingDescription.getText().toString().trim())
                            .startDate(startDate)
                            .startTime(startTime)
                            .durationMinutes(Integer.valueOf(editMeetingDuration.getText().toString())) // TODO: validate
                            .courseId(course.getId())
                            .members(getUserIDs(members))
                            .build();
                    meetingDAO.create(m);
                }
            }
        });
    }

    private boolean validate(EditText editText) { // TODO: refactor into utils
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
        user = intent.getParcelableExtra(EXTRA_USER);
        term = intent.getParcelableExtra(EXTRA_TERM);
        course = intent.getParcelableExtra(EXTRA_COURSE);
        now = DateTime.now();
        startDate = now.toLocalDate();
        startTime = now.toLocalTime();
        ArrayList<User> temp = intent.getParcelableArrayListExtra(EXTRA_SELECTED_USERS);
        if (temp != null) {
            updateMembers(temp);
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
        if (requestCode == DEFAULT_REQUEST_CODE && resultCode == DEFAULT_RESULT_CODE) {
            ArrayList<User> temp = data.getParcelableArrayListExtra(EXTRA_SELECTED_USERS);
            if (temp != null) {
                updateMembers(temp);
            }
        }
    }

    public void selectMembers(View view) {
        startUserSelectActivityForResult(this, user, term, course, members, DEFAULT_REQUEST_CODE);
    }

    private void updateMembers(List<User> users) {
        members = users;
//        textMeetingMembers.setText(joinStrings(getUserNames(members), "\n")); // TODO display users on UI
    }

}
