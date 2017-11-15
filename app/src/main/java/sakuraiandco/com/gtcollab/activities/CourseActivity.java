package sakuraiandco.com.gtcollab.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.CoursePagerAdapter;
import sakuraiandco.com.gtcollab.adapters.GroupAdapter;
import sakuraiandco.com.gtcollab.adapters.GroupAdapterListener;
import sakuraiandco.com.gtcollab.adapters.MeetingAdapter;
import sakuraiandco.com.gtcollab.adapters.MeetingAdapterListener;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_COURSE;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCourseListActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCreateGroupActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCreateMeetingActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startUserListActivity;

public class CourseActivity extends AppCompatActivity {

    final static String[] FILTER_OPTION_PREFIXES = {"All", "My"};
    final static int FILTER_OPTION_ALL = 0;
    final static int FILTER_OPTION_MY = 1;
    final static String FILTER_TITLE = "Filter by";

    // data
    CourseDAO courseDAO;
    GroupDAO groupDAO;
    MeetingDAO meetingDAO;

    // adapter
    private CoursePagerAdapter coursePagerAdapter;
    private GroupAdapter groupAdapter;
    private MeetingAdapter meetingAdapter;
    List<Group> groupsList;
    List<Group> myGroupsList;
    List<Meeting> meetingsList;
    List<Meeting> myMeetingsList;

    // layout manager
    private LinearLayoutManager groupsLayoutManager;
    private LinearLayoutManager meetingsLayoutManager;

    // view
    CoordinatorLayout mainContent;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView textCourseTermName;
    TextView textCourseLongName;
    TextView textCourseSections;
    TextView textCourseNumMembers;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView textNoGroupsFound;
    TextView textNoMeetingsFound;
    RecyclerView groupsRecyclerView;
    RecyclerView meetingsRecyclerView;
    LinearLayout groupFilterWrapper;
    LinearLayout meetingFilterWrapper;
    TextView meetingFilterText;
    TextView groupFilterText;
    SearchView meetingSearch;
    SearchView groupSearch;
    FloatingActionButton fab;

