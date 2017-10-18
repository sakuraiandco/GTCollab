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
import sakuraiandco.com.gtcollab.domain.Subject;

/**
 * Created by kaliq on 10/17/2017.
 */

public class SubjectSearchAdapter extends RecyclerView.Adapter<SubjectSearchAdapter.SubjectViewHolder> {

    public interface Listener {
        void onClickSubjectAdapter(View view, int objectId);
    }

    private List<Subject> data;
    private Listener callback;

    public SubjectSearchAdapter(Listener callback) { this(new ArrayList<Subject>(), callback); }

    public SubjectSearchAdapter(List<Subject> data, Listener callback) {
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
        holder.setObjectId(s.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Subject> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textSubjectName;
        @Setter public int objectId;

        public SubjectViewHolder(View view) {
            super(view);
            textSubjectName = view.findViewById(R.id.text_subject_name);
            objectId = -1;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onClickSubjectAdapter(view, objectId);
        }
    }

}
