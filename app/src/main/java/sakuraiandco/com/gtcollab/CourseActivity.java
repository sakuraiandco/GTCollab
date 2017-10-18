package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.adapters.GroupAdapter;
import sakuraiandco.com.gtcollab.adapters.MeetingAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN_FILE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_ID;
import static sakuraiandco.com.gtcollab.constants.Arguments.COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.GROUP;
import static sakuraiandco.com.gtcollab.constants.Arguments.MEETING;
import static sakuraiandco.com.gtcollab.constants.Arguments.SEARCH_RESULTS;
import static sakuraiandco.com.gtcollab.constants.Arguments.SUBJECT;
import static sakuraiandco.com.gtcollab.constants.Arguments.TITLE;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;

public class CourseActivity extends AppCompatActivity implements GroupAdapter.Listener, MeetingAdapter.Listener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private static TextView textNoGroupsFound;
    private static TextView textNoMeetingsFound;
    private static RecyclerView groupsRecyclerView;
    private static RecyclerView meetingsRecyclerView;

    private static GroupAdapter groupAdapter; // TODO: non-static?
    private static MeetingAdapter meetingAdapter; // TODO: non-static?

    TextView textCourseTermName;
    TextView textCourseLongName;
    TextView textCourseSections;
    TextView textCourseNumMembers;

    CourseDAO courseDAO;
    GroupDAO groupDAO;
    MeetingDAO meetingDAO;

    List<Group> groupsList;
    List<Meeting> meetingsList;

    String userId;
    String courseId;

    Course course;

    private static Context context; // TODO

    CoordinatorLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // TODO: make singleTop activity?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        courseDAO = new CourseDAO(new DAOListener<Course>() {
            @Override
            public void onListReady(List<Course> courses) {}

            @Override
            public void onObjectReady(Course course) {
                if (!course.getMembers().contains(Integer.valueOf(userId))) {
                    // left course
                    Toast.makeText(CourseActivity.this, "Left course: " + course.getSubjectCode() + " " + course.getCourseNumber(), Toast.LENGTH_SHORT).show();
                    Intent courseListActivityIntent = new Intent(CourseActivity.this, CourseListActivity.class);
                    startActivity(courseListActivityIntent);
                }

                CourseActivity.this.course = course;

                collapsingToolbarLayout.setTitle(course.getSubjectCode() + " " + course.getCourseNumber());
                String sections = "";
                if (course.getSections().size() > 0) {
                    for (String s : course.getSections()) {
                        sections += s + ", ";
                    }
                    sections = sections.substring(0, sections.length() - 2);
                } else {
                    sections = "none";
                }
                textCourseTermName.setText("Fall 2017"); // TODO
                textCourseLongName.setText(course.getName());
                textCourseSections.setText(sections);
                textCourseNumMembers.setText(String.valueOf(course.getNumMembers()));
            }
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
        });
        groupDAO = new GroupDAO(new DAOListener<Group>() {
            @Override
            public void onListReady(List<Group> groups) {
                groupsList = groups;
                groupAdapter.setData(groupsList);
                if (groups.isEmpty()) {
                    textNoGroupsFound.setVisibility(View.VISIBLE);
                } else {
                    textNoGroupsFound.setVisibility(View.GONE);
                }
            }
            @Override
            public void onObjectReady(Group group) {
                List<Integer> members = group.getMembers();
                boolean isMember = false;
                for (int i = 0; i < groupsList.size(); i++) {
                    Group g = groupsList.get(i);
                    if (g.getId() == group.getId()) {
                        groupsList.set(i, group);
                        isMember = group.getMembers().contains(Integer.valueOf(userId));
                        groupAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (isMember) {
                    Snackbar.make(mainContent, "Joined group: " + group.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show(); // TODO: view?
                } else {
                    Snackbar.make(mainContent, "Left group: " + group.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show(); // TODO: view?
                }
            }
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "GroupDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
        });
        meetingDAO = new MeetingDAO(new DAOListener<Meeting>() {
            @Override
            public void onListReady(List<Meeting> meetings) {
                meetingsList = meetings;
                meetingAdapter.setData(meetingsList);
                if (meetings.isEmpty()) {
                    textNoMeetingsFound.setVisibility(View.VISIBLE);
                } else {
                    textNoMeetingsFound.setVisibility(View.GONE);
                }
            }
            @Override
            public void onObjectReady(Meeting meeting) {
                List<Integer> members = meeting.getMembers();
                boolean isMember = false;
                for (int i = 0; i < meetingsList.size(); i++) {
                    Meeting m = meetingsList.get(i);
                    if (m.getId() == meeting.getId()) {
                        meetingsList.set(i, meeting);
                        isMember = meeting.getMembers().contains(Integer.valueOf(userId));
                        meetingAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (isMember) {
                    Snackbar.make(mainContent, "Joined meeting: " + meeting.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show(); // TODO: view?
                } else {
                    Snackbar.make(mainContent, "Left meeting: " + meeting.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show(); // TODO: view?
                }
            }
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "MeetingDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
        });

        userId = getSharedPreferences(AUTH_TOKEN_FILE, 0).getString(CURRENT_USER, null);

        // adapter
        groupAdapter = new GroupAdapter(this, userId);
        meetingAdapter = new MeetingAdapter(this, userId);

        // view
        mainContent = (CoordinatorLayout) findViewById(R.id.main_content);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tab = mViewPager.getCurrentItem();
                switch (tab) {
                    case TAB_MEETINGS:
                        createNewMeeting();
                        break;
                    case TAB_GROUPS:
                        createNewGroup();
                        break;
                    default:
                        Toast.makeText(CourseActivity.this, "No context", Toast.LENGTH_SHORT).show();
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        textCourseTermName = (TextView) findViewById(R.id.text_course_term_name);
        textCourseLongName = (TextView) findViewById(R.id.text_course_long_name);
        textCourseSections = (TextView) findViewById(R.id.text_course_sections);
        textCourseNumMembers = (TextView) findViewById(R.id.text_course_num_members);

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        courseId = getIntent().getStringExtra(COURSE_ID);
        if (courseId != null) {
            Map<String, String> filters = new HashMap<>();
            filters.put(COURSE, courseId);
            courseDAO.get(Integer.valueOf(courseId));
            groupDAO.getByFilters(filters);
            meetingDAO.getByFilters(filters);
        } else {
            Toast.makeText(this, "No course ID", Toast.LENGTH_SHORT).show();
        }
        int tab = getIntent().getIntExtra(COURSE_TAB, -1);
        switch (tab) {
            case TAB_MEETINGS:
                mViewPager.setCurrentItem(TAB_MEETINGS);
                break;
            case TAB_GROUPS:
                mViewPager.setCurrentItem(TAB_GROUPS);
                break;
            default:
                mViewPager.setCurrentItem(TAB_MEETINGS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_course_members:
                openUserList(COURSE, courseId, course.getShortName());
                return true;
            case R.id.action_new_meeting:
                createNewMeeting();
                return true;
            case R.id.action_new_group:
                createNewGroup();
                return true;
            case R.id.action_leave_course:
                courseDAO.leaveCourse(Integer.valueOf(courseId));
                return true;
            case R.id.action_settings:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGroupCheckboxClick(View v, int objectId) {
        if (((CheckBox) v).isChecked()) {
            groupDAO.joinGroup(objectId);
        } else {
            groupDAO.leaveGroup(objectId);
        }
    }

    @Override
    public void onGroupMembersClick(View v, int objectId, String groupName) {
        openUserList(GROUP, String.valueOf(objectId), groupName);
    }

    @Override
    public void onMeetingCheckboxClick(View v, int objectId) {
        if (((CheckBox) v).isChecked()) {
            meetingDAO.joinMeeting(objectId);
        } else {
            meetingDAO.leaveMeeting(objectId);
        }
    }

    @Override
    public void onMeetingMembersClick(View v, int objectId, String meetingName) {
        openUserList(MEETING, String.valueOf(objectId), meetingName);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            if (sectionNum == TAB_GROUPS) {
                rootView = inflater.inflate(R.layout.fragment_course_groups, container, false);
                groupsRecyclerView = rootView.findViewById(R.id.groups_recycler_view);
                groupsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                groupsRecyclerView.setAdapter(groupAdapter);
                groupsRecyclerView.addItemDecoration(new DividerItemDecoration(groupsRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
                textNoGroupsFound = rootView.findViewById(R.id.text_no_groups_found);
            } else { // meetings (e.g. sectionNum = 0)
                rootView = inflater.inflate(R.layout.fragment_course_meetings, container, false);
                meetingsRecyclerView = rootView.findViewById(R.id.meetings_recycler_view);
                meetingsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                meetingsRecyclerView.setAdapter(meetingAdapter);
                meetingsRecyclerView.addItemDecoration(new DividerItemDecoration(meetingsRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
                textNoMeetingsFound = rootView.findViewById(R.id.text_no_meetings_found);
            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
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

    private void openUserList(String objectType, String objectId, String title) {
        Intent userListActivityIntent = new Intent(this, UserListActivity.class);
        userListActivityIntent.putExtra(objectType, objectId);
        userListActivityIntent.putExtra(TITLE, title);
        startActivity(userListActivityIntent);
    }

    private void createNewMeeting() {
        Intent createMeetingActivityIntent = new Intent(this, CreateMeetingActivity.class);
        createMeetingActivityIntent.putExtra(COURSE, courseId);
        startActivity(createMeetingActivityIntent);
    }

    private void createNewGroup() {
        Intent createGroupActivityIntent = new Intent(this, CreateGroupActivity.class);
        createGroupActivityIntent.putExtra(COURSE, courseId);
        startActivity(createGroupActivityIntent);
    }
}
