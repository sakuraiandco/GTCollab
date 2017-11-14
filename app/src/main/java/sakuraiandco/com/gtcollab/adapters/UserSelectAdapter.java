package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.UserSelectAdapter.UserViewHolder;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class UserSelectAdapter extends BaseAdapter<User, UserViewHolder> {

    @Getter @Setter private List<User> selected;

    public UserSelectAdapter() { this(new ArrayList<User>()); }

    public UserSelectAdapter(List<User> data) {
        super(data, null);
        this.selected = new ArrayList<>();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_select, parent, false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User u = data.get(position);
        holder.checkedTextUserName.setText(u.getFirstName() + " " + u.getLastName());
        holder.object = u;
        if (selected.contains(u)) {
            holder.checkedTextUserName.setChecked(true);
            holder.checkedTextUserName.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
        } else {
            holder.checkedTextUserName.setChecked(false);
            holder.checkedTextUserName.setCheckMarkDrawable(null);
        }
    }

    public void addSelected(User u) {
        selected.add(u);
        notifyDataSetChanged(); // TODO: this should call onBindViewHolder() and update checkmark?
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView checkedTextUserName;
        User object;

        UserViewHolder(View view) {
            super(view);
            checkedTextUserName = view.findViewById(R.id.checked_text_user_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkedTextUserName.isChecked()) {
                        selected.remove(object);
                        checkedTextUserName.setChecked(false);
                        checkedTextUserName.setCheckMarkDrawable(null);
                    } else {
                        selected.add(object); // TODO: check duplicates?
                        checkedTextUserName.setChecked(true);
                        checkedTextUserName.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    }
                }
            });
        }

    }

}
