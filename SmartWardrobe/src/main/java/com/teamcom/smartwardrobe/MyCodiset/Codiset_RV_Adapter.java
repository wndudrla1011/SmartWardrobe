package com.teamcom.smartwardrobe.MyCodiset;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamcom.smartwardrobe.ImageSaver;
import com.teamcom.smartwardrobe.R;

import java.util.List;

/**
 * Created by ljrhi on 2018-05-10.
 */

public class Codiset_RV_Adapter extends RecyclerView.Adapter<Codiset_RV_Adapter.CardViewHolder>{
    Context context;
    List<CodisetItem> items;

    //생성자
    public Codiset_RV_Adapter(Context context,List<CodisetItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = new View(context);
        switch (viewType)
        {
            case 2: //코디셋이 2피스인 경우
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_codiset_2piece, null);
                break;

            }
            case 3: //코디셋이 3피스인 경우
            {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_codiset_3piece, null);
                break;
            }
        }
        return new CardViewHolder(v, viewType);
    }

    //바인드 되는 뷰들에 내용 할당
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CodisetItem item = items.get(position);
        int piece = item.getPieceOpt();

        holder.title_TV.setText((position+1)+"번 코디셋");

        switch (piece)
        {
            case 2: //코디셋이 2피스
            {

                Bitmap temp = new ImageSaver(context)
                        .setExternal(true)
                        .setFileName(getImgSrc(item, 1))
                        .setDirectoryName("SmartWardrobe/.clothes")
                        .load();
                holder.top_IV.setImageBitmap(temp);

                temp = new ImageSaver(context)
                        .setExternal(true)
                        .setFileName(getImgSrc(item, 2))
                        .setDirectoryName("SmartWardrobe/.clothes")
                        .load();
                holder.bottom_IV.setImageBitmap(temp);
                break;
            }
            case 3: //코디셋이 3피스
            {
                Bitmap temp = new ImageSaver(context)
                        .setExternal(true)
                        .setFileName(getImgSrc(item, 3))
                        .setDirectoryName("SmartWardrobe/.clothes")
                        .load();
                holder.outer_IV.setImageBitmap(temp);

                temp = new ImageSaver(context)
                        .setExternal(true)
                        .setFileName(getImgSrc(item, 2))
                        .setDirectoryName("SmartWardrobe/.clothes")
                        .load();
                holder.top_IV.setImageBitmap(temp);

                temp = new ImageSaver(context)
                        .setExternal(true)
                        .setFileName(getImgSrc(item, 3))
                        .setDirectoryName("SmartWardrobe/.clothes")
                        .load();
                holder.bottom_IV.setImageBitmap(temp);
                break;
            }
        }
    }

    @Override
    //해당 인덱스의 아이템의 피스 정보를 반환
    public int getItemViewType(int position) {
        return items.get(position).getPieceOpt();
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //뷰홀더
    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView outer_IV;
        ImageView top_IV;
        ImageView bottom_IV;
        TextView title_TV;

        public CardViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType)
            {
                case 2:
                {
                    top_IV= itemView.findViewById(R.id.codi_top_IV);
                    bottom_IV = itemView.findViewById(R.id.codi_bottom_IV);
                    title_TV=itemView.findViewById(R.id.item_codiset_title_tv);
                    break;
                }
                case 3:
                {
                    outer_IV = itemView.findViewById(R.id.codi_outer_IV);
                    top_IV= itemView.findViewById(R.id.codi_top_IV);
                    bottom_IV = itemView.findViewById(R.id.codi_bottom_IV);
                    title_TV=itemView.findViewById(R.id.item_codiset_title_tv);
                    break;
                }
            }
        }
    }

    //아이템의 아이디를 통해서 이미지 파일 명을 반환하는 메소드
    private String getImgSrc(CodisetItem item, int clothesType){
        String temp=null;

        switch (clothesType)
        {
            case 1:
            {
                if(item.getOuterId()<10)
                {
                    temp="o"+"000"+String.valueOf(item.getOuterId())+".png";
                }
                else if(item.getOuterId()<100)
                {
                    temp="o"+"00"+String.valueOf(item.getOuterId())+".png";
                }
                else if(item.getOuterId()<1000)
                {
                    temp="o"+"0"+String.valueOf(item.getOuterId())+".png";
                }
                else if(item.getOuterId()<10000)
                {
                    temp="o"+String.valueOf(item.getOuterId())+".png";
                }
                break;
            }
            case 2:
            {
                if(item.getTopId()<10)
                {
                    temp="t"+"000"+String.valueOf(item.getTopId())+".png";
                }
                else if(item.getTopId()<100)
                {
                    temp="t"+"00"+String.valueOf(item.getTopId())+".png";
                }
                else if(item.getTopId()<1000)
                {
                    temp="t"+"0"+String.valueOf(item.getTopId())+".png";
                }
                else if(item.getTopId()<10000)
                {
                    temp="t"+String.valueOf(item.getTopId())+".png";
                }
                break;
            }
            case 3:
            {
                if(item.getBottomId()<10)
                {
                    temp="b"+"000"+String.valueOf(item.getBottomId())+".png";
                }
                else if(item.getBottomId()<100)
                {
                    temp="b"+"00"+String.valueOf(item.getBottomId())+".png";
                }
                else if(item.getBottomId()<1000)
                {
                    temp="b"+"0"+String.valueOf(item.getBottomId())+".png";
                }
                else if(item.getBottomId()<10000)
                {
                    temp="b"+String.valueOf(item.getBottomId())+".png";
                }
                break;
            }
        }

       return temp;
    }

}
