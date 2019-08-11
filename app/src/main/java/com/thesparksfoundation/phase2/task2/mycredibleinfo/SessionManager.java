package com.thesparksfoundation.phase2.task2.mycredibleinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;

    Editor editor;

    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UserCredential";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USERID = "userId";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_DELETE_ID = "deleteId";


    public SessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String userId, String email){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERID, userId);

        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    public void createDeletedId(String id){
        editor.putString(KEY_DELETE_ID, id);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_DELETE_ID, pref.getString(KEY_DELETE_ID, null));

        return user;
    }

    public void logoutUser(){

        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
