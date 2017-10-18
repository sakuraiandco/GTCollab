package sakuraiandco.com.gtcollab.temp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> implements Filterable{

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button joinClassButton;
        public TextView classMemberCount;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            joinClassButton = (Button) itemView.findViewById(R.id.join_class_button);
            classMemberCount = (TextView) itemView.findViewById(R.id.class_member_count);


        }
    }
    private List<TempCourse> tempCourses;
    private Context context;
    private List<TempCourse> cachedTempCourses;
    private CourseFilter courseFilter;

    public CourseAdapter(Context context) {
        this(context, new ArrayList<TempCourse>());
    }
    public CourseAdapter(Context context, List<TempCourse> tempCourses) {
        this.tempCourses = tempCourses;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.course_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder viewHolder, int position) {
        final TempCourse tempCourse = tempCourses.get(position);

        JSONArray members = tempCourse.getMembers();
        for (int i = 0; i < members.length(); i++) {
            try {
                if (members.getInt(i) == Singleton.getTempUser().getId()) {
                    viewHolder.joinClassButton.setText("View");
                    break;
                }
            } catch (JSONException error) {
                Log.e("error", error.toString());
            }
        }

        viewHolder.nameTextView.setText(tempCourse.getName());
        viewHolder.classMemberCount.setText(String.format("%d members", (tempCourse.getMembers().length())));
        viewHolder.joinClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("testing", String.format("joined %s", tempCourse.getName()));
                // TODO: access inCourse from within listener to avoid unnecessary call
                tempCourse.join(Singleton.getTempUser());
                Intent intent = new Intent(context, CoursePageActivity.class);
                intent.putExtra("courseID", tempCourse.getId());
                intent.putExtra("courseName", tempCourse.getName());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tempCourses.size();
    }

    public void addCourse(TempCourse tempCourse) {
        tempCourses.add(tempCourse);
        this.notifyItemInserted(tempCourses.size() - 1);
    }

    public void cacheCourses() {
        cachedTempCourses = tempCourses;
    }

    public void restoreCoursesFromCache() {
        tempCourses = cachedTempCourses;
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (courseFilter == null) {
            courseFilter = new CourseFilter();
        }
        return courseFilter;

    }

    private class CourseFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<TempCourse> filterList = new ArrayList<>();
                for (TempCourse tempCourse : cachedTempCourses) {
                    if ((tempCourse.getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(tempCourse);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = cachedTempCourses.size();
                results.values = cachedTempCourses;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            tempCourses = (List) results.values;
            notifyDataSetChanged();
        }

    }
}
