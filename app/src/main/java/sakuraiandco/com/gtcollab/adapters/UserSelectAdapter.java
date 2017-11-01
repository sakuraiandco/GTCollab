package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class UserSelectAdapter extends RecyclerView.Adapter<UserSelectAdapter.UserViewHolder> {

    private List<User> data;
    private List<Integer> selected;

    public UserSelectAdapter() { this(new ArrayList<User>()); }

    public UserSelectAdapter(List<User> data) {
        this.data = data;
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
        holder.setObjectId(u.getId());
        holder.setObject(u);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<Integer> getSelected() {
        return selected;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView checkedTextUserName;
        @Setter public Integer objectId;
        @Setter public User object;
        boolean isSelected;

        public UserViewHolder(View view) {
            super(view);
            checkedTextUserName = view.findViewById(R.id.checked_text_user_name);
            objectId = -1;
            isSelected = false;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkedTextUserName.isChecked()) {
                        selected.remove(UserViewHolder.this.objectId);
                        checkedTextUserName.setChecked(false);
                        checkedTextUserName.setCheckMarkDrawable(null);
                    } else {
                        selected.add(UserViewHolder.this.objectId);
                        checkedTextUserName.setChecked(true);
                        checkedTextUserName.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                    }
                }
            });
        }

    }

}
