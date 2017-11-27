package sakuraiandco.com.gtcollab.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import sakuraiandco.com.gtcollab.dialogs.MeetingProposalDialogFragment;
import sakuraiandco.com.gtcollab.domain.Course;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.Meeting;
import sakuraiandco.com.gtcollab.domain.MeetingProposal;
import sakuraiandco.com.gtcollab.domain.Term;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.CourseDAO;
import sakuraiandco.com.gtcollab.rest.GroupDAO;
import sakuraiandco.com.gtcollab.rest.MeetingDAO;
import sakuraiandco.com.gtcollab.rest.MeetingProposalDAO;
import sakuraiandco.com.gtcollab.rest.TermDAO;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.utils.PaginationScrollListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.DEFAULT_SHARED_PREFERENCES;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_COURSE_TAB;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_MEMBERS;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_TERM;
import static sakuraiandco.com.gtcollab.constants.Arguments.LAST_OPENED_COURSE;
import static sakuraiandco.com.gtcollab.constants.Arguments.TAG_MEETING_PROPOSAL_DIALOG;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_GROUPS;
import static sakuraiandco.com.gtcollab.constants.Constants.TAB_MEETINGS;
import static sakuraiandco.com.gtcollab.utils.GeneralUtils.forceDeviceTokenRefresh;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.login;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.logout;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCreateGroupActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startCreateMeetingActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startGroupChatActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startNotificationsActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startSubjectSearchActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startTermActivity;
import static sakuraiandco.com.gtcollab.utils.NavigationUtils.startUserListActivity;

public class CourseActivity extends AppCompatActivity {

    final static String[] FILTER_OPTION_PREFIXES = {"All", "My"};
    final static int FILTER_OPTION_ALL = 0;
    final static int FILTER_OPTION_MY = 1;
    final static String FILTER_TITLE = "Filter by";

    // data
    UserDAO userDAO;
    TermDAO termDAO;
    CourseDAO courseDAO;
    GroupDAO groupDAO;
    MeetingDAO meetingDAO;
    MeetingProposalDAO meetingProposalDAO;

    // saved data
    SharedPreferences prefs;

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
    DrawerLayout drawerLayout;
    NavigationView navigationView;
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

    // other TODO
    ActionBarDrawerToggle drawerToggle;

    // context
    User user;
    Term term;
    Course course;
    List<Course> myCoursesList; // replace with map; key with course id

    // variables
    private String userId;
    private String authToken;
    private Term intentTerm;
    private Course intentCourse;
//    private int currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // TODO: make singleTop activity?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // navigation drawer
        myCoursesList = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
//        drawerLayout.setDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.menu_item_term:
                        // TODO: start term-select activity
                        startTermActivity(CourseActivity.this, user);
                        break;
                    case R.id.menu_item_add_course:
                        startSubjectSearchActivity(CourseActivity.this, user, term); // TODO: startActivityForResult instead?
                        break;
                    case R.id.menu_item_notifications:
                        startNotificationsActivity(CourseActivity.this, user, term); // TODO: only after term and user have been retrieved; do if-check, else display error Toast
                        break;
                    case R.id.menu_item_sign_out:
                        logout(CourseActivity.this);
                        break;
                    default:
                        for (Course c : myCoursesList) {
                            if (c.getId() == id) {
                                updateCourseContext(c);
                                break;
                            }
                        }
//                        return false; // TODO
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // device registration
        forceDeviceTokenRefresh(); // TODO: ASSUMES CourseListActivity is launcher activity; best place to put this? LoginActivity instead?

