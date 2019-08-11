package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.enter_email_signup)
    EditText mUserEmail;
    @BindView(R.id.enter_password_signup)
    EditText mUserPassword;
    @BindView(R.id.signup_btn)
    Button mSignUpButton;
    private String mPwd;
    private String mEmail;

    private VolleySingleton mVolleySingleton;

    public String mUrl = "http://139.59.65.145:9090/user/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mUserEmail.getText().toString();
                mPwd = mUserPassword.getText().toString();
                if(isValidEmail(mEmail)){
                    if(!isValidPassword(mPwd)){
                        setNewVolleyRequest();
                        postSignUpData(mEmail);
                    }else {
                        Toast.makeText(getApplicationContext(), "Don't keep if empty. Enter a Password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Don't Leave empty fields. Enter E-mail and Password ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setNewVolleyRequest() {
        mVolleySingleton = VolleySingleton.getInstance(this);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue requestQueue = mVolleySingleton.getRequestQueue(cache, network);
        requestQueue.start();
    }

    private boolean isValidEmail(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isValidPassword(String password){
        return (TextUtils.isEmpty(password));
    }

    private void postSignUpData(String emailId){
        JSONObject userCredential = new JSONObject();
        try{
            userCredential.put("email", emailId);
            userCredential.put("password", mUserPassword.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                mUrl, userCredential,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response-signUp", response.toString());
                        if (!response.isNull("data")) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                            Toast.makeText( getApplicationContext()," Thanks for being a part of our Family ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        mVolleySingleton.addToRequestQueue(request);
    }
}