    // context
    User user;
    Term term;
    Course course;

//    private int currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // TODO: make singleTop activity?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        courseDAO = new CourseDAO(new BaseDAO.Listener<Course>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Course> courses) {}
            @Override
            public void onObjectReady(Course course) {
                onCourseObjectReady(course);
            }
        });
        groupDAO = new GroupDAO(new BaseDAO.Listener<Group>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "GroupDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Group> groups) {
                onGroupListReady(groups);
            }
            @Override
            public void onObjectReady(Group group) {
                onGroupObjectReady(group);
            }
        });
        meetingDAO = new MeetingDAO(new BaseDAO.Listener<Meeting>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "MeetingDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Meeting> meetings) {
                onMeetingListReady(meetings);
            }
            @Override
            public void onObjectReady(Meeting meeting) {
                onMeetingObjectReady(meeting);
            }
        });

        // adapter
        user = getIntent().getParcelableExtra(EXTRA_USER); // TODO: put in handleIntent?
        groupAdapter = new GroupAdapter(new GroupAdapterListener() {
            @Override
            public void onGroupCheckboxClick(Group group, boolean isChecked) {
                onGroupCheckboxClickHandler(group, isChecked);
            }
            @Override
            public void onGroupMembersClick(Group group) {
                startUserListActivity(CourseActivity.this, user, term, course, group, null);
            }
            @Override
            public void onClick(Group group) {}
        }, user);
        meetingAdapter = new MeetingAdapter(new MeetingAdapterListener() {
            @Override
            public void onMeetingCheckboxClick(Meeting meeting, boolean isChecked) {
                onMeetingCheckboxClickHandler(meeting, isChecked);
            }
            @Override
            public void onMeetingMembersClick(Meeting meeting) {
                startUserListActivity(CourseActivity.this, user, term, course, null, meeting);
            }
            @Override
            public void onClick(Meeting meeting) {}
        }, user);
        coursePagerAdapter = new CoursePagerAdapter(getSupportFragmentManager(), groupAdapter, meetingAdapter);

        // layout manager
        groupsLayoutManager = new LinearLayoutManager(this);
        meetingsLayoutManager = new LinearLayoutManager(this);

        // view
        mainContent = findViewById(R.id.main_content);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout); // null check in courseDAO
        textCourseTermName = findViewById(R.id.text_course_term_name);
        textCourseLongName = findViewById(R.id.text_course_long_name);
        textCourseSections = findViewById(R.id.text_course_sections);
        textCourseNumMembers = findViewById(R.id.text_course_num_members);
        viewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        fab = findViewById(R.id.fab);

        viewPager.setAdapter(coursePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()) {
                    case TAB_MEETINGS:
                        startCreateMeetingActivity(CourseActivity.this, user, term, course);
                        break;
                    case TAB_GROUPS:
                        startCreateGroupActivity(CourseActivity.this, user, term, course);
                        break;
                    default:
                        Toast.makeText(CourseActivity.this, "No tab context", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // retrieve data
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        term = intent.getParcelableExtra(EXTRA_TERM);
        course = intent.getParcelableExtra(EXTRA_COURSE);

        if (course != null) {
            Map<String, String> filters = new HashMap<>();
            filters.put(FILTER_COURSE, String.valueOf(course.getId()));
            groupDAO.getByFilters(filters);
            meetingDAO.getByFilters(filters);
        } else {
            Toast.makeText(this, "No course context", Toast.LENGTH_SHORT).show();
        }

        switch (intent.getIntExtra(EXTRA_COURSE_TAB, -1)) {
            case TAB_MEETINGS:
                viewPager.setCurrentItem(TAB_MEETINGS);
                break;
            case TAB_GROUPS:
                viewPager.setCurrentItem(TAB_GROUPS);
                break;
            default:
                viewPager.setCurrentItem(TAB_MEETINGS);
        }

        updateCourseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_course_members:
                startUserListActivity(this, user, term, course, null, null);
                return true;
            case R.id.action_new_meeting:
                startCreateMeetingActivity(this, user, term, course);
                return true;
            case R.id.action_new_group:
                startCreateGroupActivity(this, user, term, course);
                return true;
            case R.id.action_leave_course:
                courseDAO.leaveCourse(course.getId());
                return true;
            case R.id.action_settings:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

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
            final CourseActivity context = (CourseActivity) getActivity();
            if (sectionNum == TAB_GROUPS) {
                rootView = inflater.inflate(R.layout.fragment_course_groups, container, false);
                context.groupsRecyclerView = rootView.findViewById(R.id.groups_recycler_view);
                context.textNoGroupsFound = rootView.findViewById(R.id.text_no_groups_found);
                context.groupFilterWrapper = rootView.findViewById(R.id.group_filter_wrapper);
                context.groupFilterText = rootView.findViewById(R.id.group_filter_text);
                context.groupSearch = rootView.findViewById(R.id.group_search);
                context.groupsRecyclerView.setLayoutManager(context.groupsLayoutManager);
                context.groupsRecyclerView.setAdapter(context.groupAdapter);
                context.groupsRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                context.groupsRecyclerView.addOnScrollListener(new PaginationScrollListener<>(context.groupsLayoutManager, context.groupDAO));
                context.groupFilterWrapper.setTag(FILTER_OPTION_ALL); // TODO: show all by default
                context.groupFilterWrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.onGroupFilterClickHandler(v);
                    }
                });
                context.groupSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        context.refreshGroupList(query);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        context.refreshGroupList(newText); // TODO: performance?
                        return false;
                    }
                });
            } else { // meetings (e.g. sectionNum = 0)
                rootView = inflater.inflate(R.layout.fragment_course_meetings, container, false);
                context.meetingsRecyclerView = rootView.findViewById(R.id.meetings_recycler_view);
                context.textNoMeetingsFound = rootView.findViewById(R.id.text_no_meetings_found);
                context.meetingFilterWrapper = rootView.findViewById(R.id.meeting_filter_wrapper);
                context.meetingFilterText = rootView.findViewById(R.id.meeting_filter_text);
                context.meetingSearch = rootView.findViewById(R.id.meeting_search);
                context.meetingsRecyclerView.setLayoutManager(context.meetingsLayoutManager);
                context.meetingsRecyclerView.setAdapter(context.meetingAdapter);
                context.meetingsRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                context.meetingsRecyclerView.addOnScrollListener(new PaginationScrollListener<>(context.meetingsLayoutManager, context.meetingDAO));
                context.meetingFilterWrapper.setTag(FILTER_OPTION_ALL); // TODO: show all by default
                context.meetingFilterWrapper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.onMeetingFilterClickHandler(v);
                    }
                });
                context.meetingSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        context.refreshMeetingList(query);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        context.refreshMeetingList(newText);  // TODO: performance?
                        return false;
                    }
                });
            }
            return rootView;
        }
    }

    private void updateCourseInfo() { //  TODO: unnecessary? courseDAO is only used to leave course - not change course info
        String sections = "";
        if (course.getSections().size() > 0) {
            for (String s : course.getSections()) {
                sections += s + ", ";
            }
            sections = sections.substring(0, sections.length() - 2);
        } else {
            sections = "none";
        }
        collapsingToolbarLayout.setTitle(course.getShortName());
        textCourseTermName.setText(term.getName());
        textCourseLongName.setText(course.getName());
        textCourseSections.setText(sections);
        textCourseNumMembers.setText(String.valueOf(course.getNumMembers()));
    }

    private void onCourseObjectReady(Course course) {
        if (!course.getMembers().contains(user.getId())) {
            Toast.makeText(CourseActivity.this, "Left course: " + course.getShortName(), Toast.LENGTH_SHORT).show();
            startCourseListActivity(this, user, term);
        }

        this.course = course;
        updateCourseInfo();
    }

    private void onGroupListReady(List<Group> groups) {
        if (groupDAO.hasPrev()) { // handle pagination - append results
            groupsList.addAll(groups);
            for (Group g : groups) {
                if (g.getMembers().contains(user.getId())) {
                    myGroupsList.add(g);
                }
            }
        } else { // first page
            groupsList = groups;
            myGroupsList = new ArrayList<>();
            for (Group g : groupsList) {
                if (g.getMembers().contains(user.getId())) {
                    myGroupsList.add(g);
                }
            }
        }
        switch((int) groupFilterWrapper.getTag()) {
            case FILTER_OPTION_ALL:
                showAllGroups();
                break;
            case FILTER_OPTION_MY:
                showMyGroups();
                break;
            default:
                Toast.makeText(CourseActivity.this, "Group Filter Error", Toast.LENGTH_SHORT).show(); // TODO: error handling
        }
        tabLayout.getTabAt(TAB_GROUPS).setText("Groups (" + groupsList.size() + ")");
    }

    private void onGroupObjectReady(Group group) {
        boolean joined = group.getMembers().contains(user.getId());
        for (Group g : groupsList) {
            if (g.getId() == group.getId()) {
                g.setMembers(group.getMembers()); //  TODO: this should update myGroupsList too
                if (joined) {
                    myGroupsList.add(g); // TODO: check for duplicates?
                } else {
                    myGroupsList.remove(g);
                }
                groupAdapter.notifyDataSetChanged();
                break;
            }
        }
        Snackbar.make(mainContent, (joined ? "Joined" : "Left") + " group: " + group.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    private void onMeetingListReady(List<Meeting> meetings) {
        if (meetingDAO.hasPrev()) { // handle pagination - append results
            meetingsList.addAll(meetings);
            for (Meeting m : meetings) {
                if (m.getMembers().contains(user.getId())) {
                    myMeetingsList.add(m);
                }
            }
        } else { // first page
            meetingsList = meetings;
            myMeetingsList = new ArrayList<>();
            for (Meeting m : meetingsList) {
                if (m.getMembers().contains(user.getId())) {
                    myMeetingsList.add(m);
                }
            }
        }
        switch((int) meetingFilterWrapper.getTag()) {
            case FILTER_OPTION_ALL:
                showAllMeetings();
                break;
            case FILTER_OPTION_MY:
                showMyMeetings();
                break;
            default:
                Toast.makeText(CourseActivity.this, "Meeting Filter Error", Toast.LENGTH_SHORT).show(); // TODO: error handling
        }
        tabLayout.getTabAt(TAB_MEETINGS).setText("Meetings (" + meetingsList.size() + ")");
    }

    private void onMeetingObjectReady(Meeting meeting) {
        boolean joined = meeting.getMembers().contains(user.getId());
        for (Meeting m : meetingsList) {
            if (m.getId() == meeting.getId()) {
                m.setMembers(meeting.getMembers()); //  TODO: this should update myMeetingsList too
                if (joined) {
                    myMeetingsList.add(m); // TODO: check for duplicates?
                } else {
                    myMeetingsList.remove(m);
                }
                meetingAdapter.notifyDataSetChanged();
                break;
            }
        }
        Snackbar.make(mainContent, (joined ? "Joined" : "Left") + " meeting: " + meeting.getName(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    private void onGroupCheckboxClickHandler(Group group, boolean isChecked) {
        if (isChecked) {
            groupDAO.joinGroup(group.getId());
        } else {
            groupDAO.leaveGroup(group.getId());
        }
    }

    private void onMeetingCheckboxClickHandler(Meeting meeting, boolean isChecked) {
        if (isChecked) {
            meetingDAO.joinMeeting(meeting.getId());
        } else {
            meetingDAO.leaveMeeting(meeting.getId());
        }
    }

    private void onGroupFilterClickHandler(final View v) {
        final String category = "Groups";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String[] filterOptions = Arrays.copyOf(FILTER_OPTION_PREFIXES, FILTER_OPTION_PREFIXES.length);
        for (int i = 0; i < filterOptions.length; i++) {
            filterOptions[i] += " " + category;
        }

        builder.setTitle(FILTER_TITLE)
                .setItems(filterOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which != (int) v.getTag()) {
                            v.setTag(which);
                            groupFilterText.setText(filterOptions[which]);
                            switch(which) {
                                case FILTER_OPTION_ALL:
                                    showAllGroups();
                                    break;
                                case FILTER_OPTION_MY:
                                    showMyGroups();
                                    break;
                                default:
                                    break; // TODO: error handling
                            }
                        }
                    }
                })
                .show();
    }

    private void onMeetingFilterClickHandler(final View v) {
        final String category = "Meetings";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final String[] filterOptions = Arrays.copyOf(FILTER_OPTION_PREFIXES, FILTER_OPTION_PREFIXES.length);
        for (int i = 0; i < filterOptions.length; i++) {
            filterOptions[i] += " " + category;
        }

        builder.setTitle(FILTER_TITLE)
                .setItems(filterOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which != (int) v.getTag()) {
                            v.setTag(which);
                            meetingFilterText.setText(filterOptions[which]);
                            switch(which) {
                                case FILTER_OPTION_ALL:
                                    showAllMeetings();
                                    break;
                                case FILTER_OPTION_MY:
                                    showMyMeetings();
                                default:
                                    break; // TODO: error handling
                            }
                        }
                    }
                })
                .show();
    }

    private void refreshGroupList(String query) {
        Map<String, String> filters = new HashMap<>();
        filters.put(FILTER_COURSE, String.valueOf(course.getId()));
        groupDAO.getBySearchAndFilters(query, filters);
    }

    private void refreshMeetingList(String query) {
        Map<String, String> filters = new HashMap<>();
        filters.put(FILTER_COURSE, String.valueOf(course.getId()));
        meetingDAO.getBySearchAndFilters(query, filters);
    }

    private void showAllGroups() { // TODO: unnecessary?
        showGroups(groupsList);
    }

    private void showMyGroups() { // TODO: unnecessary?
        showGroups(myGroupsList);
    }

    private void showAllMeetings() { // TODO: unnecessary?
        showMeetings(meetingsList);
    }

    private void showMyMeetings() { // TODO: unnecessary?
        showMeetings(myMeetingsList);
    }

    private void showGroups(List<Group> groups) {
        groupAdapter.setData(groups);
        if (groups.isEmpty()) {
            textNoGroupsFound.setVisibility(View.VISIBLE);
        } else {
            textNoGroupsFound.setVisibility(View.GONE);
        }
    }

    private void showMeetings(List<Meeting> meetings) {
        meetingAdapter.setData(meetings);
        if (meetings.isEmpty()) {
            textNoGroupsFound.setVisibility(View.VISIBLE);
        } else {
            textNoGroupsFound.setVisibility(View.GONE);
        }
    }

}
