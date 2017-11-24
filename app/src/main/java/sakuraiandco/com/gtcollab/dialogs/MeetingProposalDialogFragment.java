package sakuraiandco.com.gtcollab.dialogs;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.MeetingProposal;

/**
 * Created by kaliq on 11/23/2017.
 */

public class MeetingProposalDialogFragment extends DialogFragment {

    public interface MeetingProposalDialogListener {
        void onMeetingProposalDialogSubmit(MeetingProposal meetingProposal);
    }

    MeetingProposalDialogListener listener;
    Meeting meeting;
    LocalDate startDate;
    LocalTime startTime;

    public static MeetingProposalDialogFragment newInstance(MeetingProposalDialogListener listener, Meeting meeting) {
        MeetingProposalDialogFragment f = new MeetingProposalDialogFragment();
        f.listener = listener;
        f.meeting = meeting;
        f.startDate = meeting.getStartDate();
        f.startTime = meeting.getStartTime();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_meeting_proposal, container, false);
        final TextView textErrorMessage = v.findViewById(R.id.text_error_message);
        final EditText editMeetingLocation = v.findViewById(R.id.edit_meeting_location);
        final EditText editMeetingStartDate = v.findViewById(R.id.edit_meeting_start_date);
        final EditText editMeetingStartTime = v.findViewById(R.id.edit_meeting_start_time);
        Button buttonCancelMeetingProposal = v.findViewById(R.id.button_cancel_meeting_proposal);
        Button buttonCreateMeetingProposal = v.findViewById(R.id.button_create_meeting_proposal);

        editMeetingLocation.setHint(meeting.getLocation());
        editMeetingStartDate.setHint(startDate.toString("EEE MMM dd"));
        editMeetingStartTime.setHint(startTime.toString("h:mm a"));

        Log.d("TEMP_TEST", ":" + startDate.getYear() + " " + startDate.getMonthOfYear() + " " + startDate.getDayOfMonth());

        editMeetingStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
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
                    TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
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

        buttonCancelMeetingProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeetingProposalDialogFragment.this.dismiss();
            }
        });

        buttonCreateMeetingProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate(editMeetingLocation)
                        || validate(editMeetingStartDate)
                        || validate(editMeetingStartTime)) { // TODO: validate fields in order they appear on the screen
                    MeetingProposal mp = MeetingProposal.builder()
                            .meetingId(meeting.getId())
                            .location(editMeetingLocation.getText().toString().trim())
                            .startDate(startDate)
                            .startTime(startTime)
                            .build();
                    listener.onMeetingProposalDialogSubmit(mp);
                    MeetingProposalDialogFragment.this.dismiss();
                } else {
                    textErrorMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        return v;
    }

    private boolean validate(EditText editText) { // TODO: refactor into utils
        return !editText.getText().toString().trim().isEmpty();
    }

}
