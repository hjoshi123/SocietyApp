package com.hacks.societyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hacks.societyapp.R;
import com.hacks.societyapp.model.Authentication;
import com.hacks.societyapp.retrofitapi.SocietyAPI;
import com.hacks.societyapp.retrofitclient.SocietyClient;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import timber.log.Timber;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ExtendedEditText mEmailEditText;
    private ExtendedEditText mPasswordEditText;
    private ExtendedEditText mEmplNumberEditText;
    private CircularProgressButton mProgressButton;
    private SocietyAPI mSocietyAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailEditText = findViewById(R.id.email_address);
        mPasswordEditText = findViewById(R.id.password);
        mEmplNumberEditText = findViewById(R.id.empl_number);
        mProgressButton = findViewById(R.id.register);
        TextView login = findViewById(R.id.login_activity);

        login.setOnClickListener(view -> {
           startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        mProgressButton.setOnClickListener(this);
    }

    private void registerUser(String email, String password, String emplNumber) {
        mSocietyAPI = SocietyClient.getClient(getBaseContext())
                .create(SocietyAPI.class);
        mSocietyAPI.registerUser(emplNumber, email, password)
            .enqueue(new Callback<Authentication>() {
                @Override
                public void onResponse(@NotNull Call<Authentication> call,
                                       @NotNull Response<Authentication> response) {
                    Authentication auth = response.body();

                    if (auth.getSuccessMsg().equals("true")) {
                        mSocietyAPI.loginUser(email, password)
                            .enqueue(new Callback<Authentication>() {
                                @Override
                                public void onResponse(@NotNull Call<Authentication> call,
                                                       @NotNull Response<Authentication> response) {
                                    Authentication loginAuth = response.body();

                                    if (loginAuth.getSuccessMsg().equals("true")) {
                                        mProgressButton.doneLoadingAnimation(
                                                R.color.colorPrimary,
                                                ((BitmapDrawable) ResourcesCompat.getDrawable
                                                        (getResources(), R.drawable.update,
                                                                null)).getBitmap()
                                        );
                                        Intent intent = new Intent(
                                                SignUpActivity.this,
                                                MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(SignUpActivity.this,
                                                "Error: " + loginAuth.getErrorCode(),
                                                Toast.LENGTH_SHORT).show();
                                        mProgressButton.recoverInitialState();
                                        mProgressButton.revertAnimation();
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<Authentication> call,
                                                      @NotNull Throwable t) {
                                    Timber.d(t);
                                    mProgressButton.recoverInitialState();
                                    mProgressButton.revertAnimation();
                                }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                "Error Registering.. Please try again",
                                Toast.LENGTH_SHORT).show();
                        mProgressButton.recoverInitialState();
                        mProgressButton.revertAnimation();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Authentication> call, @NotNull Throwable t) {
                    Timber.d(t);
                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences mcpPreferences = getSharedPreferences("cookie",
                Context.MODE_PRIVATE);
        HashSet<String> cookie =
                (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
        if (!cookie.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            mProgressButton.startAnimation();
            registerUser(mEmailEditText.getEditableText().toString(),
                mPasswordEditText.getEditableText().toString(),
                mEmplNumberEditText.getEditableText().toString());
        }
    }
}
