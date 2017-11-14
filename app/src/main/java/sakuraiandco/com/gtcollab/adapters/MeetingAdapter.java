package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.MeetingAdapter.MeetingViewHolder;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class MeetingAdapter extends BaseAdapter<Meeting, MeetingViewHolder> {

    private User user;
    private MeetingViewHolder currentExpanded;

    public MeetingAdapter(MeetingAdapterListener callback, User user) { this(new ArrayList<Meeting>(), callback, user); }

    public MeetingAdapter(List<Meeting> data, MeetingAdapterListener callback, User user) {
        super(data, callback);
        this.user = user;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false));
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        Meeting m = data.get(position);
        holder.textMeetingName.setText(m.getName());
        holder.textMeetingLocation.setText(m.getLocation());
        holder.textMeetingStartDate.setText(m.getStartDate().toString("EEE MMM dd"));
        holder.textMeetingStartTime.setText(m.getStartTime().toString("h:mm a"));
        holder.textMeetingDuration.setText(String.valueOf(m.getDurationMinutes() + "min"));
        holder.textMeetingCreator.setText(m.getCreator().getFirstName() + " " + m.getCreator().getLastName());
        holder.textMeetingNumMembers.setText(String.valueOf(m.getMembers().size()));
        holder.checkboxMeeting.setChecked(m.getMembers().contains(user.getId()));
        holder.object = m;
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder {

        LinearLayout meetingDetailsShort;
        TextView textMeetingName;
        TextView textMeetingLocation;
        TextView textMeetingStartDate;
        TextView textMeetingStartTime;
        TextView textMeetingDuration;
        TextView textMeetingCreator;
        TextView textMeetingNumMembers;
        TextView textMeetingDescription;
        LinearLayout meetingNumMembers;
        CheckBox checkboxMeeting;
        Meeting object;

        MeetingViewHolder(View view) {
            super(view);
            meetingDetailsShort = view.findViewById(R.id.meeting_details_short);
            textMeetingName = view.findViewById(R.id.text_meeting_name);
            textMeetingLocation = view.findViewById(R.id.text_meeting_location);
            textMeetingStartDate = view.findViewById(R.id.text_meeting_start_date);
            textMeetingStartTime = view.findViewById(R.id.text_meeting_start_time);
            textMeetingDuration = view.findViewById(R.id.text_meeting_duration);
            textMeetingCreator = view.findViewById(R.id.text_meeting_creator);
            textMeetingNumMembers = view.findViewById(R.id.text_meeting_num_members);
            textMeetingDescription = view.findViewById(R.id.text_meeting_description);
            meetingNumMembers = view.findViewById(R.id.meeting_num_members);
            checkboxMeeting = view.findViewById(R.id.checkbox_meeting);
            meetingDetailsShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MeetingViewHolder.this != currentExpanded) {
                        if (currentExpanded != null) {
                            currentExpanded.textMeetingDescription.setVisibility(View.GONE);
                        }
                        textMeetingDescription.setVisibility(View.VISIBLE);
                        currentExpanded = MeetingViewHolder.this;
                    } else {
                        textMeetingDescription.setVisibility(View.GONE);
                        currentExpanded = null;
                    }
                }
            });
            meetingNumMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MeetingAdapterListener) callback).onMeetingMembersClick(object);
                }
            });
            checkboxMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MeetingAdapterListener) callback).onMeetingCheckboxClick(object, ((CheckBox) v).isChecked());
                }
            });
        }

    }

}
