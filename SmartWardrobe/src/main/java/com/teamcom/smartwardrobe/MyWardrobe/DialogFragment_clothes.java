package com.teamcom.smartwardrobe.MyWardrobe;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.teamcom.smartwardrobe.ImageSaver;
import com.teamcom.smartwardrobe.Networks;
import com.teamcom.smartwardrobe.R;
import com.teamcom.smartwardrobe.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class DialogFragment_clothes extends android.support.v4.app.DialogFragment{

    @Override
    public void onStart() {
        super.onStart();

        //safty check
        if (getDialog() == null)
            return;

        //다이얼로그의 사이즈 지정
        final int dialogWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());
        final int dialogHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 480, getResources().getDisplayMetrics());

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    public static final int OPT_DETAIL = 1;
    public static final int OPT_MODIFY = 2;
    public static final int OPT_NEW = 3;

    //사진관련 상수들
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private static final int MULTIPLE_PERMISSIONS = 101;

    //사진관련 변수들
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private Uri photoUri;
    Bitmap temp_bitmap;
    ImageView img_IV; //다이얼로그에서 옷 사진을 담는 이미지뷰

    Context context;
    View view;  //layoutInflater로 inflate한 뷰를 담을 변수

    //번들로 넘어온 아이템 정보를 저장할 객체
    ClothesItem temp_item;

    HashMap<String,String> params;

    private int dialogOpt;


    DialogFragment_clothes_color colorDialogFragment; //컬러 다이얼로그
    DialogFragment_clothes_type typeDialogFragment; //종류 다이얼로그

    ImageView color_IV;

    public DialogFragment_clothes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        params = new HashMap<>();
        //
        this.dialogOpt = getArguments().getInt("opt");

        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // remove dialog background
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //필요한 권한 요청
        checkPermissions();

        //OPT_NEW가 아니면 전부 기존 정보를 불러와야 하므로
        if(this.dialogOpt!= OPT_NEW)
        {
            this.temp_item= new ClothesItem("temp",
                    getArguments().getInt("type"),
                    getArguments().getInt("color"),
                    getArguments().getInt("minTem"),
                    getArguments().getInt("maxTem"),
                    getArguments().getString("imgSrc"),
                    getArguments().getBoolean("exist"),
                    getArguments().getInt("count")
            );
        }

        switch (this.dialogOpt)
        {
            //번들로 전달된 다이얼로그 옵션이 의류 상세 정보 다이얼로그인 경우
            case OPT_DETAIL:
            {
                // Inflate the layout for this fragment
                this.view = inflater.inflate(R.layout.dialog_clothes_detail, container, false);

                //set Context
                this.context = view.getContext();

                /*
                    상단 버튼객체 할당
                 */
                Button backBtn = view.findViewById(R.id.dialog_clothes_back_btn);
                Button modBtn = view.findViewById(R.id.dialog_clothes_mod_btn);
                Button delBtn = view.findViewById(R.id.dialog_clothes_del_btn);

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

                modBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //mod Dialog Fragment로 이동
                        FragmentManager manager = getFragmentManager();
                        DialogFragment_clothes modDialogFragment = new DialogFragment_clothes();

                        Bundle args= new Bundle();
                        //옵션을 수정 다이얼로그로 지정
                        args.putInt("opt",OPT_MODIFY);

                        //이하 선택된 item의 정보를 번들로 넘김
                        args.putInt("type",temp_item.getType());
                        args.putInt("color",temp_item.getColor());
                        args.putBoolean("exist",temp_item.getIsExist());
                        args.putInt("count",temp_item.getWearCount());
                        args.putInt("minTem",temp_item.getMinTem());
                        args.putInt("MaxTem",temp_item.getMaxTem());
                        args.putString("imgSrc",temp_item.getImgSrc());

                        modDialogFragment.setArguments(args);
                        modDialogFragment.show(manager,null);

                        dismiss(); //상세정보 프래그먼트는 소멸
                    }
                });
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteIntoServer();
                        dismiss();
                    }
                });

                /*
                    하단 컨텐츠 부분 뷰 객체 할당 및 이벤트 리스너 할당
                 */
                ImageView img_IV =(ImageView)view.findViewById(R.id.item_clothes_img_iv);
                TextView type_TV = (TextView)view.findViewById(R.id.item_clothes_type_tv);
                color_IV = (ImageView) view.findViewById(R.id.item_clothes_color_iv);
                TextView color_TV=(TextView) view.findViewById(R.id.item_clothes_color_tv);
                ImageView exist_IV = (ImageView) view.findViewById(R.id.item_clothes_isExist_iv);
                TextView count_TV = (TextView)view.findViewById(R.id.item_clothes_count_tv);
                TextView tem_season_min_TV=(TextView)view.findViewById(R.id.item_clothes_tem_season_min);
                TextView tem_season_max_TV=(TextView)view.findViewById(R.id.item_clothes_tem_season_max);
                TextView tem_min_TV =(TextView)view.findViewById(R.id.item_clothes_tem_min);
                TextView tem_max_TV =(TextView)view.findViewById(R.id.item_clothes_tem_max);


                count_TV.setText(Integer.toString(this.temp_item.getWearCount()));
                if(this.temp_item.getIsExist())
                {
                    exist_IV.setBackgroundResource(R.drawable.ic_all_circle);
                } else
                {
                    exist_IV.setBackgroundResource(R.drawable.ic_all_circle_x);
                }
                Bitmap tempBitmap = new ImageSaver(context).setExternal(true).setFileName(this.temp_item.getImgSrc()).setDirectoryName("SmartWardrobe/.clothes").load();
                img_IV.setImageBitmap(tempBitmap);

                type_TV.setText(new ClothesHashMap().getValue(this.temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));

                color_IV.setImageResource(new ClothesHashMap().getResId(this.temp_item.getColor()));
                color_TV.setText(new ClothesHashMap().getValue(this.temp_item.getColor(),ClothesHashMap.INT_TO_COLOR_NAME));

                //온도 뷰관련 설정
                tem_min_TV.setText(String.valueOf(temp_item.getMinTem()));
                tem_max_TV.setText(String.valueOf(temp_item.getMaxTem()));
                tem_season_min_TV.setText(temToSeason(temp_item.getMinTem()));
                tem_season_max_TV.setText(temToSeason(temp_item.getMaxTem()));

                break;
            }

            //번들로 전달된 다이얼로그 옵션이 의류 정보 수정 다이얼로그인 경우
            case OPT_MODIFY:
            {
                // Inflate the layout for this fragment
                this.view = inflater.inflate(R.layout.dialog_clothes_new_mod, container, false);

                //set Context
                this.context = view.getContext();

                /*
                    상단 버튼 객체 할당
                 */
                Button okBtn = view.findViewById(R.id.dialog_clothes_ok_btn);
                Button cancelBtn =view.findViewById(R.id.dialog_clothes_cancel_btn);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("변경 확인")
                                .setMessage("변경사항을 확정하시겠습니까?")
                                .setIcon(R.drawable.ic_all_check_round)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveIntoSever(OPT_MODIFY);
                                        //완성
                                        dismiss();
                                    }
                                })
                                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //empty method
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("취소 확인")
                                .setMessage("수정을 취소하시겠습니까?")
                                .setIcon(R.drawable.ic_all_cancel)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context,"수정이 취소되었습니다.",Toast.LENGTH_SHORT).show();
                                        dismiss();
                                        //완성
                                    }
                                })
                                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //empty method
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                /*
                    하단 컨텐츠 부분 뷰 객체 할당 및 이벤트 리스너 할당
                 */
                img_IV =(ImageView)view.findViewById(R.id.item_clothes_img_iv);
                final TextView type_TV = (TextView)view.findViewById(R.id.item_clothes_type_tv);
                RangeBar tem_RB=(RangeBar)view.findViewById(R.id.item_clothes_tem_RB);
                color_IV = (ImageView) view.findViewById(R.id.item_clothes_color_iv);
                final TextView color_TV=(TextView) view.findViewById(R.id.item_clothes_color_tv);

                Bitmap tempBitmap = new ImageSaver(context).setExternal(true).setFileName(this.temp_item.getImgSrc()).setDirectoryName("SmartWardrobe/.clothes").load();
                img_IV.setImageBitmap(tempBitmap);
                img_IV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //사진선택이나 사진촬영 다이얼로그를 뛰우는 메소드
                        showSelectImgDialog(context);
                    }
                });

                type_TV.setText(new ClothesHashMap().getValue(this.temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                type_TV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //종류 선택 다이얼로그 호출
                        FragmentManager manager = getFragmentManager();
                        typeDialogFragment = new DialogFragment_clothes_type();

                        typeDialogFragment.show(manager,null);

                        typeDialogFragment.setDialogResult(new DialogFragment_clothes_type.OnDialogResult() {
                            @Override
                            public void result(int result) {
                                temp_item.setType(result);
                                if(result >= 0 && result <= 50){
                                    type_TV.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }else if(result >= 51 && result <= 100){
                                    type_TV.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }else if(result >= 101 && result <= 150){
                                    type_TV.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }
                                //기존의 저장된 아이템들 정보를 읽어와 다음 파일명을 계산
                                temp_item.setImgSrc(makeImgSrc());
                            }
                        });
                    }
                });

                //기존 저장 값을 불러옴
                color_IV.setBackgroundResource (new ClothesHashMap().getResId(this.temp_item.getColor()));
                color_TV.setText(new ClothesHashMap().getValue(temp_item.getColor(),ClothesHashMap.INT_TO_COLOR_NAME));

                color_IV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //색 선택 다이얼로그 호출
                        FragmentManager manager = getFragmentManager();
                        colorDialogFragment = new DialogFragment_clothes_color();
                        colorDialogFragment.show(manager,null);

                        colorDialogFragment.setDialogResult(new DialogFragment_clothes_color.OnDialogResult(){
                            @Override
                            public void result(int result) {
                                temp_item.setColor(result);
                                color_IV.setBackgroundResource(new ClothesHashMap().getResId(temp_item.getColor()));
                                color_TV.setText(new ClothesHashMap().getValue(temp_item.getColor(),ClothesHashMap.INT_TO_COLOR_NAME));
                            }
                        });

                    }
                });


                tem_RB.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                        //온도 설정바 관련 리스너 설정된값을 temp_item객체에 저장
                        temp_item.setMinTem(Integer.parseInt(leftPinValue));
                        temp_item.setMaxTem(Integer.parseInt(rightPinValue));
                    }
                });

                break;
            }

            case OPT_NEW:
            {
                // Inflate the layout for this fragment
                this.view = inflater.inflate(R.layout.dialog_clothes_new_mod, container, false);

                //set Context
                this.context = view.getContext();

                //새로 추가하는 객체들의 정보를 저장하기 위한 임시 변수
                this.temp_item = new ClothesItem(null,-1,-1,-1,-1,null,true,0);

                //상단 버튼 객체 할당
                Button okBtn = (Button) view.findViewById(R.id.dialog_clothes_ok_btn);
                Button cancelBtn =(Button) view.findViewById(R.id.dialog_clothes_cancel_btn);
                final TextView color_TV=(TextView) view.findViewById(R.id.item_clothes_color_tv);
                color_IV = (ImageView)view.findViewById(R.id.item_clothes_color_iv);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("추가 확인")
                                .setMessage("추가사항을 확정하시겠습니까?")
                                .setIcon(R.drawable.ic_all_check_round)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(temp_item.getType()!=-1 && temp_item.getMinTem()!=-1 && temp_item.getMaxTem()!=-1  && temp_item.getColor()!=-1 && temp_item.getImgSrc()!=null)
                                        {
                                            //크롭한 이미지를 단말기내에 저장
                                            new ImageSaver(context).setExternal(true).setFileName(temp_item.getImgSrc()).setDirectoryName("SmartWardrobe/.clothes").save(temp_bitmap);
                                            //서버에 데이터 저장
                                            saveIntoSever(OPT_NEW);
                                            dismiss();
                                        }
                                    }
                                })
                                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("취소 확인")
                                .setMessage("추가를 취소하시겠습니까?")
                                .setIcon(R.drawable.ic_all_cancel)
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context,"취소되었습니다.",Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                })
                                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                /*
                    이하 뷰 객체 할당 및 이벤트 리스터 할당
                 */
                img_IV =(ImageView)view.findViewById(R.id.item_clothes_img_iv);
                img_IV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //사진선택이나 사진촬영 다이얼로그를 뛰우는 메소드
                        showSelectImgDialog(context);
                    }

                });

                final TextView type_Btn= (TextView)view.findViewById(R.id.item_clothes_type_tv);
                type_Btn.setText(new ClothesHashMap().getValue(this.temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                type_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //다이얼로그 뛰우는 코드
                        FragmentManager manager = getFragmentManager();
                        typeDialogFragment = new DialogFragment_clothes_type();

                        typeDialogFragment.show(manager,null);
                        typeDialogFragment.setDialogResult(new DialogFragment_clothes_type.OnDialogResult() {
                            @Override
                            public void result(int result) {
                                Log.e("확인용",String.valueOf(result));
                                temp_item.setType(result);
                                if(result >= 0 && result <= 50){
                                    type_Btn.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }else if(result >= 51 && result <= 100){
                                    type_Btn.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }else if(result >= 100 && result <= 150){
                                    type_Btn.setText(new ClothesHashMap().getValue(temp_item.getType(),ClothesHashMap.INT_TO_TYPE_NAME));
                                }

                                //기존의 저장된 아이템들 정보를 읽어와 다음 파일명을 계산
                                temp_item.setImgSrc(makeImgSrc());
                            }
                        });

                    }
                });

                color_IV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //다이얼로그 뛰우는 코드
                        FragmentManager manager = getFragmentManager();
                        colorDialogFragment = new DialogFragment_clothes_color();

                        colorDialogFragment.show(manager,null);
                        colorDialogFragment.setDialogResult(new DialogFragment_clothes_color.OnDialogResult(){
                            @Override
                            public void result(int result) {
                                temp_item.setColor(result);
                                color_IV.setBackgroundResource(new ClothesHashMap().getResId(temp_item.getColor()));
                                color_TV.setText(new ClothesHashMap().getValue(temp_item.getColor(),ClothesHashMap.INT_TO_COLOR_NAME));
                            }
                        });
                    }
                });

                RangeBar tem_RB=(RangeBar)view.findViewById(R.id.item_clothes_tem_RB);

                tem_RB.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                        //온도 설정바 관련 리스너 설정된값을 temp_item객체에 저장
                        temp_item.setMinTem(Integer.parseInt(leftPinValue));
                        temp_item.setMaxTem(Integer.parseInt(rightPinValue));
                    }
                });
                break;
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //사진 추가 화면에서 사진을 추가한후 이미지뷰에 반영하기 위한 부분
        if(temp_bitmap!=null)
        {
            img_IV.setImageBitmap(this.temp_bitmap);
        }

    }


    //단말에 저장된 아이템수를 통해서 다음 이미지파일의 이름을 세움
    private String makeImgSrc(){

        int existItemsCount=-1;     //기존 저장된 아이템들의 수  값은 0~9998까지

        String result=null;

        int itemsLen=-1;   //숫자의 자리수를 계산

        //종류별로 케이스를 나눔
        if(temp_item.getType()<=50 && temp_item.getType()>=1)
        {
            existItemsCount= PagerOuterFragment.getItemsSize();
            result = String.valueOf(existItemsCount+1);
            itemsLen=getIntLen(existItemsCount);
            Log.e("확인","itemsCount:"+String.valueOf(existItemsCount+1));

            switch (itemsLen)
            {
                case 1:
                {
                    result ="o000"+result+".png";
                    break;
                }
                case 2:
                {
                    result ="o00"+result+".png";
                    break;
                }
                case 3:
                {
                    result="o0"+result+".png";
                    break;
                }
                case 4:
                {
                    result="o"+result+".png";
                }
            }
        }
        else if(temp_item.getType()<=100)
        {
            existItemsCount= PagerTopFragment.getItemsSize();
            String.valueOf(existItemsCount+1);
            itemsLen=getIntLen(existItemsCount);

            switch (itemsLen)
            {
                case 1:
                {
                    result="t000"+result+".png";
                    break;
                }
                case 2:
                {
                    result="t00"+result+".png";
                    break;
                }
                case 3:
                {
                    result="t0"+result+".png";
                    break;
                }
                case 4:
                {
                    result="t"+result+".png";
                }
            }
        }
        else if(temp_item.getType()<=150)
        {
            existItemsCount= PagerBottomFragment.getItemsSize();
            String.valueOf(existItemsCount+1);
            itemsLen=getIntLen(existItemsCount);

            switch (itemsLen)
            {
                case 1:
                {
                    result="b000"+result+".png";
                    break;
                }
                case 2:
                {
                    result="b00"+result+".png";
                    break;
                }
                case 3:
                {
                    result="b0"+result+".png";
                    break;
                }
                case 4:
                {
                    result="b"+result+".png";
                }
            }
        }

        return result;
    }
    private int getIntLen(int num){
        int result=1;

        while((num/10)!=0){
            result++;
            num=num/10;
        }
        return result;
    }

    //온도 값을 받아서 계절 값으로 반환
    private String temToSeason(int tem)
    {
        String result=null;

        if(tem<=1)
        {
            result="한겨울";
        }
        else if(tem<=8)
        {
            result="초겨울";
        }
        else  if (tem<=15)
        {
            result="가을";
        }
        else  if(tem<=22)
        {
            result="초봄";
        }
        else  if(tem<=29)
        {
            result="초여름";
        }
        else if(tem<=35)
        {
            result="한여름";
        }
        return result;
    }

    /*
    이하는 모두 카메라 및 갤러리에서 이미지 선택 관련 메소드 ->OPT_NEW,OPT_MODIFY 사용
     */
    //사진선택이나 사진촬영 다이얼로그를 뛰우는 메소드
    private void showSelectImgDialog(Context context)
    {
        /*
            이미지 관련 작업
        */
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {/* 카메라 촬영*/
                doTakePhotoAction();

            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {/*앨범에서 사진 선택*/
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {/*취소*/
                dialog.dismiss();
            }
        };

        // 카메라 촬영이나 앨범을 열기 위한 다이얼로그
        new android.app.AlertDialog.Builder(context)
                .setTitle("사진을 가져옵니다.")
                .setPositiveButton("사진촬영",cameraListener)
                .setNeutralButton("취소",cancelListener)
                .setNegativeButton("앨범선택",albumListener)
                .show();

    }

    //카메라 촬영을 할 수 있게 해주는 함수 아래 createImageFile()과 함께 사용
    private void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(context,
                    "com.teamcom.smartwardrobe.provider", photoFile); //FileProvider의 경우 이전 포스트를 참고하세요.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/SmartWardrobe/.temp/"); //해당 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );
        return image;
    }

    //앨범에서 이미지를 가져오는 함수
    private void  doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(context, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        switch (requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                if(data==null){
                    return;
                }
                photoUri = data.getData();
                cropImage();
                break;
            }
            case PICK_FROM_CAMERA:
            {
                cropImage();
                MediaScannerConnection.scanFile(getActivity(), //앨범에 사진을 보여주기 위해 Scan을 합니다.
                        new String[]{photoUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                break;
            }
            case CROP_FROM_CAMERA:
            {
                try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 400, 400);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    thumbImage.compress(Bitmap.CompressFormat.PNG, 30, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                    //결과 비트맵 객체를 클래스의 임시 객체에 할당
                    this.temp_bitmap = thumbImage;

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
                break;
            }
        }
    }

    public void cropImage() {
        getActivity().grantUriPermission("com.android.camera", photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        getActivity().grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(context, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/Smartwardrobe/.temp/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(getActivity(),
                    "com.teamcom.smartwardrobe.provider", tempFile);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getActivity().grantUriPermission(res.activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);

        }

    }

    /*
    여기까지 모두 카메라 및 갤러리에서 이미지 선택 관련 메소드
     */

    //권한요청을 체크하는 함수
    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }
    //권한 요청 거부시 메세지
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(getContext(), "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        dismiss();
    }


    /*
    이하 서버 관련 메소드
     */
    private void saveIntoSever(int opt)
    {
        params.put("user", User.getInstance().id);
        params.put("id",String.valueOf(temp_item.getId()));
        params.put("type",String.valueOf(temp_item.getType()));
        params.put("color",String.valueOf(temp_item.getColor()));
        params.put("maxTem",String.valueOf(temp_item.getMaxTem()));
        params.put("minTem",String.valueOf(temp_item.getMinTem()));
        Log.e("전송값 확인",temp_item.getImgSrc());
        params.put("imgSrc",String.valueOf(temp_item.getImgSrc()));
        params.put("isAs",String.valueOf(temp_item.getIsExist()));
        params.put("wearcount",String.valueOf(temp_item.getWearCount()));

        if(opt==OPT_NEW)
            new LoadTask("http://wndudrla1011.dothome.co.kr/AddCloths.php",params).execute();
        else if(opt==OPT_MODIFY)
            new LoadTask("http://wndudrla1011.dothome.co.kr/ModifyCloths.php",params).execute();
        else
        {
            Toast.makeText(context,"오류발생입니다.",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(context,"변경사항이 반영되었습니다.",Toast.LENGTH_SHORT).show();
    }

    private void deleteIntoServer()
    {
        params.put("user",User.getInstance().id);
        params.put("id",String.valueOf(temp_item.getId()));
        new LoadTask("http://wndudrla1011.dothome.co.kr/DeleteCloths.php",params).execute();
        Toast.makeText(context,"의류정보가 삭제 되었습니다.",Toast.LENGTH_SHORT).show();
    }

    class LoadTask extends AsyncTask<Void,Void,String> {
        String url;
        HashMap<String, String> params;
        public LoadTask(String url, HashMap<String, String> params) {
            this.url = url;
            this.params = params;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = Networks.httpConnect(url, params);
//            if(choice == 1) {
//                UploadFile.uploadFile(filePath, "http:/wndudrla1011.dothome.co.kr/Picture.php?id=" + User.getInstance().id+"?c_id="+temp_item.getId());// 여기php 안에서 DB에 사진 경로 insert 및 update
//            }
//            else if(choice == 2){
//                UploadFile.uploadFile(filePath, "http:/wndudrla1011.dothome.co.kr/modi_Picture.php?id=" + User.getInstance().id+"?c_id="+temp_item.getId());
//            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (json.getBoolean("result")) {
                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
