package com.madwhale.g82.usinghttplibs;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.madwhale.g82.usinghttplibs.server.ServerAPI;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OKHttpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OKHttpFragment extends Fragment {

    public static final String Name = "OKHttp";

    private EditText etEmail, etPassword;

    public OKHttpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HttpUrlConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OKHttpFragment newInstance() {
        OKHttpFragment fragment = new OKHttpFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.common_login, container, false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        return view;
    }

    private void attemptLogin() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        LoginApiTask loginApiTask = new LoginApiTask();
        loginApiTask.execute(email, password);

    }

    /**
     *
     * using okhttp
     *
     * https://github.com/square/okhttp
     *
     */

    private class LoginApiTask extends AsyncTask<String, Void, Boolean> {

        private static final String TAG = "LoginApiTask";

        @Override
        protected Boolean doInBackground(String... params) {

            String email = params[0];
            String password = params[1];

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            OkHttpClient client = new OkHttpClient();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("user_id", email)
                    .appendQueryParameter("user_pw", password);
            String content = builder.build().getEncodedQuery();

            RequestBody body = RequestBody.create(mediaType, content);
            Request request = new Request.Builder()
                    .url(ServerAPI.LOGIN)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                return result.equals("success");

            } catch (IOException e) {
                Log.d("usingOkHttp", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Snackbar.make(getActivity().findViewById(R.id.main_content), "OkHttp : " + aBoolean.toString(), Snackbar.LENGTH_LONG).show();
        }

    }

}
