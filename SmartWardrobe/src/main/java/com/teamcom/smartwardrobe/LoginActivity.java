package com.teamcom.smartwardrobe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by KMW on 2018-01-31.
 */

public class LoginActivity extends AppCompatActivity {
    EditText eid;
    EditText epw;
    Button btnlogin;
    Button btnjoin;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);                                //액션바 역할을 툴바로 하겠다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView titleName= (TextView)findViewById(R.id.toolbar_title);
        titleName.setText("로그인");

        eid = (EditText)findViewById(R.id.id);
        epw = (EditText)findViewById(R.id.pw);
        btnlogin = (Button)findViewById(R.id.login);
        btnjoin = (Button)findViewById(R.id.join);
        params = new HashMap<>();



        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(j);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.put("id", eid.getText().toString());
                params.put("pw", epw.getText().toString());
                new LoginTask("http://wndudrla1011.dothome.co.kr/LogCheck.php",params).execute();
            }
        });


    }
    class LoginTask extends AsyncTask<Void,Void,String> {
        String url;
        HashMap<String,String> params;
        public LoginTask(String url, HashMap<String,String> params){
            this.url = url;
            this.params = params;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result = Networks.httpConnect(url,params);
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean("result")) {
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject json = new JSONObject(result);
                if(json.getBoolean("result")){
                    Toast.makeText(LoginActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                    User.getInstance().id = eid.getText().toString();
                    User.getInstance().pw = json.getString("name");//키값으로 "name"가지고 옴.

                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
