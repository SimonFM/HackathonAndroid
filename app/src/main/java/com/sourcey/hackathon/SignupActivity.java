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

        final String[] names = name.split(" ");
        final JSONObject params = new JSONObject();
        try{
            params.put("expMonth", "01");
            params.put("id", "1237281732173");
            params.put("expYear", "99");
            params.put("number", "5555555555554444");
            params.put("cvc", "123");
            params.put("fname", names[0]);
            params.put("lname", names[1]);
            params.put("email", email);
            params.put("password", password);
            params.put("phoneNumber", "+353 86 1234 789");

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        // TODO: Implement your own signup logic here.
           new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {

                            try{
                                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, signUpURL, params,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                System.out.println(response);
                                                onSignupSuccess();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                onSignupFailed();
                                            }
                                        });
                                queue.add(jsObjRequest);

                            } catch (Exception ex) {

                            }
                            progressDialog.dismiss();
                        }
                    }, 3000);

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