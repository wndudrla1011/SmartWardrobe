package com.teamcom.smartwardrobe.Calendar;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.teamcom.smartwardrobe.Calendar.Decorator.EventDecorator;
import com.teamcom.smartwardrobe.Calendar.Decorator.OneDayDecorator;
import com.teamcom.smartwardrobe.Calendar.Decorator.SaturdayDecorator;
import com.teamcom.smartwardrobe.Calendar.Decorator.SundayDecorator;
import com.teamcom.smartwardrobe.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment{

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView calendarView;
    TextView textView;                          //임시용 추후 코디셋 정보를 보여줄 공간

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View CalView =inflater.inflate(R.layout.fragment_calendar, container, false);

        //달력 주소받아옴
        calendarView = (MaterialCalendarView) CalView.findViewById(R.id.calendarView);

        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) //달력의 시작
                .setMaximumDate(CalendarDay.from(2030,0,1))   //달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)                   //월 단위로 표시
                .commit();

        /**
         * 달력의 토요일 - 파란색
         * 일요일 - 빨간색으로 표시
         * 오늘 - 초록색으로 표시    -> 추후 변경가능
         */
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);


        /**
         *   현재 result 값은 임의 지정
         *   추후 서버에서 코디셋을 입은 날짜 정보를 받아와서 저장
         */
        String[] result = {"2018,05,12", "2018,04,30", "2018,05,03"};

        /* 입은 코디셋 정보가 있는 날에 달력에 표시 */
        new Mark_Calendar(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        /**
         *  달력을 클릭할 경우 선택한 날짜에 대한 정보 Toast 메세지를 띄움
         *  if(codyset_information_exist true)
         *      해당 날짜 코디셋 정보를 띄워줌
         *  else
         *      정보 없음
         */
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "년 " + Month + "월 " + Day + "일";

                Log.i("shot_Day test", shot_Day + "");
                calendarView.clearSelection();                   //선택한 상태를 유지하려면 주석처리

                Toast.makeText(getActivity(), shot_Day , Toast.LENGTH_SHORT).show();
            }
        });

        return CalView;
    }

    /* 특정 날짜에 Mark 하기 */
    private  class Mark_Calendar extends AsyncTask<Void,Void,List<CalendarDay>>{

        String[] Time_Result;

        Mark_Calendar(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정 날짜 달력에 점 표시*/
            /*월은 0부터 시작하므로 1을 더해야함
             * 여기선 임의로 지정한 날짜가 그대로 입력받기 때문에 역으로 1을 빼줌
             * ex)2018,05,12 -> 05 그대로 입력받음 -> 실제 5월을 가르키는 값은 4
             */

            for(int i=0; i<Time_Result.length; i++) {

                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year, month-1, dayy);                 //날짜 정보 calendar에 저장

                CalendarDay day = CalendarDay.from(calendar);             //calendar에 있는 정보 day에 저장
                dates.add(day);
            }
            return  dates;
        }

        @Override
        protected  void onPostExecute(List<CalendarDay> calendarDays){
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, CalendarFragment.this));
        }
    }

}
