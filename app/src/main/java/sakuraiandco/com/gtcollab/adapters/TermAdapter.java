package sakuraiandco.com.gtcollab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.TermAdapter.TermViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Term;

/**
 * Created by kaliq on 10/17/2017.
 */

public class TermAdapter extends BaseAdapter<Term, TermViewHolder> {

    @Setter Term currentTerm;

    public TermAdapter(AdapterListener<Term> callback) { this(new ArrayList<Term>(), callback); }

    public TermAdapter(List<Term> data, AdapterListener<Term> callback) {
        super(data, callback);
    }

    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TermViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term, parent, false));
    }

    class TermViewHolder extends BaseViewHolder<Term> {

        TextView textTermName;
        Term object;

        TermViewHolder(View view) {
            super(view);
            textTermName = view.findViewById(R.id.text_term_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(object);
                }
            });
        }

        @Override
        public void bind(Term t) {
            if (t.getId() == currentTerm.getId()) {
                textTermName.setText(t.getName() + " (current)");
            } else {
                textTermName.setText(t.getName());
            }
            object = t;
        }

    }

}