        // data
        userDAO = new UserDAO(new BaseDAO.Listener<User>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "UserDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<User> users) {}
            @Override
            public void onObjectReady(User user) {
                onUserObjectReady(user);
            }
            @Override
            public void onObjectDeleted() {}
        });
        termDAO = new TermDAO(new BaseDAO.Listener<Term>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "TermDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Term> terms) {}
            @Override
            public void onObjectReady(Term term) {
                onTermObjectReady(term);
            }
            @Override
            public void onObjectDeleted() {}
        });
        courseDAO = new CourseDAO(new BaseDAO.Listener<Course>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "CourseDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<Course> courses) {
                onCourseListReady(courses);
            }
            @Override
            public void onObjectReady(Course course) {
                onCourseObjectReady(course);
            }
            @Override
            public void onObjectDeleted() {}
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
            @Override
            public void onObjectDeleted() {}
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
            @Override
            public void onObjectDeleted() {
                onMeetingObjectDeleted();
            }
        });
        meetingProposalDAO = new MeetingProposalDAO(new BaseDAO.Listener<MeetingProposal>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(CourseActivity.this, "MeetingProposalDAO error", Toast.LENGTH_SHORT).show(); // TODO: error handling
            }
            @Override
            public void onListReady(List<MeetingProposal> meetingProposals) {}
            @Override
            public void onObjectReady(MeetingProposal meetingProposal) {
                onMeetingProposalObjectReady(meetingProposal);
            }
            @Override
            public void onObjectDeleted() {}
        });

        // saved data
        prefs = getSharedPreferences(DEFAULT_SHARED_PREFERENCES, 0);
        userId = prefs.getString(CURRENT_USER, null); // TODO: not needed? how to use this to verify authentication?
        authToken = prefs.getString(AUTH_TOKEN, null); // TODO: not needed? how to use this to verify authentication?

        // adapter
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
            public void onClick(Group group) {
                List<Integer> members = group.getMembers();
                if (members.contains(user.getId())) {
                    Context context = SingletonProvider.getContext();
                    startGroupChatActivity(context, user, group);
                }
            }
        });
        meetingAdapter = new MeetingAdapter(new MeetingAdapterListener() {
            @Override
            public void onButtonDeleteMeetingClick(Meeting meeting) {
                onButtonDeleteMeetingClickHandler(meeting);
            }
            @Override
            public void onButtonProposeNewTimeLocationClick(Meeting meeting) {
                onButtonProposeNewTimeLocationClickHandler(meeting);
            }
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
        });
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
        intentTerm = intent.getParcelableExtra(EXTRA_TERM);
        intentCourse = intent.getParcelableExtra(EXTRA_COURSE);

        if (userId != null && authToken != null) {
            userDAO.get(Integer.valueOf(userId));
        } else {
            login(this);
        }

        // TODO: update course status (e.g. new activity bubble)

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

    }

    private void getCourses() { // TODO: refactor into static utils method getMyCourses(CourseDAO courseDAO)
        Map<String, String> filters = new HashMap<>();
        filters.put(FILTER_TERM, String.valueOf(term.getId()));
        filters.put(FILTER_MEMBERS, String.valueOf(user.getId()));
        courseDAO.getByFilters(filters);
    }

    private void onUserObjectReady(User user) {
        this.user = user;
        groupAdapter.setUser(user);
        meetingAdapter.setUser(user);
        this.term = intentTerm;
        if (this.term != null) {
            getCourses();
        } else {
            termDAO.getCurrent();
        }
    }

    private void onTermObjectReady(Term term) {
        this.term = term;
        getCourses();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { // TODO: what is this for?
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) { // TODO: what is this for?
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
                        return false;
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
                        return false;
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
        textCourseTermName.setText(term.getName()); // refactor into updateTermInfo()?
        textCourseLongName.setText(course.getName());
        textCourseSections.setText(sections);
        textCourseNumMembers.setText(String.valueOf(course.getNumMembers()));
    }

    private void onCourseObjectReady(Course course) {
        if (!course.getMembers().contains(user.getId())) { // TODO: is this check needed?
            Toast.makeText(CourseActivity.this, "Left course: " + course.getShortName(), Toast.LENGTH_SHORT).show();
            Menu menu = navigationView.getMenu();
            for (int i = 0; i < myCoursesList.size(); i++) {
                Course c = myCoursesList.get(i);
                if (c.getId() == course.getId()) {
                    menu.removeItem(c.getId());
                    myCoursesList.remove(c);
                    break;
                }
            }
            if (!myCoursesList.isEmpty()) {
                menu.getItem(1).setChecked(true);
                updateCourseContext(myCoursesList.get(0)); // TODO: default to first course?
            } else {
                startSubjectSearchActivity(this, user, term); // TODO: default to subject search activity?
            }
        }
    }

    private void onCourseListReady(List<Course> courses) {
        if (courses.isEmpty()) {
            startSubjectSearchActivity(this, user, term); // TODO: default to subject search activity?
            return;
        }
        Menu menu = navigationView.getMenu();
        MenuItem menuItemTerm = menu.findItem(R.id.menu_item_term);
        menuItemTerm.setTitle(term.getName());
        for (int i = 0; i < myCoursesList.size(); i++) {
            menu.removeItem(myCoursesList.get(i).getId());
        }
        myCoursesList = courses; // TODO: memory leak?

        int selectedCourseId = prefs.getInt(LAST_OPENED_COURSE, -1); // TODO: refactor out -1 as INVALID or something
        if (intentCourse != null) {
            selectedCourseId = intentCourse.getId();
        }
        boolean courseSelected = false;
        for (int i = 0; i < myCoursesList.size(); i++) { // TODO: put in handleIntent() after retrieving all courses
            Course c = myCoursesList.get(i);
            MenuItem item = menu.add(R.id.group_courses, c.getId(), 1, c.getShortName()); // TODO: long name? member count?
            item.setIcon(R.drawable.ic_school_black_24dp); // TODO: icon displaying number of new meetings?
            item.setCheckable(true);
            if (!courseSelected && c.getId() == selectedCourseId) {
                item.setChecked(true);
                updateCourseContext(c);
                courseSelected = true;
            }
        }
        if (!courseSelected) {
            menu.getItem(1).setChecked(true);
            updateCourseContext(myCoursesList.get(0)); // TODO: default to first course?
        }
    }

    private void updateCourseContext(Course c) {
        if (course != null && course.getId() == c.getId()) {
            return;
        }
        course = c;
        prefs.edit().putInt(LAST_OPENED_COURSE, course.getId()).apply();
        Map<String, String> filters = new HashMap<>();
        filters.put(FILTER_COURSE, String.valueOf(course.getId()));
        groupDAO.getByFilters(filters);
        meetingDAO.getByFilters(filters);
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
        tabLayout.getTabAt(TAB_GROUPS).setText("Groups (" + groupDAO.getCount() + ")");
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
        tabLayout.getTabAt(TAB_MEETINGS).setText("Meetings (" + meetingDAO.getCount() + ")");
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

    private void onMeetingObjectDeleted() {
        tabLayout.getTabAt(TAB_MEETINGS).setText("Meetings (" + meetingDAO.getCount() + ")");
        Snackbar.make(mainContent, "Meeting deleted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }

    private void onMeetingProposalObjectReady(MeetingProposal meetingProposal) {
        Toast.makeText(this, "Successfully created meeting proposal", Toast.LENGTH_SHORT).show(); // TODO: how to indicate ongoing meeting proposal in UI?
    }

    private void onGroupCheckboxClickHandler(Group group, boolean isChecked) {
        if (isChecked) {
            groupDAO.joinGroup(group.getId());
        } else {
            groupDAO.leaveGroup(group.getId());
        }
    }

    private void onButtonDeleteMeetingClickHandler(Meeting meeting) { // TODO: show confirmation dialog
        meetingDAO.delete(meeting.getId());
        meetingsList.remove(meeting); // TODO: ok to do before deleting for real?
        myMeetingsList.remove(meeting); // TODO: ok to do before deleting for real?
        meetingAdapter.notifyDataSetChanged();
    }

    private void onButtonProposeNewTimeLocationClickHandler(Meeting meeting) {
        MeetingProposalDialogFragment mp = MeetingProposalDialogFragment.newInstance(new MeetingProposalDialogFragment.MeetingProposalDialogListener() {
            @Override
            public void onMeetingProposalDialogSubmit(MeetingProposal meetingProposal) {
                meetingProposalDAO.create(meetingProposal);
            }
        }, meeting);
        mp.show(getFragmentManager(), TAG_MEETING_PROPOSAL_DIALOG);
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
