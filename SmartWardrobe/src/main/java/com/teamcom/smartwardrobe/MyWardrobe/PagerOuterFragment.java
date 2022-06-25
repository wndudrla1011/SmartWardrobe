package com.teamcom.smartwardrobe.MyWardrobe;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamcom.smartwardrobe.Networks;
import com.teamcom.smartwardrobe.R;
import com.teamcom.smartwardrobe.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PagerOuterFragment extends Fragment {
    View views;
    RecyclerView recyclerView;

    static List<ClothesItem> items;
    HashMap<String,String> params;

    private static int itemsSize;

    public PagerOuterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        new Outer_data("http://wndudrla1011.dothome.co.kr/Bring_cloths.php",params).execute();
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        items = new ArrayList<>();
//        params = new HashMap<>();
//        params.put("user", User.getInstance().id);
//        new Outer_data("http://wndudrla1011.dothome.co.kr/Bring_Outer_cloths.php",params).execute();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.viewpager_outer, container, false);
        views = view;
        //카드뷰관련 리사이클뷰
        recyclerView = (RecyclerView) view.findViewById(R.id.clothesRV);
        //리사이클뷰를 GridLayout으로 하겠다고 설정, spanCount:3으로 열설정
        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클뷰에 카드뷰 아이템들을 넣는 작업
        items = new ArrayList<>();
        params = new HashMap<>();
        params.put("user", User.getInstance().id);
        new Outer_data("http://wndudrla1011.dothome.co.kr/Bring_Outer_cloths.php",params).execute();

        return view;
    }

    public class Outer_data extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String,String>  params;
        public Outer_data(String url, HashMap<String,String> params) {
            this.url = url;
            this.params = params;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = Networks.httpConnect(url,params);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean("result")) {
                    String array = json.getString("rows");
                    JSONArray arr = new JSONArray(array);
                    for(int i=0; i<arr.length(); i++){
                        JSONObject object = arr.getJSONObject(i);
                        ClothesItem loadItem = new ClothesItem(
                                object.getString("id"),
                                object.getInt("type"),
                                object.getInt("color"),
                                object.getInt("minTem"),
                                object.getInt("maxTem"),
                                object.getString("imgSrc"),
                                object.getBoolean("isExist"),
                                object.getInt("wearCount"));


                        items.add(loadItem);
                        //이 코드로 인하여 현재 아이템이 몇 개나 들어있는가 갱신
                        itemsSize = items.size();
                    }

                    recyclerView.setAdapter(new ClothesAdapter(views.getContext(),items));

                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getItemsSize()
    {
        return itemsSize;
    }
}
