package sakuraiandco.com.gtcollab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.UserListAdapter.UserViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
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

    class UserViewHolder extends BaseViewHolder<User> {

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

        @Override
        public void bind(User u) {
            textUserName.setText(u.getFirstName() + " " + u.getLastName());
            object = u;
        }

    }

}
