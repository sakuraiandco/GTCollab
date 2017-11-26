package sakuraiandco.com.gtcollab.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.GroupMessageAdapter;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.GroupMessage;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.GroupMessageDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;

public class GroupChatActivity extends AppCompatActivity {

    private Group group;
    private User user;
    private RecyclerView messageRecycler;
    private GroupMessageAdapter messageAdapter;
    private EditText messageText;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SingletonProvider.setContext(getApplicationContext());

        messageText = findViewById(R.id.edittext_message);
        group = getIntent().getParcelableExtra("group");
        user = getIntent().getParcelableExtra(EXTRA_USER);
        getSupportActionBar().setTitle(group.getName());

        messageRecycler = findViewById(R.id.recyclerview_message_list);
        messageAdapter = new GroupMessageAdapter(user);
        layoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(layoutManager);
        messageRecycler.setAdapter(messageAdapter);

        HashMap<String, String> filters = new HashMap<>();
        filters.put("group", String.valueOf(group.getId()));
        new GroupMessageDAO(
                new BaseDAO.Listener<GroupMessage>() {
                    @Override
                    public void onDAOError(BaseDAO.Error error) {}

                    @Override
                    public void onListReady(List<GroupMessage> groupMessages) {
                        Collections.reverse(groupMessages);
                        messageAdapter.setData(groupMessages);
                    }

                    @Override
                    public void onObjectReady(GroupMessage groupMessage) {}

                    @Override
                    public void onObjectDeleted() {}
                }
        ).getByFilters(filters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void sendMessage(View view) {
        String message = messageText.getText().toString();
        if (!message.isEmpty()) {
            GroupMessage groupMessage = GroupMessage.builder()
                    .content(message)
                    .groupId(group.getId())
                    .creator(user)
                    .timestamp(DateTime.now())
                    .build();
            sendMessage(groupMessage);
        }
    }

    public void sendMessage(GroupMessage message) {
        new GroupMessageDAO(new BaseDAO.Listener<GroupMessage>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {}

            @Override
            public void onListReady(List<GroupMessage> groupMessages) {}

            @Override
            public void onObjectReady(GroupMessage groupMessage) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                messageAdapter.addItem(groupMessage);
                messageRecycler.scrollToPosition(messageAdapter.getItemCount() - 1);
            }

            @Override
            public void onObjectDeleted() {}
        }).create(message);
    }
}
