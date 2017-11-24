package sakuraiandco.com.gtcollab.adapters;

import sakuraiandco.com.gtcollab.domain.Meeting;

/**
 * Created by kaliq on 11/13/2017.
 */

public interface MeetingAdapterListener extends AdapterListener<Meeting> {

    void onButtonDeleteMeetingClick(Meeting meeting);
    void onButtonProposeNewTimeLocationClick(Meeting meeting);
    void onMeetingCheckboxClick(Meeting meeting, boolean isChecked);
    void onMeetingMembersClick(Meeting meeting);

}
