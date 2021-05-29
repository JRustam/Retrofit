package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        takeConfiguration();
    }

    private void takeConfiguration() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        IApi api = retrofit.create(IApi.class);

        Call<List<Response>> call = api.getPosts();

        call.enqueue(new Callback<List<Response>>() {

            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

                if (response.body() != null) {
                    for (Response item : response.body()) {
                        textView.append("UserId: " + item.getUserId()
                        + "\nId: " + item.getId() + "\nTitle: " + item.getTitle()
                        + "\nBody: " + item.getText());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {
                Log.e("MAinActivity", t.getMessage());
            }
        });
    }

}