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

public class JoinActivity extends AppCompatActivity {
    //Pattern math = Pattern.compile();
    //Matcher matcher;
    EditText id;
    EditText num;
    EditText num2;
    EditText name;
    EditText year;
    Button overlap;
    Button join;
    HashMap<String,String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);                                //액션바 역할을 툴바로 하겠다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        params = new HashMap<>();
        id = (EditText)findViewById(R.id.j_id);
        num = (EditText)findViewById(R.id.j_num);
        num2 = (EditText)findViewById(R.id.j_num2);
        name = (EditText)findViewById(R.id.j_name);
        overlap = (Button)findViewById(R.id.jbtn_overlap);
        join = (Button)findViewById(R.id.jbtn_join);

        TextView titleName= (TextView)findViewById(R.id.toolbar_title);
        titleName.setText("로그인");

        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"아이디를 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                params.put("overlapid",id.getText().toString());
                new JoinTask("http://wndudrla1011.dothome.co.kr/Over.php", params).execute();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"아이디를 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(num.getText().toString().equals("") || num2.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"패스워드를 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!num.getText().toString().equals(num2.getText().toString())){
                    Toast.makeText(JoinActivity.this,"패스워드가 다릅니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                if(name.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"이름을 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }


                params.put("user", id.getText().toString());
                params.put("pw", num.getText().toString());
                params.put("name", name.getText().toString());
                new JoinTask("http://wndudrla1011.dothome.co.kr/Join.php", params).execute();
            }
        });
    }
    class JoinTask extends AsyncTask<Void,Void,String>{
        String url;
        HashMap<String,String> parmas;
        public JoinTask(String url, HashMap<String,String> params){
            this.url = url;
            this.parmas = params;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result = Networks.httpConnect(url,params);
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject json = new JSONObject(result);
                if(json.getBoolean("result")){
                    Toast.makeText(JoinActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                    Intent go = new Intent(JoinActivity.this,LoginActivity.class);
                    startActivity(go);
                }
                else if(json.getBoolean("overlap")){
                    Toast.makeText(JoinActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(JoinActivity.this,json.getString("message"),Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
