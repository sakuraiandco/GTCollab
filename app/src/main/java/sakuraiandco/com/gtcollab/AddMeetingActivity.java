package sakuraiandco.com.gtcollab;

import android.app.DatePickerDialog;
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

public class AddMeetingActivity extends AppCompatActivity {
    private int courseID;
    private ContextSingleton contextSingleton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        context = this;
        this.contextSingleton = ContextSingleton.getInstance(this.getApplicationContext());

        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID", -1);

//        final EditText dateText = (EditText) findViewById(R.id.createMeetingDate);
//
//        dateText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar currentDate = Calendar.getInstance();
//                int year=currentDate.get(Calendar.YEAR);
//                int month=currentDate.get(Calendar.MONTH);
//                int day=currentDate.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
//                        datepicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
//                            @Override
//                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                            }
//                        });
//                    }
//                },year, month, day);
//                datePicker.setTitle("Select date");
//                datePicker.show();  }
//            }
//        );


        // TODO: input checks for editTexts before submit
        // TODO: consider changing text entry to meeting end time rather than duration
        // TODO: popup datepicker and timepicker
        this.findViewById(R.id.createMeetingButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.createMeetingName)).getText().toString();
                String location = ((EditText) findViewById(R.id.createMeetingLocation)).getText().toString();
                String description = ((EditText) findViewById(R.id.createMeetingDescription)).getText().toString();
                String startDate = ((EditText) findViewById(R.id.createMeetingDate)).getText().toString();
                String startTime = ((EditText) findViewById(R.id.createMeetingTime)).getText().toString();
                int durationMinutes = Integer.parseInt(((TextView) findViewById(R.id.createMeetingDuration)).getText().toString());
                Meeting.create(name, location, description, startDate, startTime, durationMinutes, courseID, contextSingleton.getRequestQueue());
            }
        });
    }

}
