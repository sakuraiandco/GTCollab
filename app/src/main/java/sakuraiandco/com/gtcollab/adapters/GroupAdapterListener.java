package sakuraiandco.com.gtcollab.adapters;

import sakuraiandco.com.gtcollab.domain.Group;

/**
 * Created by kaliq on 11/13/2017.
 */

public interface GroupAdapterListener extends AdapterListener<Group> {

    void onButtonDeleteGroupClick(Group group);
    void onGroupCheckboxClick(Group group, boolean isChecked);
    void onGroupMembersClick(Group group);
    void onGroupChatClick(Group group);

}
