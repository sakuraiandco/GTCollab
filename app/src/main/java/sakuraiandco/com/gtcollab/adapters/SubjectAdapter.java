package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Subject;

/**
 * Created by kaliq on 10/17/2017.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    @Getter
    private List<Subject> data;
    private AdapterListener<Subject> callback;

    public SubjectAdapter(AdapterListener<Subject> callback) { this(new ArrayList<Subject>(), callback); }

    public SubjectAdapter(List<Subject> data, AdapterListener<Subject> callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubjectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false));
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {
        Subject s = data.get(position);
        holder.textSubjectName.setText(s.getName());
        holder.subject = s;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Subject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<Subject> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView textSubjectName;
        Subject subject;

        SubjectViewHolder(View view) {
            super(view);
            textSubjectName = view.findViewById(R.id.text_subject_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(subject);
                }
            });
        }
    }

}
