package sakuraiandco.com.gtcollab.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

        group = getIntent().getParcelableExtra("group");
        user = getIntent().getParcelableExtra(EXTRA_USER);
        getSupportActionBar().setTitle(group.getName());

        messageRecycler = findViewById(R.id.reyclerview_message_list);
        messageAdapter = new GroupMessageAdapter(user);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);

        HashMap<String, String> filters = new HashMap<>();
        filters.put("group", String.valueOf(group.getId()));
        new GroupMessageDAO(
                new BaseDAO.Listener<GroupMessage>() {
                    @Override
                    public void onDAOError(BaseDAO.Error error) {}

                    @Override
                    public void onListReady(List<GroupMessage> groupMessages) {
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
        // TODO: implement
    }
}
