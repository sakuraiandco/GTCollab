package sakuraiandco.com.gtcollab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.CourseListAdapter.CourseViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Course;

/**
 * Created by kaliq on 10/17/2017.
 */

public class CourseListAdapter extends BaseAdapter<Course, CourseViewHolder> {

    public CourseListAdapter(AdapterListener<Course> callback) { this(new ArrayList<Course>(), callback); }

    public CourseListAdapter(List<Course> data, AdapterListener<Course> callback) {
        super(data, callback);
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false));
    }

    class CourseViewHolder extends BaseViewHolder<Course> {

        TextView textCourseShortName;
        TextView textCourseLongName;
        TextView textCourseNumMembers;
        Course object;

        CourseViewHolder(View view) {
            super(view);
            textCourseShortName = view.findViewById(R.id.text_course_short_name);
            textCourseLongName = view.findViewById(R.id.text_course_long_name);
            textCourseNumMembers = view.findViewById(R.id.text_course_num_members);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(object);
                }
            });
        }

        @Override
        public void bind(Course c) {
            textCourseShortName.setText(c.getSubjectCode() + " " + c.getCourseNumber());
            textCourseLongName.setText(c.getName());
            textCourseNumMembers.setText(String.valueOf(c.getNumMembers()));
            object = c;
        }

    }

}
