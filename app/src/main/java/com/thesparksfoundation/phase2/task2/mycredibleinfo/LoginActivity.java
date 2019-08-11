package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.enter_email)
    EditText mUserEmail;
    @BindView(R.id.enter_password)
    EditText mUserPassword;
    @BindView(R.id.login_btn)
    Button mLoginButton;
    @BindView(R.id.signup_txt2)
    TextView mSignupButton;

    public RequestQueue mRequestQueue;
    private VolleySingleton mVolleySingleton;
    private SessionManager mSessionManager;

    public String mUrl = "http://139.59.65.145:9090/user/login";

    private String mId;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);


        mSessionManager = new SessionManager(getApplicationContext());
        setNewVolleyRequest();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLoginData();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setNewVolleyRequest(){
        mVolleySingleton = VolleySingleton.getInstance(this);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = mVolleySingleton.getRequestQueue(cache, network);
        mRequestQueue.start();


    }

    private void postLoginData(){
        JSONObject userCredential = new JSONObject();
        try {
            userCredential.put("email", mUserEmail.getText().toString());
            userCredential.put("password", mUserPassword.getText().toString());
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                mUrl, userCredential,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response-info", response.toString());
                        if (!response.isNull("status_message")) {
                            Toast.makeText(LoginActivity.this, "Enter correct Credentials else Be a part of Our Family by Signing up", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Happy to see you Back..!!", Toast.LENGTH_SHORT).show();

                            try {
                                mId = response.getJSONObject("data").getString("id");
                                mEmail = response.getJSONObject("data").getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mSessionManager.createLoginSession(mId, mEmail);
                            Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                            String showemail = mUserEmail.getText().toString();
                            intent.putExtra("Value", showemail);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        mRequestQueue.add(request);
    }

}
