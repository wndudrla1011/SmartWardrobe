package com.teamcom.smartwardrobe.MyWardrobe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.teamcom.smartwardrobe.R;


public class MyWardrobeFragment extends Fragment {

    ViewPager viewPager;

    public MyWardrobeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //MWView 객체를 통해 액티비티처럼 활용가능
        final View MWView =inflater.inflate(R.layout.fragment_my_wardrobe, container, false);

        /*
        이하 Pager관련
         */

        viewPager = (ViewPager) MWView.findViewById(R.id.viewpager);
        //페이지를 양옆 2개 미리띄어둔다 페이지 이동간에 다시 데이터로드가 발생하기때문에 설정함.
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) MWView.findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        FloatingActionButton addBtn =MWView.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DialogFragment_clothes dialogFragment = new DialogFragment_clothes();
                Bundle args= new Bundle();
                //다이얼로그의 옵션을 Detail로 설정
                args.putInt("opt", DialogFragment_clothes.OPT_NEW);

                dialogFragment.setArguments(args);
                dialogFragment.show(manager,null);

            }
        });



        return MWView;
    }





}
