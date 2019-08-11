package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EducationDetailsActivity extends AppCompatActivity {

    @BindView(R.id.university_name_edu_edit_text)
    EditText mUniversityName;
    @BindView(R.id.stream_name_edu_edit_text)
    EditText mStreamName;
    @BindView(R.id.city_name_edu_edit_text)
    EditText mCityName;
    @BindView(R.id.start_date_edu_edit_text)
    EditText mStartDate_edu;
    @BindView(R.id.end_date_edu_edit_text)
    EditText mEndDate_edu;
    @BindView(R.id.save_edu_details_button)
    Button mSaveButton_edu;
    private String mUrl = "http://139.59.65.145:9090//user/educationdetail/";
    private SessionManager mSessionManager;
    private List<UserEducationData> mData;
    private VolleySingleton mVolleySingleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        mSessionManager = new SessionManager(this);
        mData = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("user_id");
        final String flag = bundle.getString("flag");
        String College = bundle.getString("university_name");
        String streamName = bundle.getString("stream_name");
        String City = bundle.getString("location");
        String StartDate = bundle.getString("start_date");
        String EndDate = bundle.getString("end_date");


        if(flag.equals("Add")){
            mUniversityName.setText("");
            mStreamName.setText("");
            mCityName.setText("");
            mStartDate_edu.setText("");
            mEndDate_edu.setText("");
        }else if (flag.equals("Edit")){
            mUniversityName.setText(College);
            mStreamName.setText(streamName);
            mCityName.setText(City);
            mStartDate_edu.setText(StartDate);
            mEndDate_edu.setText(EndDate);
        }

        mSaveButton_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewVolleyRequest();
                if (flag.equals("Add")) {
                    setNewData(id, Request.Method.POST);
                } else if (flag.equals("Edit")) {
                    setNewData(id, Request.Method.PUT);
                }

            }


        });
    }


    private void setNewVolleyRequest(){
        mVolleySingleton = VolleySingleton.getInstance(this);
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue requestQueue = mVolleySingleton.getRequestQueue(cache, network);
        requestQueue.start();
    }

    private void setNewData(String id, int method){
        setUserData();
        mUrl += id;
        JSONObject userCredential = new JSONObject();
        try {
            userCredential.put("organisation", mData.get(0).getmUniversityName());
            userCredential.put("degree", mData.get(0).getmStreamName());
            userCredential.put("location", mData.get(0).getmCityName());
            userCredential.put("start_year", mData.get(0).getmStartYear());
            userCredential.put("end_year", mData.get(0).getmEndYear());
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(method,
                mUrl, userCredential,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response-edu-info", response.toString());
                        Toast.makeText(getApplicationContext(), "Your Details Are Successfully Added", Toast.LENGTH_SHORT).show();
                        String id = null;
                        try {
                            id = response.getJSONObject("data").getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mSessionManager.createDeletedId(id);
                        Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });

        mVolleySingleton.addToRequestQueue(request);


    }

    public void setUserData(){

        String UniversityName = mUniversityName.getText().toString().trim();
        String streamName = mStreamName.getText().toString().trim();
        String cityName = mCityName.getText().toString().trim();
        String startDate = mStartDate_edu.getText().toString().trim();
        String endDate = mEndDate_edu.getText().toString().trim();

        mData.add(new UserEducationData(UniversityName, streamName, cityName, startDate, endDate));
    }


}
