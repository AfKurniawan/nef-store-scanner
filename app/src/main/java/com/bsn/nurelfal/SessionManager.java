package com.bsn.nurelfal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.bsn.nurelfal.Variable.ID_NASABAH;
import static com.bsn.nurelfal.Variable.KEY_ID_USER;
import static com.bsn.nurelfal.Variable.KEY_USER;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "login";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IS_LOGIN = "isLogin";
    public static final String KEY_IS_LOGOUT = "isLogout";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setSavedIdNasabah(String idNasabah){
        editor.putString(ID_NASABAH, idNasabah);
        editor.commit();
    }

    public String getSavedIdNasabah() {
        return pref.getString(ID_NASABAH, "");
    }

    //save user name to SharedPref
    public void setSavedUserName(String userName) {
        editor.putString(KEY_USER, userName);
        editor.commit();
    }

    public void setSavedIdUser(String idUser) {
        editor.putString(KEY_ID_USER, idUser);
        editor.commit();
    }

    //retrieve username frome pref
    public String getSavedUserName() {
        return pref.getString(KEY_USER, "");
    }

    public void setSavedPassword(String pass) {
        editor.putString(KEY_PASSWORD, pass);
        editor.commit();
    }

    public boolean isUserLogin() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void setUserLoggedIn(boolean isLogin) {
        editor.putBoolean(KEY_IS_LOGIN, isLogin);
        editor.commit();
    }

    public void setUserLogout(boolean isLogout) {
        editor.putBoolean(KEY_IS_LOGIN, isLogout);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
