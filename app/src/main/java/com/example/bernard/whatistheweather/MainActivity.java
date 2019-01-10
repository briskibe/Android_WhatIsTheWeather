package com.example.bernard.whatistheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    EditText qryCity;
    TextView txtMain;
    TextView txtDesc;

    protected class GetWeatherJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String retVal = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char c = (char) data;
                    retVal += c;
                    data = reader.read();
                }

                return retVal;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject json = new JSONObject(result);
                JSONArray partJson = json.getJSONArray("weather");
                for (int i = 0; i < partJson.length(); i++) {
                    JSONObject weatherJSON = partJson.getJSONObject(i);
                    txtMain.setText(weatherJSON.getString("main"));
                    txtDesc.setText(weatherJSON.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                txtMain.setText("");
                txtDesc.setText("");
                qryCity.setText("");
                return;
            }
        }
    }

    public void getWeatherInfo(View view) {

        String rawJSON = null;
        GetWeatherJSON task = new GetWeatherJSON();

        String city = qryCity.getText().toString();
        String queryString = "https://openweathermap.org/data/2.5/weather?q=" +
        city +
        "&appid=b6907d289e10d714a6e88b30761fae22";

        try {
            rawJSON = task.execute(queryString).get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qryCity = (EditText) findViewById(R.id.textCity);
        txtMain = (TextView) findViewById(R.id.txtMain);
        txtDesc = (TextView) findViewById(R.id.txtDescription);
    }
}
