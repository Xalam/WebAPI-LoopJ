package com.example.quoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvQuote, tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        progressBar = findViewById(R.id.progressBar);

        getRandomQuote();
    }

    private void getRandomQuote(){
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://programming-quotes-api.herokuapp.com/quotes/random";
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                String result = new String(responseBody);
                String TAG = "Result : ";
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String quote = jsonObject.getString("en");
                    String author = jsonObject.getString("author");

                    tvQuote.setText(quote);
                    tvAuthor.setText(author);
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
