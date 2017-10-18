package sakuraiandco.com.gtcollab;

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

import java.util.Date;
import java.util.List;

import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;

public class CreateMeetingActivity extends AppCompatActivity implements DAOListener<Meeting> {

    String courseId;

    MeetingDAO meetingDAO;

    EditText editMeetingName;
    EditText editMeetingLocation;
    EditText editMeetingDescription;
    EditText editMeetingStartDate;
    EditText editMeetingStartTime;
    EditText editMeetingDuration;
    Button buttonCreateMeeting;

    DateTime now;
    LocalDate startDate;
    LocalTime startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        // toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        meetingDAO = new MeetingDAO(this);

        // TODO
//        String name;
//        String location;
//        String description;
//        LocalDate startDate;
//        LocalTime startTime;
//        int durationMinutes;
//        List<Integer> members; TODO: invite other users?

        // handle intent
        handleIntent(getIntent());

        // view
        editMeetingName = (EditText) findViewById(R.id.edit_meeting_name);
        editMeetingLocation = (EditText) findViewById(R.id.edit_meeting_location);
        editMeetingDescription = (EditText) findViewById(R.id.edit_meeting_description);
        editMeetingStartDate = (EditText) findViewById(R.id.edit_meeting_start_date);
        editMeetingStartTime = (EditText) findViewById(R.id.edit_meeting_start_time);
        editMeetingDuration = (EditText) findViewById(R.id.edit_meeting_duration);
        buttonCreateMeeting = (Button) findViewById(R.id.button_create_meeting);

        editMeetingStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePicker = new DatePickerDialog(
                            CreateMeetingActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    startDate = new LocalDate(year, month, dayOfMonth);
                                    editMeetingStartDate.setText(startDate.toString("EEE MMM dd"));
                                    editMeetingStartDate.setError(null);
                                }
                            },
                            startDate.getYear(),
                            startDate.getMonthOfYear(),
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
                            .courseId(Integer.valueOf(courseId))
                            .build();
                    meetingDAO.create(m);
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
        now = DateTime.now();
        startDate = now.toLocalDate();
        startTime = now.toLocalTime();
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
    public void onListReady(List<Meeting> meetings) {}

    @Override
    public void onObjectReady(Meeting meeting) {
        Toast.makeText(this, "Created new meeting: " + meeting.getName(), Toast.LENGTH_SHORT).show();
        Intent courseActivityIntent = new Intent(CreateMeetingActivity.this, CourseActivity.class);
        courseActivityIntent.putExtra(COURSE_ID, courseId);
        courseActivityIntent.putExtra(COURSE_TAB, TAB_MEETINGS);
        startActivity(courseActivityIntent);
    }

    @Override
    public void onDAOError(BaseDAO.Error error) {
        Toast.makeText(this, "MeetingDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling - handle server-side validation error
    }

}
