package sakuraiandco.com.gtcollab.temp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import sakuraiandco.com.gtcollab.R;

public class AddMeetingActivity extends AppCompatActivity {
    private int courseID;
    private String courseName;
    private ContextSingleton contextSingleton;
    private Context context;
    Calendar currentDate = Calendar.getInstance();

    // datePickerDialog params
    private int year=currentDate.get(Calendar.YEAR);
    private int month=currentDate.get(Calendar.MONTH);
    private int day=currentDate.get(Calendar.DAY_OF_MONTH);
    private int hour = currentDate.get(Calendar.HOUR_OF_DAY);
    private int minute = currentDate.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        context = this;
        this.contextSingleton = ContextSingleton.getInstance(this.getApplicationContext());

        Intent intent = getIntent();
//        courseID = intent.getIntExtra("courseID", -1);
//        courseName = intent.getStringExtra("courseName");
        courseID = 1;
        courseName = "Temp Course";

        final EditText dateText = (EditText) findViewById(R.id.createMeetingDate);
        final EditText timeText = (EditText) findViewById(R.id.createMeetingTime);


        // TODO: consider changing from editText to calendar
        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                            year = selectedYear;
                            month = selectedMonth;
                            day = selectedDay;
                            dateText.setText(String.format("%d-%d-%d", year, month, day));
                            dateText.setError(null);
                        }
                    }, year, month, day);
                    datePicker.setTitle("Select date");
                    datePicker.show();
                }
            }
        });

        // TODO: consider making time text in pm and am or changing from editText to time
        timeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            hour = hourOfDay;
                            AddMeetingActivity.this.minute = minute;
                            timeText.setText(String.format("%d:%d", hourOfDay, minute));
                            timeText.setError(null);
                        }
                    }, hour, minute, false);
                    timePicker.setTitle("Select time");
                    timePicker.show();
                }
            }
        });


        // TODO: consider changing text entry to meeting end time rather than duration
        // TODO: popup datepicker and timepicker
        this.findViewById(R.id.createMeetingButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.createMeetingName);
                EditText locationText = (EditText) findViewById(R.id.createMeetingLocation);
                EditText descriptionText = (EditText) findViewById(R.id.createMeetingDescription);
                EditText durationText = (EditText) findViewById(R.id.createMeetingDuration);

                String name = nameText.getText().toString().trim();
                String location = locationText.getText().toString().trim();
                String description = descriptionText.getText().toString().trim();
                String startDate = dateText.getText().toString().trim();
                String startTime = timeText.getText().toString().trim();
                String durationString  = durationText.getText().toString();

                boolean cancel = false;

                if (TextUtils.isEmpty(name)) {
                    nameText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }
                if (TextUtils.isEmpty(location)) {
                    locationText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }
                if (TextUtils.isEmpty(description)) {
                    descriptionText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }
                if (TextUtils.isEmpty(startDate)) {
                    dateText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }
                if (TextUtils.isEmpty(startTime)){
                    timeText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }
                if (TextUtils.isEmpty(durationString)) {
                    durationText.setError(getString(R.string.error_field_required));
                    cancel = true;
                }

                if (!cancel) {
                    int durationMinutes = Integer.parseInt(durationString);
                    Meeting.create(name, location, description, startDate, startTime, durationMinutes, courseID, contextSingleton.getRequestQueue());
                    Intent intent = new Intent(context, CoursePageActivity.class);
                    intent.putExtra("courseID", courseID);
                    intent.putExtra("courseName", courseName);
                    context.startActivity(intent);
                }
            }
        });
    }

}
