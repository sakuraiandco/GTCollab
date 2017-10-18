package sakuraiandco.com.gtcollab.temp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.domain.Group;

/**
 * Created by Alex on 10/14/17.
 */

//public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> implements Filterable {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView groupText;

        public ViewHolder(View itemView) {
            super(itemView);
            groupText = itemView.findViewById(R.id.groupItem);

        }
    }
    private List<Group> groups;
    private Context context;
    private List<Group> cachedGroups;
    private Filter groupFilter;

    public GroupAdapter(Context context) {
        this(context, new ArrayList<Group>());
    }
    public GroupAdapter(Context context, List<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.group_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder viewHolder, int position) {
        final Group group = groups.get(position);
        viewHolder.groupText.setText(group.getName());

        boolean inGroup = false;

        List<Integer> members = group.getMembers();
        for (int id: members) {
            // TODO: get current user and compare to member list
            String userID = context.getSharedPreferences(Arguments.AUTH_TOKEN_FILE, 0).getString(Arguments.CURRENT_USER, "missing");
            if (Integer.parseInt(userID) == id) {
                inGroup = true;
                break;
            }
        }
        viewHolder.groupText.setChecked(inGroup);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void addGroup(Group group) {
        groups.add(group);
        this.notifyItemInserted(groups.size() - 1);
    }

    public void cacheGroups() {
        cachedGroups = groups;
    }

    @Override
    public Filter getFilter() {
        if (groupFilter == null) {
            groupFilter = new GroupFilter();
        }
        return groupFilter;

    }

    private class GroupFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<Group> filterList = new ArrayList<>();
                for (Group group: cachedGroups) {
                    if ((group.getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(group);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = cachedGroups.size();
                results.values = cachedGroups;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            groups = (List) results.values;
            notifyDataSetChanged();
        }

    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
        cacheGroups();
        notifyDataSetChanged();
    }

}
