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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HttpUrlConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HttpUrlConnectionFragment extends Fragment {

    public static final String Name = "Classic";

    private EditText etEmail, etPassword;

    public HttpUrlConnectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HttpUrlConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HttpUrlConnectionFragment newInstance() {
        HttpUrlConnectionFragment fragment = new HttpUrlConnectionFragment();
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

    private class LoginApiTask extends AsyncTask<String, Void, Boolean> {

        private static final String TAG = "LoginApiTask";

        @Override
        protected Boolean doInBackground(String... params) {

            String email = params[0];
            String password = params[1];

            try {
                URL url = new URL(ServerAPI.LOGIN);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", email)
                        .appendQueryParameter("user_pw", password);
                String query = builder.build().getEncodedQuery();

                Log.d(TAG, query);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();

                byte[] buffer = new byte[8];
                int i;
                while ( (i = in.read(buffer)) != -1)
                {
                    stringBuilder.append(new String(buffer,0,i));
                }

                urlConnection.disconnect();

                String result = stringBuilder.toString();

                return result.equals("success");

            } catch (MalformedURLException e) {
                Log.d(TAG , e.getMessage());
                return false;
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Snackbar.make(getActivity().findViewById(R.id.main_content), "HttpUrlConnection : "+aBoolean.toString(), Snackbar.LENGTH_LONG).show();
        }

    }

}
