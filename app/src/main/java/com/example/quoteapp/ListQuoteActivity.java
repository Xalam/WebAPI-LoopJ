package com.example.quoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListQuoteActivity extends AppCompatActivity {
    private static final String TAG = ListQuoteActivity.class.getSimpleName();
    ProgressBar progressBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quote);

        listView = findViewById(R.id.listQuotes);
        progressBar = findViewById(R.id.progressBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("List of Quotes");
        }

        getListQuotes();
    }

    private void getListQuotes() {
        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://programming-quotes-api.herokuapp.com/quotes/page/1";
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                ArrayList<String> listQuote = new ArrayList<>();

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String quote = jsonObject.getString("en");
                        String author = jsonObject.getString("author");
                        listQuote.add("\n" + quote + "\n — " + author + "\n");
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<>(ListQuoteActivity.this, android.R.layout.simple_list_item_1, listQuote);
                    listView.setAdapter(arrayAdapter);

                } catch (Exception e) {
                    Toast.makeText(ListQuoteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
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
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(ListQuoteActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
