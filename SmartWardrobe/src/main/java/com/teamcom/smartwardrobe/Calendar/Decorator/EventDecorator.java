package com.teamcom.smartwardrobe.Calendar.Decorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.teamcom.smartwardrobe.R;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates,Fragment context) {
        drawable = context.getResources().getDrawable(R.drawable.cal_mark);
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);        // 해당 날짜 윤곽처리
        view.addSpan(new DotSpan(5, color)); // 날짜 밑에 점(반지름 크기, 색상) -> 색상은 CalendarFragment 에서 수정
    }
}