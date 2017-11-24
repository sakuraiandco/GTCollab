package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.bind(data.get(position));
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
        LinearLayout meetingDetailsExpanded;
        TextView textMeetingDescription;
        Button buttonProposeNewTimeLocation;
        Button buttonDeleteMeeting;
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
            meetingDetailsExpanded = view.findViewById(R.id.meeting_details_expanded);
            textMeetingDescription = view.findViewById(R.id.text_meeting_description);
            buttonProposeNewTimeLocation = view.findViewById(R.id.button_propose_new_time_location);
            buttonDeleteMeeting = view.findViewById(R.id.button_delete_meeting);
            meetingNumMembers = view.findViewById(R.id.meeting_num_members);
            checkboxMeeting = view.findViewById(R.id.checkbox_meeting);
            meetingDetailsShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MeetingViewHolder.this != currentExpanded) {
                        if (currentExpanded != null) {
                            currentExpanded.meetingDetailsExpanded.setVisibility(View.GONE);
                        }
                        meetingDetailsExpanded.setVisibility(View.VISIBLE);
                        currentExpanded = MeetingViewHolder.this;
                    } else {
                        meetingDetailsExpanded.setVisibility(View.GONE);
                        currentExpanded = null;
                    }
                }
            });
            buttonProposeNewTimeLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MeetingAdapterListener) callback).onButtonProposeNewTimeLocationClick(object);
                }
            });
            buttonDeleteMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MeetingAdapterListener) callback).onButtonDeleteMeetingClick(object);
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

        void bind(Meeting m) {
            boolean isMember = m.getMembers().contains(user.getId());
            boolean isCreator = m.getCreator().getId() == user.getId();
            textMeetingName.setText(m.getName());
            textMeetingLocation.setText(m.getLocation());
            textMeetingStartDate.setText(m.getStartDate().toString("EEE MMM dd"));
            textMeetingStartTime.setText(m.getStartTime().toString("h:mm a"));
            textMeetingDuration.setText(String.valueOf(m.getDurationMinutes() + "min"));
            textMeetingCreator.setText(m.getCreator().getFirstName() + " " + m.getCreator().getLastName()); // TODO: "Created by: you" if user is creator
            textMeetingNumMembers.setText(String.valueOf(m.getMembers().size()));
            textMeetingDescription.setText(m.getDescription());
            checkboxMeeting.setChecked(isMember);
            buttonProposeNewTimeLocation.setVisibility(isMember ? View.VISIBLE : View.GONE);
            buttonDeleteMeeting.setVisibility(isCreator ? View.VISIBLE : View.GONE);
            object = m;
        }

    }

}
