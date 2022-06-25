package com.teamcom.smartwardrobe.Main;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teamcom.smartwardrobe.MainActivity;
import com.teamcom.smartwardrobe.MyWardrobe.ClothesItem;
import com.teamcom.smartwardrobe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainFragment extends Fragment {

    ImageView P_weather;
    TextView DesWeather;
    TextView temp;
    TextView local;
    Button B_weather;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View MFView =inflater.inflate(R.layout.fragment_main, container, false);


        P_weather = (ImageView)MFView.findViewById(R.id.P_weather);
        DesWeather = (TextView)MFView.findViewById(R.id.DesWeather);
        temp = (TextView)MFView.findViewById(R.id.temp);
        local = (TextView)MFView.findViewById(R.id.local);
        B_weather = (Button)MFView.findViewById(R.id.B_weather);

        B_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWeather(MFView.getContext());

            }
        });

        getWeather(MFView.getContext());

        return MFView;
    }

    class WeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        public void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        public JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while((readed = in.readLine()) != null){
                        JSONObject jObject = new JSONObject(readed);
                        return jObject;
                    }
                } else{
                    return null;
                }
                return null;
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject result){
            Log.i("TAG", result.toString());
            if(result != null){
                try{
                    String tempa = result.getJSONObject("main").getString("temp");
                    //String min_temp = result.getJSONObject("main").getString("temp_min");
                    //String max_temp = result.getJSONObject("main").getString("temp_max");
                    String description = result.getJSONArray("weather").getJSONObject(0).getString("description");
                    String locals = result.getString("name");
                    description = trans(description);

                    //ImageLoader.getInstance().displayImage(trans_weather(description), P_weather);
                    P_weather.setBackgroundResource(trans_weather(description));
                    DesWeather.setText(description);
                    temp.setText(tempa+"℃");
                    local.setText(locals);


                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }

    private int trans_weather(String des){
        if(des.equals("안개")){
            return R.drawable.ic_weather_fog;
        }

        if(des.equals("구름 많음") || des.equals("구름 낌")){
            return R.drawable.ic_weather_cloudy;
        }

        if(des.equals("구름") || des.equals("구름 조금")){
            return R.drawable.ic_weather_partlycloudy;
        }

        if(des.equals("맑음")){
            return R.drawable.ic_weather_sunny;
        }

        return R.drawable.ic_weather_sunny;
    }

    private String trans(String des){

        des = des.toLowerCase();

        if(des.equals("haze")){
            return "안개";
        }

        if(des.equals("broken clouds")){
            return "구름 많음";
        }

        if(des.equals("fog")){
            return "안개";
        }

        if(des.equals("clouds")){
            return "구름";
        }

        if(des.equals("few clouds")){
            return "구름 조금";
        }

        if(des.equals("scattered clouds")){
            return "구름 낌";
        }

        if(des.equals("overcast clouds")){
            return "구름 많음";
        }

        if(des.equals("clear sky")){
            return "맑음";
        }

        return des;

    }
    private void getWeather(Context context){
        String url = ((MainActivity)MainActivity.MA_Context).useGpsUrl();

        if(url=="-1")       //-1은 오류 메세지
        {
            Toast.makeText(context,"GPS 일기 오류 입니다.",Toast.LENGTH_SHORT);
        }
        else    // 메인 엑티비티에서 GPS 링크를 받아 날씨작업 실행
        {
            WeatherTask weatherTask = new WeatherTask();
            weatherTask.execute(url);
        }
    }
}
