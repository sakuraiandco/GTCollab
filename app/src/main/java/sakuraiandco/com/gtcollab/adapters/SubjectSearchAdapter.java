package sakuraiandco.com.gtcollab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.SubjectSearchAdapter.SubjectViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Subject;

/**
 * Created by kaliq on 10/17/2017.
 */

public class SubjectSearchAdapter extends BaseAdapter<Subject, SubjectViewHolder> {

    public SubjectSearchAdapter(AdapterListener<Subject> callback) { this(new ArrayList<Subject>(), callback); }

    public SubjectSearchAdapter(List<Subject> data, AdapterListener<Subject> callback) {
        super(data, callback);
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false));
    }

    class SubjectViewHolder extends BaseViewHolder<Subject> {

        TextView textSubjectName;
        Subject object;

        SubjectViewHolder(View view) {
            super(view);
            textSubjectName = view.findViewById(R.id.text_subject_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(object);
                }
            });
        }

        @Override
        public void bind(Subject s) {
            textSubjectName.setText(s.getName());
            object = s;
        }

    }

}
