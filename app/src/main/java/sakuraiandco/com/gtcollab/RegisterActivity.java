package sakuraiandco.com.gtcollab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import sakuraiandco.com.gtcollab.constants.Arguments;
import sakuraiandco.com.gtcollab.domain.User;
import sakuraiandco.com.gtcollab.rest.UserDAO;
import sakuraiandco.com.gtcollab.rest.base.BaseDAO;
import sakuraiandco.com.gtcollab.rest.base.DAOListener;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button registerButton;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.password_confirm);
        registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testing", "register button clicked");

                // deletes auth token before registration in case invalid (due to account deletion/changing auth token)
                // NOTE: will cause problems if registration is ever accessed by logged in user (shouldn't happen though)
                context.getSharedPreferences(Arguments.AUTH_TOKEN_FILE, 0).edit().remove(Arguments.AUTH_TOKEN).apply();

                boolean valid = validateFields();
                if (valid) {
                    User user = User.builder()
                            .username(username.getText().toString())
                            .email(email.getText().toString())
                            .password(password.getText().toString())
                            .firstName("testing")
                            .lastName("testing")
                            .build();
                    new UserDAO(new DAOListener<User>() {
                        @Override
                        public void onDAOError(BaseDAO.Error error) {
                            try {
                                // TODO: include handling for other errors when implemented in backend
                                JSONObject errorJSON = new JSONObject(new String(error.volleyError.networkResponse.data));
                                JSONArray usernameErrors = errorJSON.optJSONArray("username");
                                JSONArray emailErrors = errorJSON.optJSONArray("email");
                                JSONArray passwordErrors = errorJSON.optJSONArray("password");
                                if (passwordErrors != null) {
                                    password.setError(passwordErrors.getString(0));
                                    password.requestFocus();
                                }
                                if (emailErrors != null) {
                                    email.setError(emailErrors.getString(0));
                                    email.requestFocus();
                                }
                                if (usernameErrors != null) {
                                    username.setError(usernameErrors.getString(0));
                                    username.requestFocus();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("error", new String(error.volleyError.networkResponse.data));
                        }

                        @Override
                        public void onListReady(List<User> users) {
                        }

                        @Override
                        public void onObjectReady(User user) {
                            // successful registration
                            Toast.makeText(context, "Registered successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    }).create(user);

                }
            }
        });

    }

    public void onLoginClick(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private boolean validateFields() {

        username.setError(null);
        password.setError(null);
        confirmPassword.setError(null);
        email.setError(null);


        boolean valid = true;
        // TODO: implement username and password validation when/if standards established (to match backend)

        String emailString = email.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Email must be valid");
            email.requestFocus();
            valid = false;
        }

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            password.setError("Passwords must match");
            password.requestFocus();
        }

        if(TextUtils.isEmpty(username.getText().toString())) {
            username.setError(getString(R.string.error_field_required));
            username.requestFocus();
            valid = false;
        }

        return valid;



    }



}
