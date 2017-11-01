package sakuraiandco.com.gtcollab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.constants.SingletonProvider;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.RESTServices;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;
import sakuraiandco.com.gtcollab.utils.VolleyResponseListener;

import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN;
import static sakuraiandco.com.gtcollab.constants.Arguments.AUTH_TOKEN_FILE;
import static sakuraiandco.com.gtcollab.constants.Arguments.CURRENT_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.EXTRA_USER;
import static sakuraiandco.com.gtcollab.constants.Arguments.FILTER_USERNAME;

public class LoginActivity extends AppCompatActivity {

    // data
    UserDAO userDAO;

    // saved data
    SharedPreferences prefs;

    // view
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;

    // variables
    private String userId;
    private String authToken;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialization
        SingletonProvider.setContext(getApplicationContext());

        // data
        userDAO = new UserDAO(new DAOListener<User>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                Toast.makeText(LoginActivity.this, "Could not retrieve user", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onListReady(List<User> users) {
                onUserListReady(users);
            }
            @Override
            public void onObjectReady(User user) {
                onUserObjectReady(user);
            }
        });

        // saved data
        prefs = getSharedPreferences(AUTH_TOKEN_FILE, 0);
        userId = prefs.getString(CURRENT_USER, null);
        authToken = prefs.getString(AUTH_TOKEN, null);

        // check if user is already authenticated
        if (userId != null && authToken != null) {
            userDAO.get(Integer.valueOf(userId));
        }

        // view
        editUsername = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.email_sign_in_button);

        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        editUsername.setError(null);
        editPassword.setError(null);

        username = editUsername.getText().toString();
        password = editPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (password.isEmpty()) {
            editPassword.setError(getString(R.string.error_field_required));
            focusView = editPassword;
            cancel = true;
        }
        if (username.isEmpty()) {
            editUsername.setError(getString(R.string.error_field_required));
            focusView = editUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            RESTServices.authUser(username, password, new VolleyResponseListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Could not get auth token", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(JSONObject response) {
                    onAuthResponse(response);
                }
            });
        }
    }

    public void onAuthResponse(JSONObject response) {
        authToken = response.optString("token", null);
        if (authToken != null) {
            Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show(); // TODO: remove?
            prefs.edit().putString(Arguments.AUTH_TOKEN, authToken).apply();
            Map<String, String> filters = new HashMap<>();
            filters.put(FILTER_USERNAME, username);
            userDAO.getByFilters(filters);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show(); // TODO: error handling
        }
    }

    private void onUserListReady(List<User> users) {
        User user = users.get(0);
        prefs.edit().putString(CURRENT_USER, String.valueOf(user.getId())).apply();
        startCourseListActivity(user);
    }

    private void onUserObjectReady(User user) {
        startCourseListActivity(user);
    }

    private void startCourseListActivity(User user) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(EXTRA_USER, user.getId());
        startActivity(intent);
        finish();
    }

}

