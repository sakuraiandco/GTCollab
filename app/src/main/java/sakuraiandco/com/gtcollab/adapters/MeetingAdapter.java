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

import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Meeting;

/**
 * Created by kaliq on 10/17/2017.
 */

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    public interface Listener {
        void onMeetingCheckboxClick(View v, int objectId);
        void onMeetingMembersClick(View v, int objectId, String meetingName);
    }

    private List<Meeting> data;
    private Listener callback;
    private String userId;
    private MeetingViewHolder currentExpanded;

    public MeetingAdapter(Listener callback, String userId) { this(new ArrayList<Meeting>(), callback, userId); }

    public MeetingAdapter(List<Meeting> data, Listener callback, String userId) {
        this.data = data;
        this.callback = callback;
        this.userId = userId;
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
        holder.textMeetingDescription.setText(m.getDescription());
        holder.checkboxMeeting.setChecked(m.getMembers().contains(Integer.valueOf(userId)));
        holder.setObjectId(m.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Meeting> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout meetingDetailsShort;
        public TextView textMeetingName;
        public TextView textMeetingLocation;
        public TextView textMeetingStartDate;
        public TextView textMeetingStartTime;
        public TextView textMeetingDuration;
        public TextView textMeetingCreator;
        public TextView textMeetingNumMembers;
        public TextView textMeetingDescription;
        public LinearLayout meetingNumMembers;
        public CheckBox checkboxMeeting;
        @Setter public int objectId;

        public MeetingViewHolder(View view) {
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
            objectId = -1;
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
                    callback.onMeetingMembersClick(v, objectId, textMeetingName.getText().toString());
                }
            });
            checkboxMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onMeetingCheckboxClick(v, objectId);
                }
            });
        }

    }

}
