package sakuraiandco.com.gtcollab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddMeetingActivity extends AppCompatActivity {
    private int courseID;
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
        courseID = intent.getIntExtra("courseID", -1);

        final EditText dateText = (EditText) findViewById(R.id.createMeetingDate);
        final EditText timeText = (EditText) findViewById(R.id.createMeetingTime);


        // TODO: consider changing from editText to calendar
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;
                        dateText.setText(String.format("%d-%d-%d", year, month, day));
                    }
                }, year, month, day);
                datePicker.setTitle("Select date");
                datePicker.show();
            }
        });

        // TODO: consider making time text in pm and am or changing from editText to time
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        AddMeetingActivity.this.minute = minute;
                        timeText.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, hour, minute, false);
                timePicker.setTitle("Select time");
                timePicker.show();
            }
        });


        // TODO: input checks for editTexts before submit
        // TODO: consider changing text entry to meeting end time rather than duration
        // TODO: popup datepicker and timepicker
        this.findViewById(R.id.createMeetingButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.createMeetingName)).getText().toString();
                String location = ((EditText) findViewById(R.id.createMeetingLocation)).getText().toString();
                String description = ((EditText) findViewById(R.id.createMeetingDescription)).getText().toString();
                String startDate = dateText.getText().toString();
                String startTime = timeText.getText().toString();
                int durationMinutes = Integer.parseInt(((TextView) findViewById(R.id.createMeetingDuration)).getText().toString());
                Meeting.create(name, location, description, startDate, startTime, durationMinutes, courseID, contextSingleton.getRequestQueue());
            }
        });
    }

}
