package sakuraiandco.com.gtcollab.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sakuraiandco.com.gtcollab.activities.CourseActivity;

import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;

/**
 * Created by kaliq on 10/19/2017.
 */

public class CoursePagerAdapter extends FragmentPagerAdapter {

    GroupAdapter groupAdapter;
    MeetingAdapter meetingAdapter;

    public CoursePagerAdapter(FragmentManager fm, GroupAdapter groupAdapter, MeetingAdapter meetingAdapter) {
        super(fm);
        this.groupAdapter = groupAdapter;
        this.meetingAdapter = meetingAdapter;
    }

    @Override
    public Fragment getItem(int position) {
        return CourseActivity.PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getCount() { return 2; }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_MEETINGS:
                return "Meetings (" + String.valueOf(meetingAdapter.getItemCount()) + ")";
            case TAB_GROUPS:
                return "Groups (" + String.valueOf(groupAdapter.getItemCount()) + ")";
        }
        return null;
    }
}
