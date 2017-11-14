package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.UserListAdapter.UserViewHolder;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class UserListAdapter extends BaseAdapter<User, UserViewHolder> {

    public UserListAdapter(AdapterListener<User> callback) { this(new ArrayList<User>(), callback); }

    public UserListAdapter(List<User> data, AdapterListener<User> callback) {
        super(data, callback);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User u = data.get(position);
        holder.textUserName.setText(u.getFirstName() + " " + u.getLastName());
        holder.object = u;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textUserName;
        User object;

        UserViewHolder(View view) {
            super(view);
            textUserName = view.findViewById(R.id.text_user_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(object);
                }
            });
        }
    }

}
