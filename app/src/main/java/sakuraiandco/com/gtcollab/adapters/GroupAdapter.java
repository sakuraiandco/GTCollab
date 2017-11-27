package sakuraiandco.com.gtcollab.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.GroupAdapter.GroupViewHolder;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.User;

/**
 * Created by kaliq on 10/17/2017.
 */

public class GroupAdapter extends BaseAdapter<Group, GroupViewHolder> {

    private User user;
    private int expandedGroupId;
    private Context context;
    boolean selected;

    public GroupAdapter(Context context, GroupAdapterListener callback) { this(context, callback, null); }

    public GroupAdapter(Context context, GroupAdapterListener callback, User user) { this(context, new ArrayList<Group>(), callback, user); }

    public GroupAdapter(Context context, List<Group> data, GroupAdapterListener callback, User user) {
        super(data, callback);
        this.context = context;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
    }

    class GroupViewHolder extends BaseViewHolder<Group> {

        LinearLayout groupDetailsShort;
        LinearLayout groupDetailsContainer;

        TextView textGroupName;
        TextView textGroupCreator;
        TextView textGroupNumMembers;

        Group object;

        ImageView iconCheck;
        ImageView iconMembers;

        Button buttonDeleteGroup;
        Button buttonJoinGroup;
        Button buttonLeaveGroup;
        Button buttonGroupMembers;
        Button buttonOpenChat;

        GroupViewHolder(View view) {
            super(view);
            groupDetailsShort = view.findViewById(R.id.group_details_short);
            textGroupName = view.findViewById(R.id.text_group_name);
            textGroupCreator = view.findViewById(R.id.text_group_creator);
            textGroupNumMembers = view.findViewById(R.id.text_group_num_members);
            groupDetailsContainer = view.findViewById(R.id.group_container_expanded);

            iconCheck = view.findViewById(R.id.icon_check);
            iconMembers = view.findViewById(R.id.icon_members);

            buttonDeleteGroup = view.findViewById(R.id.button_delete_group);
            buttonJoinGroup = view.findViewById(R.id.button_join_group);
            buttonLeaveGroup = view.findViewById(R.id.button_leave_group);
            buttonGroupMembers = view.findViewById(R.id.button_group_members);
            buttonOpenChat = view.findViewById(R.id.button_group_chat);

            groupDetailsShort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandedGroupId == object.getId()) {
                        expandedGroupId = -1;
                        groupDetailsContainer.setVisibility(View.GONE);
                    } else {
                        notifyDataSetChanged();
                        expandedGroupId = object.getId();
                        groupDetailsContainer.setVisibility(View.VISIBLE);
                    }
                }
            });
            buttonDeleteGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GroupAdapterListener) callback).onButtonDeleteGroupClick(object);
                }
            });
            buttonGroupMembers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GroupAdapterListener) callback).onGroupMembersClick(object);
                }
            });
            buttonJoinGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = true;
                    v.setVisibility(View.GONE);
                    buttonLeaveGroup.setVisibility(View.VISIBLE);
                    textGroupNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    iconMembers.setImageResource(R.drawable.ic_people_accent_24dp);
                    iconCheck.setVisibility(View.VISIBLE);
                    ((GroupAdapterListener) callback).onGroupCheckboxClick(object, selected);
                }
            });
            buttonLeaveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = false;
                    v.setVisibility(View.GONE);
                    buttonJoinGroup.setVisibility(View.VISIBLE);
                    textGroupNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorDark));
                    iconMembers.setImageResource(R.drawable.ic_people_dark_24dp);
                    iconCheck.setVisibility(View.GONE);
                    ((GroupAdapterListener) callback).onGroupCheckboxClick(object, selected);
                }
            });
            buttonOpenChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GroupAdapterListener) callback).onGroupChatClick(object);
                }
            });
        }

        @Override
        public void bind(Group g) {
            boolean isMember = g.getMembers().contains(user.getId());
            boolean isCreator = g.getCreator().getId() == user.getId();

            textGroupName.setText(g.getName());
            if (isMember) {
                selected = true;
                buttonJoinGroup.setVisibility(View.GONE);
                buttonLeaveGroup.setVisibility(View.VISIBLE);
                textGroupNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                iconMembers.setImageResource(R.drawable.ic_people_accent_24dp);
                iconCheck.setVisibility(View.VISIBLE);
            } else {
                selected = false;
                buttonLeaveGroup.setVisibility(View.GONE);
                buttonJoinGroup.setVisibility(View.VISIBLE);
                textGroupNumMembers.setTextColor(ContextCompat.getColor(context, R.color.colorDark));
                iconMembers.setImageResource(R.drawable.ic_people_dark_24dp);
                iconCheck.setVisibility(View.GONE);
            }
            textGroupName.setText(g.getName());
            textGroupCreator.setText(g.getCreator().getFullName()); // TODO: "Created by: you" if user is creator
            textGroupNumMembers.setText(String.valueOf(g.getMembers().size()));
            groupDetailsContainer.setVisibility((expandedGroupId == g.getId()) ? View.VISIBLE : View.GONE);
            buttonDeleteGroup.setVisibility(isCreator ? View.VISIBLE : View.GONE);
            buttonOpenChat.setVisibility(isMember ? View.VISIBLE : View.GONE);
            object = g;
        }

    }

}
