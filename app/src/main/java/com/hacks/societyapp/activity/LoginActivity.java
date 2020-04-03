package com.hacks.societyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hacks.societyapp.MainActivity;
import com.hacks.societyapp.R;
import com.hacks.societyapp.model.Authentication;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;

import org.jetbrains.annotations.NotNull;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ExtendedEditText mEmailEditText;
    private ExtendedEditText mPasswordEditText;
    private CircularProgressButton mProgressButton;
    private SocietyAPI mSocietyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.email_address);
        mPasswordEditText = findViewById(R.id.password);
        mProgressButton = findViewById(R.id.register);
        TextView signup = findViewById(R.id.signup_activity);

        signup.setOnClickListener(view -> {
           startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        mProgressButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            mProgressButton.startAnimation();
            loginUser(mEmailEditText.getEditableText().toString(),
                    mPasswordEditText.getEditableText().toString());
        }
    }

    private void loginUser(String email, String password) {
        mSocietyAPI = SocietyClient.getClient(getBaseContext())
                .create(SocietyAPI.class);
        mSocietyAPI.loginUser(email, password)
                .enqueue(new Callback<Authentication>() {
                    @Override
                    public void onResponse(@NotNull Call<Authentication> call,
                                           @NotNull Response<Authentication> response) {
                        Authentication loginAuth = response.body();
                        Timber.d(response.toString());
                        if (loginAuth.getSuccessMsg().equals("true")) {
                            mProgressButton.doneLoadingAnimation(
                                    R.color.colorPrimary,
                                    ((BitmapDrawable) ResourcesCompat.getDrawable
                                            (getResources(), R.drawable.update, null)).getBitmap()
                            );
                            Intent intent = new Intent(
                                    LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Error: " + loginAuth.getErrorCode(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Authentication> call,
                                          @NotNull Throwable t) {
                        Timber.d(t);
                    }
                });
    }
}
