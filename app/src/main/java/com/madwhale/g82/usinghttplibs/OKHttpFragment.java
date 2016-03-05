package com.madwhale.g82.usinghttplibs;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.madwhale.g82.usinghttplibs.server.ServerAPI;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
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

    /**
     * using okhttp
     * https://github.com/square/okhttp
     */

    private void attemptLogin() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

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

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Snackbar.make(getActivity().findViewById(R.id.main_content), "Okhttp : " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Snackbar.make(getActivity().findViewById(R.id.main_content), "Okhttp : " + response.body().string(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

}
