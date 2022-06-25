package com.teamcom.smartwardrobe.MyCodiset;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamcom.smartwardrobe.R;

import java.util.ArrayList;
import java.util.List;


public class MyCodisetFragment extends Fragment {
    int ITEM_SIZE=4;

    public MyCodisetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View MCView =inflater.inflate(R.layout.fragment_my_codiset, container, false);

        //FAB관련
        FloatingActionButton addBtn = MCView.findViewById(R.id.codi_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //추가 가능 구현
            }
        });

        //카드뷰관련 리사이클뷰
        RecyclerView recyclerView = (RecyclerView) MCView.findViewById(R.id.codiset_RV);
        //리사이클뷰를 Linear Layout으로 설정하겠다고 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(MCView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클뷰에 카드뷰 아이템들을 넣는 작업
        List<CodisetItem> items = new ArrayList<>();
        CodisetItem[] item = new CodisetItem[ITEM_SIZE];

        /*
            여기에 item[i] = new CodisetItem(매개변수);  형식으로 값을 할당해주는 코드 필요
         */
        item[0] = new CodisetItem(0,3,1,2,0);
        item[1] = new CodisetItem(1,2,4,0);
        item[2] = new CodisetItem(2,3,3,5,0);
        item[3] = new CodisetItem(3,2,3,0);


        for (int i = 0; i < ITEM_SIZE; i++)
        {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new Codiset_RV_Adapter(MCView.getContext(), items ));

        return MCView;
    }


}
