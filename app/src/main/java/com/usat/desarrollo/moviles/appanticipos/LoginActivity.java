package com.usat.desarrollo.moviles.appanticipos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiAdapter;
import com.usat.desarrollo.moviles.appanticipos.data.remote.api.ApiService;
import com.usat.desarrollo.moviles.appanticipos.data.remote.response.LoginResponse;
import com.usat.desarrollo.moviles.appanticipos.domain.modelo.DatosSesion;
import com.usat.desarrollo.moviles.appanticipos.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText txtEmail, txtPassword;
    Button btnLogin;
    ProgressBar progressBar;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txt_login_email);
        txtPassword = findViewById(R.id.txt_login_password);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        apiService = ApiAdapter.getApiService();

        login(txtEmail.getText().toString(), Helper.convertPassMd5(txtPassword.getText().toString()));
    }

    private void login(String email, String password) {
        btnLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            apiService.getSesion(email, password).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.code() == 200) {
                        LoginResponse loginResponse = response.body();
                        boolean status = loginResponse.getStatus();
                        if (status) {
                            DatosSesion.USUARIO_NOMBRE = loginResponse.getData().getNombres() + " " + loginResponse.getData().getApellidos();
                            DatosSesion.USUARIO_EMAIL = loginResponse.getData().getEmail();
                            DatosSesion.TOKEN = loginResponse.getData().getToken();
                            DatosSesion.USUARIO_ID = loginResponse.getData().getId();
                            Snackbar
                                    .make(findViewById(R.id.layoutLogin), loginResponse.getMessage(), Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(ContextCompat.getColor(LoginActivity.this, R.color.primaryDarkColor))
                                    .show();
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }
                    } else {
                        try {
                            JSONObject jsonError = new JSONObject(response.errorBody().string());
                            String message =  jsonError.getString("message");
                            Snackbar
                                    .make(findViewById(R.id.layoutLogin), message, Snackbar.LENGTH_LONG)
                                    .setBackgroundTint(ContextCompat.getColor(LoginActivity.this, R.color.error))
                                    .show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("LoginError", t.getMessage());
                }
            });
        });
    }
}