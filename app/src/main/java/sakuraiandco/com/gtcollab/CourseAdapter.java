package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 10/14/17.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

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
    private List<Course> courses;
    private Context context;

    public CourseAdapter(Context context) {
        this(context, new ArrayList<Course>());
    }
    public CourseAdapter(Context context, List<Course> courses) {
        this.courses = courses;
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
        final Course course = courses.get(position);

        JSONArray members = course.getMembers();
        boolean inCourse = false;
        for (int i = 0; i < members.length(); i++) {
            try {
                if (members.getInt(i) == Singleton.getUser().getId()) {
                    inCourse = true;
                    viewHolder.joinClassButton.setText("View");
                    break;
                }
            } catch (JSONException error) {
                Log.e("error", error.toString());
            }
        }

        viewHolder.nameTextView.setText(course.getName());
        viewHolder.classMemberCount.setText(String.format("%d members", (course.getMembers().length())));
        viewHolder.joinClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.d("testing", String.format("joined %s", course.getName()));
                // TODO: access inCourse from within listener to avoid unnecessary call
                course.join(Singleton.getUser());
                Intent intent = new Intent(context, CoursePageActivity.class);
                intent.putExtra("courseID", course.getId());
                intent.putExtra("courseName", course.getName());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void addCourse(Course course) {
        courses.add(course);
        this.notifyItemInserted(courses.size() - 1);
    }
}
