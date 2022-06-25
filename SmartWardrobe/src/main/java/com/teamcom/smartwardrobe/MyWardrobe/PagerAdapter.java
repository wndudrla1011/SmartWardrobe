package com.teamcom.smartwardrobe.MyWardrobe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

//내 옷장의 레이아웃 구성을 관리하는 PagerAdapter
public class PagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String tabTitles[] = new String[]{"외투", "상의", "하의"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PagerOuterFragment();
        } else if (position == 1) {
            return new PagerTopFragment();
        } else {
            return new PagerBottomFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}

