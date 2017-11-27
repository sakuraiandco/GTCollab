package sakuraiandco.com.gtcollab.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.MeetingAdapter.MeetingViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class MeetingAdapter extends BaseAdapter<Meeting, MeetingViewHolder> {

    private Context context;
    private User user;

    public MeetingAdapter(Context context, MeetingAdapterListener callback) { this(context, callback, null); }

    public MeetingAdapter(Context context, MeetingAdapterListener callback, User user) { this(context, new ArrayList<Meeting>(), callback, user); }

    public MeetingAdapter(Context context, List<Meeting> data, MeetingAdapterListener callback, User user) {
        super(data, callback);
        this.context = context;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting, parent, false));
    }

    class MeetingViewHolder extends BaseViewHolder<Meeting> {

        LinearLayout meetingDetailsShort;

        LinearLayout rowMeetingName;
        TextView textMeetingName;
        LinearLayout meetingMembersWrapper;
        ImageView iconCheck;
        ImageView iconMembers;
        TextView textMeetingNumMembers;

        LinearLayout rowMeetingDateTimeDuration;
        TextView textMeetingStartDate;
        TextView textMeetingStartTime;
        TextView textMeetingDuration;
        TextView textJoin;

        LinearLayout rowMeetingLocationMembers;
        TextView textMeetingLocation;

        LinearLayout meetingDetailsExpanded;

        TextView textMeetingCreator;

        TextView textMeetingDescription;

        LinearLayout rowMeetingButtons;
        Button buttonProposeNewTimeLocation;
        Button buttonDeleteMeeting;
        Button buttonMeetingMembers;

        Meeting object;

        boolean selected;
        boolean expanded;

        MeetingViewHolder(View view) {
            super(view);
            meetingDetailsShort = view.findViewById(R.id.meeting_container);
            meetingDetailsExpanded = view.findViewById(R.id.meeting_container_expanded);
            rowMeetingName = view.findViewById(R.id.row_meeting_name);
            meetingMembersWrapper = view.findViewById(R.id.meeting_members_wrapper);
            iconCheck = view.findViewById(R.id.icon_check);
            iconMembers = view.findViewById(R.id.icon_members);
            rowMeetingDateTimeDuration = view.findViewById(R.id.row_meeting_date_time_duration);
            rowMeetingLocationMembers = view.findViewById(R.id.row_meeting_location_members);
            textMeetingName = view.findViewById(R.id.text_meeting_name);
            textMeetingLocation = view.findViewById(R.id.text_meeting_location);
            textMeetingStartDate = view.findViewById(R.id.text_meeting_start_date);
            textMeetingStartTime = view.findViewById(R.id.text_meeting_start_time);
            textMeetingDuration = view.findViewById(R.id.text_meeting_duration);
            textJoin = view.findViewById(R.id.text_join);
            textMeetingCreator = view.findViewById(R.id.text_meeting_creator);
            textMeetingNumMembers = view.findViewById(R.id.text_meeting_num_members);
            textMeetingDescription = view.findViewById(R.id.text_meeting_description);
            rowMeetingButtons = view.findViewById(R.id.row_meeting_buttons);
            buttonProposeNewTimeLocation = view.findViewById(R.id.button_propose_new_time_location);
            buttonDeleteMeeting = view.findViewById(R.id.button_delete_meeting);
            buttonMeetingMembers = view.findViewById(R.id.button_meeting_members);
            textMeetingName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expanded) {
                        expanded = false;
                        meetingDetailsExpanded.setVisibility(View.GONE);
                    } else {
                        expanded = true;
                        meetingDetailsExpanded.setVisibility(View.VISIBLE);
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
            buttonMeetingMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MeetingAdapterListener) callback).onMeetingMembersClick(object);
                }
            });
            meetingMembersWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selected) {
                        selected = false;
                        textMeetingNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorDark));
                        iconMembers.setImageResource(R.drawable.ic_people_dark_24dp);
                        iconCheck.setVisibility(View.GONE);
                        textJoin.setVisibility(View.VISIBLE);
                    } else {
                        selected = true;
                        textMeetingNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        iconMembers.setImageResource(R.drawable.ic_people_accent_24dp);
                        iconCheck.setVisibility(View.VISIBLE);
                        textJoin.setVisibility(View.GONE);
                    }
                    ((MeetingAdapterListener) callback).onMeetingCheckboxClick(object, selected);
                }
            });

        }

        @Override
        public void bind(Meeting m) {
            boolean isMember = m.getMembers().contains(user.getId());
            boolean isCreator = m.getCreator().getId() == user.getId();
            if (isMember) {
                selected = true;
                textMeetingNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                iconMembers.setImageResource(R.drawable.ic_people_accent_24dp);
                iconCheck.setVisibility(View.VISIBLE);
                textJoin.setVisibility(View.GONE);
            } else {
                selected = false;
                textMeetingNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorDark));
                iconMembers.setImageResource(R.drawable.ic_people_dark_24dp);
                iconCheck.setVisibility(View.GONE);
                textJoin.setVisibility(View.VISIBLE);
            }
            expanded = false;
            meetingDetailsExpanded.setVisibility(View.GONE);
            textMeetingName.setText(m.getName());
            textMeetingLocation.setText(m.getLocation());
            textMeetingStartDate.setText(m.getStartDate().toString("EEE MMM dd"));
            textMeetingStartTime.setText(m.getStartTime().toString("h:mm a"));
            textMeetingDuration.setText(String.valueOf(m.getDurationMinutes() + "min"));
            textMeetingCreator.setText(m.getCreator().getFullName()); // TODO: "Created by: you" if user is creator
            textMeetingNumMembers.setText(String.valueOf(m.getMembers().size()));
            textMeetingDescription.setText(m.getDescription());
            buttonProposeNewTimeLocation.setVisibility(isMember ? View.VISIBLE : View.GONE);
            buttonDeleteMeeting.setVisibility(isCreator ? View.VISIBLE : View.GONE);

//            meetingDetailsExpanded.setVisibility((this == currentExpanded) ? View.VISIBLE : View.GONE);

            object = m;
        }

    }

}
