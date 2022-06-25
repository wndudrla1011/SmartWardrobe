package com.teamcom.smartwardrobe;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.teamcom.smartwardrobe.Board.BoardFragment;
import com.teamcom.smartwardrobe.Calendar.CalendarFragment;
import com.teamcom.smartwardrobe.Main.MainFragment;
import com.teamcom.smartwardrobe.MyCodiset.MyCodisetFragment;
import com.teamcom.smartwardrobe.MyWardrobe.MyWardrobeFragment;
import com.teamcom.smartwardrobe.Recommend.RecommendFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    TextView titleName;

    //Menu login;
    //MenuItem mlogin;

    // GPSTracker class
    private Gps gps;

    public static Context MA_Context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MA_Context=this;    // 다른 액티비티나 프래그먼트에서 이 액티비티의 메소드를 사용하기 위함.

        /*
            이하 3줄 툴바관련
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);                                //액션바 역할을 툴바로 하겠다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //툴바의 자체 타이틀을 없애고 텍스트뷰로 대체
        titleName = (TextView)findViewById(R.id.toolbar_title);


        /*
        ImageLoader 관련
         */

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.this)
                .threadPriority(Thread.NORM_PRIORITY - 2)//
                .denyCacheImageMultipleSizesInMemory()//
                .tasksProcessingOrder(QueueProcessingType.LIFO)//
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);//유니버셜 이미지 로더 초기화하기, Config설정은 기본적인 메모리캐시나 디스크캐시 입출력방식 다운로드방식 등에 대해서 설정

        DisplayImageOptions options = new DisplayImageOptions.Builder()//이미지에 대해서 설정할 수 있는 옵션
                .showImageOnLoading(R.drawable.ic_launcher_foreground)
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();


        /*
        이하 Navi Drawer 관련
         */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        callPermission();  // 권한 요청을 해야 함

        initMainFragment(); //최초 실행시 메인 프래그먼트가 실행되게 하는 메소드
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        Fragment fragment = null;
        String title="";

        if (id == R.id.nav_main) {
            //메인화면
            fragment = new MainFragment();
            title="스마트 옷장";

        } else if (id == R.id.nav_myWardrobe) {
            //내 옷장
            fragment= new MyWardrobeFragment();
            title="내 옷장";

        } else if (id == R.id.nav_myCodiset) {
            //내 코디셋
            fragment= new MyCodisetFragment();
            title="내 코디셋";
        } else if (id == R.id.nav_recommend) {
            //코디셋 추천
            fragment= new RecommendFragment();
            title="코디셋 추천";
        } else if (id == R.id.nav_calendar) {
            //월간 코디 현환
            fragment= new CalendarFragment();
            title="월간 코디 현황";
        } else if (id == R.id.nav_board) {
            //코디 추천 게시판
            fragment= new BoardFragment();
            title="코디 추천 게시판";
        } else if (id == R.id.nav_manage){
            //환경설정
        }
        else if(id == R.id.nav_login){//User.getInstance().id = null 이거나 null 아닐시 라는 if문 걸어서 set 바꾸기 그리고 로그아웃 누를때 로그인 페이지로 인텐트 시키기
            User.getInstance().id = null;
            User.getInstance().pw = null;
            User.getInstance().name = null;
            Intent go = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(go);
            finish();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        titleName.setText(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //처음 실행시 메인 프래그먼트를 실행시켜주기 위한 메소드
    private void initMainFragment()
    {
        Fragment fragment = new MainFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    public String  useGpsUrl(){
        // 권한 요청을 해야 함
        String error_log="-1";

        if (!isPermission) {
            callPermission();
            return error_log;
        }

        gps = new Gps(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            String url = "http://api.openweathermap.org/data/2.5/weather?lat="+ latitude + "&lon="+ longitude +
                    "&mode=json&units=metric&appid=02d0aec1e52029c889f9bf7709776514";

            return url;
        }
        else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
            return error_log;
        }
    }



}
