package com.sourcey.hackathon;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private RequestQueue mQueue;
    RequestQueue queue;
    String signUpURL = "http://10.0.2.2:4000/user";
    final CountDownLatch countDownLatch = new CountDownLatch(1);
    final Object[] responseHolder = new Object[1];

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        queue = Volley.newRequestQueue(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup()  {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final boolean wasSuccess[] = {true};
        // TODO: Implement your own signup logic here.
        new Thread(new Runnable(){
            @Override
            public void run() {
                // Your implementation goes here
                try{
                   OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\n    \"id\" : \"1237281732173\",\n    \"name\" : \"" + name +"\",\n    \"password\" : \"" + password + "\",\n    \"email\" : \"" + email + "\",\n    \"phoneNumber\" : \"123456789\",\n    \"creditCardNumber\" : \"5555555555554444\",\n    \"expMonth\" : \"05\",\n    \"expYear\" : \"19\",\n    \"cvc\" : \"123\"\n}");
                    com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                            .url(signUpURL)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    com.squareup.okhttp.Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        wasSuccess[0] = true;
                    } else {
                        wasSuccess[0] = false;
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    wasSuccess[0] = false;
                }
            }
        }).start();
        if(wasSuccess[0]){
            onSignupSuccess();
        } else {
            onSignupFailed();
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}