package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.GroupAdapter.GroupViewHolder;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class GroupAdapter extends BaseAdapter<Group, GroupViewHolder> {

    private User user;
    private GroupViewHolder currentExpanded;

    public GroupAdapter(GroupAdapterListener callback, User user) { this(new ArrayList<Group>(), callback, user); }

    public GroupAdapter(List<Group> data, GroupAdapterListener callback, User user) {
        super(data, callback);
        this.user = user;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        Group g = data.get(position);
        holder.textGroupName.setText(g.getName());
        holder.textGroupCreator.setText(g.getCreator().getFirstName() + " " + g.getCreator().getLastName());
        holder.textGroupNumMembers.setText(String.valueOf(g.getMembers().size()));
        holder.checkboxGroup.setChecked(g.getMembers().contains(user.getId()));
        holder.group = g;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        LinearLayout groupDetailsShort;
        TextView textGroupName;
        TextView textGroupCreator;
        TextView textGroupNumMembers;
        LinearLayout groupNumMembers;
        CheckBox checkboxGroup;
        Group group;

        GroupViewHolder(View view) {
            super(view);
            groupDetailsShort = view.findViewById(R.id.group_details_short);
            textGroupName = view.findViewById(R.id.text_group_name);
            textGroupCreator = view.findViewById(R.id.text_group_creator);
            textGroupNumMembers = view.findViewById(R.id.text_group_num_members);
            groupNumMembers = view.findViewById(R.id.group_num_members);
            checkboxGroup = view.findViewById(R.id.checkbox_group);
            groupDetailsShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(group);
                }
            });
            groupNumMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GroupAdapterListener) callback).onGroupMembersClick(group);
                }
            });
            checkboxGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GroupAdapterListener) callback).onGroupCheckboxClick(group, ((CheckBox) v).isChecked());
                }
            });
        }
    }

}
