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
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    @Getter
    private List<User> data;
    private AdapterListener<User> callback;

    public UserAdapter(AdapterListener<User> callback) { this(new ArrayList<User>(), callback); }

    public UserAdapter(List<User> data, AdapterListener<User> callback) {
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
        holder.user = u;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addData(List<User> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textUserName;
        User user;

        UserViewHolder(View view) {
            super(view);
            textUserName = view.findViewById(R.id.text_user_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(user);
                }
            });
        }
    }

}
