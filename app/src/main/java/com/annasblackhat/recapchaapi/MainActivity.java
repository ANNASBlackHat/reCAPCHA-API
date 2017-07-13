package com.annasblackhat.recapchaapi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String mSiteKey;
    private String secretKey;
    private CheckBox checkBox;
    private boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get your key at => https://www.google.com/recaptcha/admin
        mSiteKey = "";
        secretKey = "";

        checkBox = (CheckBox) findViewById(R.id.cbx_verify);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!verified){
                    checkBox.setChecked(false);
                    processRecapcha();
                }else{
                    checkBox.setChecked(true);
                }
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .build();

        mGoogleApiClient.connect();
    }

    private void processRecapcha() {
        SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, mSiteKey)
                .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                    @Override
                    public void onResult(SafetyNetApi.RecaptchaTokenResult result) {
                        Status status = result.getStatus();
                        if ((status != null) && status.isSuccess()) {
                            verify(result.getTokenResult());
                        } else {
                            Toast.makeText(MainActivity.this, "Error occurred " +
                                    "when communicating with the reCAPTCHA service.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClick(View v) {
        if(checkBox.isChecked()){
            Toast.makeText(this, "Login success!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Please verify that you are not a robot!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void verify(String tokenResult) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.google.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecapchaService service = retrofit.create(RecapchaService.class);
        service.verify(secretKey, tokenResult)
                .enqueue(new Callback<Recapcha>() {
                    @Override
                    public void onResponse(Call<Recapcha> call, Response<Recapcha> response) {
                        if(response.isSuccessful()){
                            //check status
                            verified = response.body().getSuccess();
                            if(verified){
                                checkBox.setChecked(true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Recapcha> call, Throwable t) {

                    }
                });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed...", Toast.LENGTH_SHORT).show();
    }
}
