package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Course;

/**
 * Created by kaliq on 10/17/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {

    public interface Listener {
        void onClickCourseAdapter(View view, int objectId);
    }

    private List<Course> data;
    private Listener callback;

    public CourseListAdapter(Listener callback) { this(new ArrayList<Course>(), callback); }

    public CourseListAdapter(List<Course> data, Listener callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false));
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course c = data.get(position);
        holder.textCourseShortName.setText(c.getSubjectCode() + " " + c.getCourseNumber());
        holder.textCourseLongName.setText(c.getName());
        holder.textCourseNumMembers.setText(String.valueOf(c.getNumMembers()));
        holder.setObjectId(c.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Course> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textCourseShortName;
        public TextView textCourseLongName;
        public TextView textCourseNumMembers;
        @Setter public int objectId;

        public CourseViewHolder(View view) {
            super(view);
            textCourseShortName = view.findViewById(R.id.text_course_short_name);
            textCourseLongName = view.findViewById(R.id.text_course_long_name);
            textCourseNumMembers = view.findViewById(R.id.text_course_num_members);
            objectId = -1;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onClickCourseAdapter(view, objectId);
        }
    }

}
