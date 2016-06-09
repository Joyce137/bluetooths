package com.health.app.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.health.app.MainApplication;
import com.loopj.android.http.RequestParams;
import com.mslibs.api.BaseApi;
import com.mslibs.api.CallBack;
import com.mslibs.api.ResponseException;

public class Api extends BaseApi {
    protected String TAG = "Api";
    private MainApplication mApp;

    boolean hastoken = false;
    private RequestParams params = new RequestParams();

    public Api() {
        super();
    }

    public Api(CallBack callback, MainApplication mainApp) {
        super(callback);

        setLog(true);

        if (mainApp != null) {
            mApp = mainApp;
            String token = mApp.getToken();
            if (!TextUtils.isEmpty(token)) {
                hastoken = true;
                params.put("token", token);
            }
        }
    }

    // public Api(CallBack callback) {
    // super(callback);
    // }

    public boolean checkLogin() {
        if (hastoken == false) {
            if (mCallBack != null) {
                // mCallBack.onFailure("请先登录");
            }
        }
        return !hastoken;
    }

    public String parse(String response) throws ResponseException,
            JSONException {
        JSONObject json = new JSONObject(response);
        Iterator<String> it = (Iterator<String>) json.keys();
        int success = 0;
        String data = null;
        String error = "";
        int code = 0;
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.equals("success")) {
                success = json.getInt(key);
            } else if (key.equals("message")) {
                error = json.getString(key);
            } else if (key.equals("data")) {
                data = json.getString(key);
            } else if (key.equals("code")) {
                code = json.getInt(key);
                if (code > 0 && mCallBack != null) {
                    mCallBack.setCode(code);
                }
            }
        }
        if (success == 1) {
            return data;
        } else {
			/*
			 * if (code == 400) { Intent intent = new
			 * Intent().setAction("action.finish");
			 * mApp.getApplicationContext().sendBroadcast(intent); }
			 */
            throw new ResponseException(error);
        }
    }

    public void test(String response) {
        // handler.onSuccess(response);
    }

    public void test() {
        String response = "{\"success\":1,\"data\":[" + "{\"title\":\"标题1\"},"
                + "{\"title\":\"标题2\"}," + "{\"title\":\"标题3\"},"
                + "{\"title\":\"标题4\"}," + "{\"title\":\"标题5\"},"
                + "{\"title\":\"标题6\"}," + "{\"title\":\"标题7\"},"
                + "{\"title\":\"标题8\"}," + "{\"title\":\"标题9\"},"
                + "{\"title\":\"标题10\"}," + "{\"title\":\"标题11\"}]}";
        // handler.onSuccess(response);
    }

    public void version() {
        Client.get("api/app/check_version", params, handler);
    }

    // 获取验证码
    public void getCode(String mobile, int type) {
        params.put("mobile", mobile);
        params.put("type", type);
        Client.get("api/common/getCode", params, handler);
    }

    public void signIn(String tel, String password) {
        params.put("tel", tel);
        params.put("password", password);
        Client.get("api/user/signIn", params, handler);
    }

    public void resetPwd(String tel, String password, String rpassword, String code ) {

        params.put("tel", tel);
        params.put("password", password);
        params.put("rpassword", rpassword);
        params.put("code", code);

        Client.get("api/user/resetPwd", params, handler);
    }

    public void signUp(String tel, String password, String rpassword, String code,
                       String real_name, String id_card, String contact) {

        params.put("tel", tel);
        params.put("password", password);
        params.put("rpassword", rpassword);
        params.put("code", code);
        params.put("real_name", real_name);
        params.put("id_card", id_card);
        params.put("contact", contact);
        Client.get("api/user/signUp", params, handler);
    }
    public void updatePwd(String opwd, String pwd, String rpwd) {

        params.put("opwd", opwd);
        params.put("pwd", pwd);
        params.put("rpwd", rpwd);


        Client.get("api/user/updatePwd", params, handler);
    }
    public void updateInfo(String real_name, String age, String sex, String contact,String nick ) {

        params.put("real_name", real_name);
        params.put("age", age);
        params.put("sex", sex);//1=男 2=女
        params.put("contact", contact);
        params.put("nick", nick);
        Client.get("api/user/updateInfo", params, handler);
    }
    public void getInfo() {
        Client.get("api/user/getInfo", params, handler);
    }
    public void updateAvatar(String avatar) {

        if (!TextUtils.isEmpty(avatar)) {
            Log.e(TAG, "upload image " + avatar);
            File myFile = new File(avatar);
            try {
                params.put("avatar", myFile, "image/jpeg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Client.post("api/user/updateAvatar", params, handler);
    }
    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String message, int type) {

    }

    @Override
    public void start() {

    }

    @Override
    public void retry() {

    }

}
