package sakuraiandco.com.gtcollab.temp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;

/**
 * Created by Alex on 10/14/17.
 */

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> implements Filterable {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView meetingTitle;
        public TextView meetingLocation;
        public TextView meetingTime;
        public CheckedTextView meetingMemberCount;

        public ViewHolder(View itemView) {
            super(itemView);

            meetingTitle = (TextView) itemView.findViewById(R.id.meetingTitle);
            meetingLocation = (TextView) itemView.findViewById(R.id.meetingLocation);
            meetingTime = (TextView) itemView.findViewById(R.id.meetingTime);
            meetingMemberCount = (CheckedTextView) itemView.findViewById(R.id.meetingMemberCount);

        }
    }
    private List<Meeting> meetings;
    private Context context;
    private List<Meeting> cachedMeetings;
    private Filter meetingFilter;

    public MeetingAdapter(Context context) {
        this(context, new ArrayList<Meeting>());
    }
    public MeetingAdapter(Context context, List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public MeetingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.meeting_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MeetingAdapter.ViewHolder viewHolder, int position) {
        final Meeting meeting = meetings.get(position);

        viewHolder.meetingTitle.setText(meeting.getName());
        viewHolder.meetingLocation.setText(meeting.getLocation());
        viewHolder.meetingTime.setText(meeting.getStartDate());
        viewHolder.meetingMemberCount.setText(String.format("%d members", meeting.getMembers().length()));

        boolean inMeeting = false;

        JSONArray members = meeting.getMembers();
        for (int i = 0; i < members.length(); i++) {
            try {
                if (members.getInt(i) == Singleton.getTempUser().getId()) {
                    inMeeting = true;
                    break;
                }
            } catch (JSONException error) {
                Log.e("error", error.toString());
            }
        }
        viewHolder.meetingMemberCount.setChecked(inMeeting);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
        this.notifyItemInserted(meetings.size() - 1);
    }

    public void cacheMeetings() {
        cachedMeetings = meetings;
    }

    @Override
    public Filter getFilter() {
        if (meetingFilter == null) {
            meetingFilter = new MeetingAdapter.MeetingFilter();
        }
        return meetingFilter;

    }

    private class MeetingFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Meeting> filterList = new ArrayList<>();
                for (Meeting meeting: cachedMeetings) {
                    if ((meeting.getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(meeting);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = cachedMeetings.size();
                results.values = cachedMeetings;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            meetings = (List) results.values;
            notifyDataSetChanged();
        }

    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

}