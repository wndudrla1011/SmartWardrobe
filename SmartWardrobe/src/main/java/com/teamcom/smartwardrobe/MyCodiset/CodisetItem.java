package com.teamcom.smartwardrobe.MyCodiset;

/**
 * Created by ljrhi on 2018-05-10.
 */

public class CodisetItem {
    private int codisetId;
    private int outerId;
    private int topId;
    private int bottomId;
    private int wearCount;
    private int pieceOpt;  //2피스 3피스로만 구성


    public CodisetItem(int codisetId, int outerId, int topId, int bottomId, int wearCount) {
        this.codisetId = codisetId;

        this.outerId = outerId;
        this.topId = topId;
        this.bottomId = bottomId;

        this.wearCount = wearCount;

        this.pieceOpt = 3;
    }

    public CodisetItem(int codisetId, int topId, int bottomId, int wearCount) {
        this.codisetId = codisetId;
        this.topId = topId;
        this.bottomId = bottomId;
        this.wearCount = wearCount;

        this.pieceOpt = 2;
    }


    public int getCodisetId() {
        return codisetId;
    }

    public void setCodisetId(int codisetId) {
        this.codisetId = codisetId;
    }

    public int getOuterId() {
        return outerId;
    }

    public void setOuterId(int outerId) {
        this.outerId = outerId;
    }

    public int getTopId() {
        return topId;
    }

    public void setTopId(int topId) {
        this.topId = topId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public int getWearCount() {
        return wearCount;
    }

    public void setWearCount(int wearCount) {
        this.wearCount = wearCount;
    }

    public int getPieceOpt() {
        return this.pieceOpt;
    }

    public void setPieceOpt(int pieceOpt) {
        if(pieceOpt ==2| pieceOpt ==3)
        {
            this.pieceOpt = pieceOpt;
        }
        else
        {
            return;
        }
    }
}
