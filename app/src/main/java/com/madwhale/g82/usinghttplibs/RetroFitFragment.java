package com.madwhale.g82.usinghttplibs;


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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RetroFitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RetroFitFragment extends Fragment {

    public static final String Name = "RetroFit";

    private EditText etEmail, etPassword;
    private LoginService loginService;

    public RetroFitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment HttpUrlConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RetroFitFragment newInstance() {
        RetroFitFragment fragment = new RetroFitFragment();
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                .build();
        loginService = retrofit.create(LoginService.class);

        return view;
    }

    /**
     * using Retrofit 2
     *
     * https://square.github.io/retrofit/
     *
     */

    public interface LoginService {
        @FormUrlEncoded
        @POST("login.php")
        Call<ResponseBody> login(@Field("user_id") String id, @Field("user_pw") String pw);
    }

    private void attemptLogin() {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Call<ResponseBody> call = loginService.login(email, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Snackbar.make(getActivity().findViewById(R.id.main_content), "RetroFit : " + response.body().string(), Snackbar.LENGTH_LONG).show();
                } catch (IOException e) {
                    Snackbar.make(getActivity().findViewById(R.id.main_content), "RetroFit : " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    Log.d("RetroFit", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(R.id.main_content), "RetroFit : " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                Log.d("RetroFit", t.getMessage());
            }
        });

    }

}
