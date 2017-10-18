package sakuraiandco.com.gtcollab.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.domain.Group;

/**
 * Created by kaliq on 10/17/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public interface Listener {
        void onGroupCheckboxClick(View v, int objectId);
        void onGroupMembersClick(View v, int objectId, String groupName);
    }

    private List<Group> data;
    private Listener callback;
    private String userId;
    private GroupViewHolder currentExpanded;

    public GroupAdapter(Listener callback, String userId) { this(new ArrayList<Group>(), callback, userId); }

    public GroupAdapter(List<Group> data, Listener callback, String userId) {
        this.data = data;
        this.callback = callback;
        this.userId = userId;
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
        holder.checkboxGroup.setChecked(g.getMembers().contains(Integer.valueOf(userId)));
        holder.setObjectId(g.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Group> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout groupDetailsShort;
        public TextView textGroupName;
        public TextView textGroupCreator;
        public TextView textGroupNumMembers;
        public TextView textGroupDescription;
        public LinearLayout groupNumMembers;
        public CheckBox checkboxGroup;
        @Setter public int objectId;

        public GroupViewHolder(View view) {
            super(view);
            groupDetailsShort = view.findViewById(R.id.group_details_short);
            textGroupName = view.findViewById(R.id.text_group_name);
            textGroupCreator = view.findViewById(R.id.text_group_creator);
            textGroupNumMembers = view.findViewById(R.id.text_group_num_members);
            textGroupDescription = view.findViewById(R.id.text_group_description);
            groupNumMembers = view.findViewById(R.id.group_num_members);
            checkboxGroup = view.findViewById(R.id.checkbox_group);
            objectId = -1;
            groupDetailsShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GroupViewHolder.this != currentExpanded) {
                        if (currentExpanded != null) {
                            currentExpanded.textGroupDescription.setVisibility(View.GONE);
                        }
                        textGroupDescription.setVisibility(View.VISIBLE);
                        currentExpanded = GroupViewHolder.this;
                    } else {
                        textGroupDescription.setVisibility(View.GONE);
                        currentExpanded = null;
                    }
                }
            });
            groupNumMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onGroupMembersClick(v, objectId, textGroupName.getText().toString());
                }
            });
            checkboxGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onGroupCheckboxClick(v, objectId);
                }
            });
        }
    }

}
