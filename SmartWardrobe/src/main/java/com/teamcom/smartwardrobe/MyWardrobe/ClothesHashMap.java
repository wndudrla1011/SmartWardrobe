package com.teamcom.smartwardrobe.MyWardrobe;


import com.teamcom.smartwardrobe.MainActivity;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ljrhi on 2018-05-13.
 */

public class ClothesHashMap{
    public static final int INT_TO_COLOR_VAL=1;
    public static final int INT_TO_COLOR_NAME=2;
    public static final int INT_TO_TYPE_NAME =3;

    private HashMap<Integer, String> intToColorValMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> intToColorNameMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> intToTypeNameMap = new HashMap<Integer, String>();

    public ClothesHashMap() {
        for(int i=1; i<=25;i++)
        {
            intToColorValMap.put(i,"color_"+i);
        }
        intToColorNameMap.put(1,"브라운");
        intToColorNameMap.put(2,"카키");
        intToColorNameMap.put(3,"다크블루");
        intToColorNameMap.put(4,"화이트");
        intToColorNameMap.put(5,"그레이");
        intToColorNameMap.put(6,"베이지");
        intToColorNameMap.put(7,"스카이");
        intToColorNameMap.put(8,"레드");
        intToColorNameMap.put(9,"그린");
        intToColorNameMap.put(10,"네이비");
        intToColorNameMap.put(11,"민트");
        intToColorNameMap.put(12,"옐로");
        intToColorNameMap.put(13,"블랙");
        intToColorNameMap.put(14,"카멜");
        intToColorNameMap.put(15,"핑크");
        intToColorNameMap.put(16,"블루");
        intToColorNameMap.put(17,"오렌지");
        intToColorNameMap.put(18,"퍼플");
        intToColorNameMap.put(19,"아이보리");
        intToColorNameMap.put(20,"챠콜");
        intToColorNameMap.put(21,"와인");
        intToColorNameMap.put(22,"연청");
        intToColorNameMap.put(23,"흑청");
        intToColorNameMap.put(24,"진청");
        intToColorNameMap.put(25,"중청");

        //외투
        intToTypeNameMap.put(1,"가디건");
        intToTypeNameMap.put(2,"롱 가디건");
        intToTypeNameMap.put(3,"조끼");
        intToTypeNameMap.put(4,"니트조끼");
        intToTypeNameMap.put(5,"남방");
        intToTypeNameMap.put(6,"스트라이프 남방");
        intToTypeNameMap.put(7,"바람막이");
        intToTypeNameMap.put(8,"후드집업");
        intToTypeNameMap.put(9,"트렌치 코트");
        intToTypeNameMap.put(10,"코트");
        intToTypeNameMap.put(11,"청자켓");
        intToTypeNameMap.put(12,"가죽자켓");
        intToTypeNameMap.put(13,"면자켓");
        intToTypeNameMap.put(14,"트랙자켓");
        intToTypeNameMap.put(15,"야구점퍼");
        intToTypeNameMap.put(16,"항공점퍼");
        intToTypeNameMap.put(17,"블루종");
        intToTypeNameMap.put(18,"블레이저");
        intToTypeNameMap.put(19,"무스탕");
        intToTypeNameMap.put(20,"패딩");
        intToTypeNameMap.put(21,"패딩조끼");
        intToTypeNameMap.put(22,"야상");
        intToTypeNameMap.put(23,"청남방");
        intToTypeNameMap.put(24,"체크남방");

        //상의
        intToTypeNameMap.put(51,"반팔티");
        intToTypeNameMap.put(52,"스트라이프 반팔티");
        intToTypeNameMap.put(53,"반팔 카라티");
        intToTypeNameMap.put(54,"칠부티");
        intToTypeNameMap.put(55,"스트라이프 칠부");
        intToTypeNameMap.put(56,"긴팔티");
        intToTypeNameMap.put(57,"스트라이프 긴팔티");
        intToTypeNameMap.put(58,"후드티");
        intToTypeNameMap.put(59,"폴라티");
        intToTypeNameMap.put(60,"맨투맨");
        intToTypeNameMap.put(61,"니트/스웨터");
        intToTypeNameMap.put(62,"폴라니트");
        intToTypeNameMap.put(63,"셔츠");
        intToTypeNameMap.put(64,"스트라이프 셔츠");
        intToTypeNameMap.put(65,"체크셔츠");
        intToTypeNameMap.put(66,"청셔츠");
        intToTypeNameMap.put(67,"반팔셔츠");
        intToTypeNameMap.put(68,"반팔 스트라이프 셔츠");
        intToTypeNameMap.put(69,"반팔 체크 셔츠");

        //하의
        intToTypeNameMap.put(101,"루즈핏 면바지");
        intToTypeNameMap.put(102,"기본핏 면바지");
        intToTypeNameMap.put(103,"면반바지");
        intToTypeNameMap.put(104,"루즈핏 청바지");
        intToTypeNameMap.put(105,"기본핏 청바지");
        intToTypeNameMap.put(106,"청반바지");
        intToTypeNameMap.put(107,"워싱 청바지");
        intToTypeNameMap.put(108,"워싱 청반바지");
        intToTypeNameMap.put(109,"루즈핏 슬랙스");
        intToTypeNameMap.put(110,"기본핏 슬랙스");
        intToTypeNameMap.put(111,"슬랙스 반바지");
        intToTypeNameMap.put(112,"기본핏 린넨바지");
        intToTypeNameMap.put(113,"루즈핏 린넨바지");
        intToTypeNameMap.put(114,"린넨 반바지");

    }

    public String getValue(int key, int opt)
    {
        String result = null;

        switch (opt)
        {
            case INT_TO_COLOR_VAL:
            {
                result =intToColorValMap.get(key);
                break;
            }
            case INT_TO_COLOR_NAME:
            {
                result= intToColorNameMap.get(key);
                break;
            }
            case INT_TO_TYPE_NAME:
            {
                result = intToTypeNameMap.get(key);
                break;
            }
            default:
            {
                result="-1";
            }
        }

        return result;
    }
    public Iterator<Integer> getHashAllData(int opt)
    {
        Iterator<Integer> result = null;

        switch (opt)
        {
            case INT_TO_COLOR_VAL:
            {
                result = intToColorValMap.keySet().iterator();
                break;
            }
            case INT_TO_COLOR_NAME:
            {
                result = intToColorNameMap.keySet().iterator();
                break;
            }
            default:
            {
                result=null;
            }
        }
        return result;
    }

    public int getHashSize(int opt){
        int result = -1;

        switch (opt)
        {
            case INT_TO_COLOR_VAL:
            {
                result = intToColorValMap.size();
                break;
            }
            case INT_TO_COLOR_NAME:
            {
                result = intToColorNameMap.size();
                break;
            }

            default:
            {
                result=-1;
            }
        }
        return result;
    }
    //색상 숫자값을 받아 해당하는 ResId로 반환
    public int getResId(int val)
    {
        String preId = getValue(val,INT_TO_COLOR_VAL);
        int result = -1;
        //1~22까지는 @color의 색상들
        if(val > 0 && val < 22)
        {
            result = MainActivity.MA_Context.getResources().getIdentifier(preId,"color","com.teamcom.smartwardrobe");
        }
        //23이상은 청바지색상이라 drawable의 색상들
        else if(val>=22)
        {
            result = MainActivity.MA_Context.getResources().getIdentifier(preId,"drawable","com.teamcom.smartwardrobe");
        }
        return result;
    }
}
