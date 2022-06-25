package com.teamcom.smartwardrobe.MyWardrobe;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teamcom.smartwardrobe.ImageSaver;
import com.teamcom.smartwardrobe.R;

import java.util.List;


//내옷장의 카드뷰를 위한 어뎁터 클래스
public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.CardViewHolder>{
    Context context;
    List<ClothesItem> items;

    //생성자
    public ClothesAdapter(Context context, List<ClothesItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clothes, null);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        final ClothesItem item = items.get(position);
        Bitmap tempBitmap= null;

        if(item != null)
        {
             tempBitmap = new ImageSaver(context).setExternal(true).setFileName(item.getImgSrc()).setDirectoryName("SmartWardrobe/.clothes").load();
        }


        holder.image.setImageBitmap(tempBitmap);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택된 아이템의 세부정보를 보여주는 다이얼로그 생성
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                DialogFragment_clothes dialogFragment = new DialogFragment_clothes();
                Bundle args= new Bundle();
                //다이얼로그의 옵션을 Detail로 설정
                args.putInt("opt", DialogFragment_clothes.OPT_DETAIL);
                //이하 선택된 item의 정보를 번들로 넘김
                args.putInt("type",item.getType());
                args.putInt("color",item.getColor());
                args.putBoolean("exist",item.getIsExist());
                args.putInt("count",item.getWearCount());
                args.putInt("tem",item.getMinTem());
                args.putInt("maxTem",item.getMaxTem());
                args.putString("imgSrc",item.getImgSrc());

                dialogFragment.setArguments(args);
                dialogFragment.show(manager,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView cardview;

        public CardViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.clothesIV);
            cardview = (CardView) itemView.findViewById(R.id.clothesCV);
        }
    }



}


