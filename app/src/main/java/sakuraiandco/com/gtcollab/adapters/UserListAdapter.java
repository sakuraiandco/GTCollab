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
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    public interface Listener {
        void onClickUserAdapter(View view, int objectId);
    }

    private List<User> data;
    private Listener callback;

    public UserListAdapter(Listener callback) { this(new ArrayList<User>(), callback); }

    public UserListAdapter(List<User> data, Listener callback) {
        this.data = data;
        this.callback = callback;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User u = data.get(position);
        holder.textUserName.setText(u.getFirstName() + " " + u.getLastName());
        holder.setObjectId(u.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textUserName;
        @Setter public int objectId;

        public UserViewHolder(View view) {
            super(view);
            textUserName = view.findViewById(R.id.text_user_name);
            objectId = -1;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            callback.onClickUserAdapter(view, objectId);
        }
    }

}
