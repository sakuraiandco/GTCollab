package sakuraiandco.com.gtcollab.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sakuraiandco.com.gtcollab.CourseActivity;

import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;

/**
 * Created by kaliq on 10/19/2017.
 */

public class CoursePagerAdapter extends FragmentPagerAdapter {

    public CoursePagerAdapter(FragmentManager fm) {
        super(fm);
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
                return "Meetings";
            case TAB_GROUPS:
                return "Groups";
        }
        return null;
    }
}
