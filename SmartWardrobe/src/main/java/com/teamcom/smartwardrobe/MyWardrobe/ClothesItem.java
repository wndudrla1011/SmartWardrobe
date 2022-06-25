package com.teamcom.smartwardrobe.MyWardrobe;

/**
 * Created by ljrhi on 2018-05-02.
 */

public class ClothesItem {
    private String id;                  //DB내에 해당아이템 primary key (auto increment)
    private int type;                   //의류 종류에 대한 상세구분
    private int color;                  //의류 색 정보
    private int minTem;                 // 적정온도
    private int maxTem;
    private String imgSrc;              // 단말기내 저장된 파일명 (숫자는 1부터 늘어감)           -> ex) t0001.png , o0245.png, b0112.png
    private boolean isExist;            // 현재 옷장내에 해당옷이 존재하는 지에 대한 여부
    private int wearCount;


    public ClothesItem(String id, int type, int color, int minTem, int maxTex, String imgSrc, boolean isExist, int wearCount) {
        this.id = id;
        this.type = type;
        this.color = color;
        this.minTem = minTem;
        this.maxTem = maxTex;
        this.imgSrc = imgSrc;
        this.isExist = isExist;
        this.wearCount = wearCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public boolean getIsExist() {
        return isExist;
    }

    public void setIsExist(boolean as) {
        isExist = as;
    }

    public int getMinTem() {
        return minTem;
    }

    public void setMinTem(int minTem) {
        this.minTem = minTem;
    }

    public int getMaxTem() {
        return maxTem;
    }

    public void setMaxTem(int maxTem) {
        this.maxTem = maxTem;
    }

    public int getWearCount() {
        return wearCount;
    }

    public void setWearCount(int wearCount) {
        this.wearCount = wearCount;
    }

}
