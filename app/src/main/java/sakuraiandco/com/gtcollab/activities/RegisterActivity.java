package sakuraiandco.com.gtcollab.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;

import static sakuraiandco.com.gtcollab.utils.NavigationUtils.login;

public class RegisterActivity extends AppCompatActivity {

    // data
    UserDAO userDAO;

    // view
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText username;
    EditText password;
    EditText passwordConfirm;
    Button registerButton;

    TextView loginTextButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDAO = new UserDAO(new BaseDAO.Listener<User>() {
            @Override
            public void onDAOError(BaseDAO.Error error) {
                try {
                    // TODO: include handling for other errors when implemented in backend
                    JSONObject errorJSON = new JSONObject(new String(error.volleyError.networkResponse.data));
                    JSONArray firstNameErrors = errorJSON.optJSONArray("first_name");
                    JSONArray lastNameErrors = errorJSON.optJSONArray("last_name");
                    JSONArray emailErrors = errorJSON.optJSONArray("email");
                    JSONArray usernameErrors = errorJSON.optJSONArray("username");
                    JSONArray passwordErrors = errorJSON.optJSONArray("password");
                    if (firstNameErrors != null) {
                        firstName.setError(firstNameErrors.getString(0));
                        firstName.requestFocus();
                    }
                    if (lastNameErrors != null) {
                        lastName.setError(lastNameErrors.getString(0));
                        lastName.requestFocus();
                    }
                    if (emailErrors != null) {
                        email.setError(emailErrors.getString(0));
                        email.requestFocus();
                    }
                    if (usernameErrors != null) {
                        username.setError(usernameErrors.getString(0));
                        username.requestFocus();
                    }
                    if (passwordErrors != null) {
                        password.setError(passwordErrors.getString(0));
                        password.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("error", new String(error.volleyError.networkResponse.data));
            }
            @Override
            public void onListReady(List<User> users) {}
            @Override
            public void onObjectReady(User user) {
                Toast.makeText(RegisterActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                login(RegisterActivity.this);
            }
        });

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.password_confirm);
        registerButton = findViewById(R.id.register_button);
        loginTextButton = findViewById(R.id.login_text_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // deletes auth token before registration in case invalid (due to account deletion/changing auth token)
                // NOTE: will cause problems if registration is ever accessed by logged in user (shouldn't happen though)
                getSharedPreferences(Arguments.DEFAULT_SHARED_PREFERENCES, 0).edit().remove(Arguments.AUTH_TOKEN).apply();
                if (validateFields()) {
                    User user = User.builder()
                            .firstName(firstName.getText().toString())
                            .lastName(lastName.getText().toString())
                            .email(email.getText().toString())
                            .username(username.getText().toString())
                            .password(password.getText().toString())
                            .build();
                    userDAO.create(user);
                }
            }
        });

        loginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(RegisterActivity.this);
            }
        });

    }

    private boolean validateFields() {
        username.setError(null);
        password.setError(null);
        firstName.setError(null);
        lastName.setError(null);
        passwordConfirm.setError(null);
        email.setError(null);
        boolean valid = true;
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError(getString(R.string.error_field_required));
            firstName.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError(getString(R.string.error_field_required));
            lastName.requestFocus();
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Email must be valid");
            email.requestFocus();
            valid = false;
        }
        if(TextUtils.isEmpty(username.getText().toString())) { // TODO: implement username and password validation when/if standards established (to match backend)
            username.setError(getString(R.string.error_field_required));
            username.requestFocus();
            valid = false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Password must be greater than 0 characters");
            password.requestFocus();
            valid = false;
        }
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            password.setError("Passwords must match");
            password.requestFocus();
            valid = false;
        }
        return valid;
    }

}
