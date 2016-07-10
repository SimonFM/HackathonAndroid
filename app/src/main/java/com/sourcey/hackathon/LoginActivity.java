package com.sourcey.hackathon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    //private static final String loginURL = "http://10.157.194.119:4000/login";
    private static final String loginURL = "http://10.0.2.2:4000/login";
    public static final String MyPREFERENCES = "MyPrefs";

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    SharedPreferences sharedpreferences;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        final boolean[] wasSuccess = {true};
        new Thread(new Runnable(){
            @Override
            public void run() {
                    // Your implementation goes here
//                     try{
//                        OkHttpClient client = new OkHttpClient();
//
//                        MediaType mediaType = MediaType.parse("application/json");
//                        RequestBody body = RequestBody.create(mediaType, "{\n    \"email\" : \""+email+"\",\n    \"password\" : \""+password+"\"\n}");
//                        Request request = new Request.Builder()
//                                .url(loginURL)
//                                .post(body)
//                                .addHeader("content-type", "application/json")
//                                .addHeader("cache-control", "no-cache")
//                                .build();
//
//                        Response response = client.newCall(request).execute();
//                        if(response.isSuccessful()){
//                            wasSuccess[0] = true;
//                        } else {
//                            wasSuccess[0] = false;
//                        }
//                }
//                catch (Exception ex) {
//                    ex.printStackTrace();
//                    wasSuccess[0] = false;
//                }
            }
        }).start();
        if(wasSuccess[0]){
            onLoginSuccess(email);
        } else{
            onLoginFailed();
        }
        progressDialog.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String user) {
        if(user.isEmpty()){
            user = "NiallTest@gmail.com";
        }
        _loginButton.setEnabled(true);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("CurrentUser", user);
        editor.commit();
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            //valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            //valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
