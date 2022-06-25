package com.teamcom.smartwardrobe.MyWardrobe;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamcom.smartwardrobe.R;

import java.util.ArrayList;


public class DialogFragment_clothes_color extends android.support.v4.app.DialogFragment implements View.OnClickListener{

    Context context;
    View view;  //layoutInflater로 inflate한 뷰를 담을 변수

    RecyclerView color_rv;
    Color_IV_Adapter color_adapter;

    private int selecetedPosition = -1;

    OnDialogResult dialogResult;


    public DialogFragment_clothes_color() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.dialog_clothes_color, container, false);

        //set Context
        this.context = view.getContext();

        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // remove dialog background
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        color_rv=(RecyclerView)view.findViewById(R.id.dialog_color_RV);
        // init LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 기본값이 VERTICAL

        // setLayoutManager
        color_rv.setHasFixedSize(true);
        color_rv.setLayoutManager(mLayoutManager);


        ArrayList<ItemColorData> items = new ArrayList<>();
        ItemColorData[] item = new ItemColorData[new ClothesHashMap().getHashSize(ClothesHashMap.INT_TO_COLOR_VAL)];

        for (int i = 0; i < new ClothesHashMap().getHashSize(ClothesHashMap.INT_TO_COLOR_VAL) ; i++) {
            item[i]= new ItemColorData(new ClothesHashMap().getValue(i+1,ClothesHashMap.INT_TO_COLOR_VAL)
                    ,new ClothesHashMap().getValue(i+1,ClothesHashMap.INT_TO_COLOR_NAME));
            items.add(item[i]);
        }

        // init Adapter
        color_adapter = new Color_IV_Adapter(items);
        color_rv.setAdapter(color_adapter);

        TextView commit_color = (TextView) view.findViewById(R.id.commit_color);
        TextView cancel_color = (TextView) view.findViewById(R.id.cancel_color);

        commit_color.setOnClickListener(this);

        cancel_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return this.view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.commit_color:
            {
                if(dialogResult!=null)
                {
                    dialogResult.result(selecetedPosition+1);
                    dismiss();
                }
                break;
            }
        }
    }
    public void setDialogResult(OnDialogResult dialogResult){
        this.dialogResult = dialogResult;
    }

    public interface OnDialogResult{
        void result(int result);
    }


    /*
    이하 컬러선택 Recyleview 관련 클래스들
 */


    class Color_IV_Adapter extends RecyclerView.Adapter<Color_IV_ViewHolder>{
        private ArrayList<ItemColorData> items;

        public Color_IV_Adapter(ArrayList<ItemColorData> items) {
            this.items = items;
        }

        @Override
        public Color_IV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_color_title, parent, false);

            Color_IV_ViewHolder holder = new Color_IV_ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(final Color_IV_ViewHolder holder, final int position) {
            final ItemColorData tempData = items.get(position);

            //기본 배경색 지정
            setBackground(holder, position, tempData);

            if(selecetedPosition==position)
            {
                //체크 표시 설정
                holder.image.setBackgroundResource(R.drawable.ic_all_check);
            }
            else
            {
                //기본 배경색 복귀
                setBackground(holder,position,tempData);
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selecetedPosition= position;
                    notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private void setBackground(final Color_IV_ViewHolder holder, final int position,ItemColorData tempData)
        {
            if(position<21)
            {
                holder.image.setBackgroundResource (getContext().getResources().getIdentifier(
                        String.valueOf(tempData.getColorVal())
                        ,"color"
                        ,"com.teamcom.smartwardrobe"));
                holder.description.setText(tempData.getColorName());
            }
            else
            {
                holder.image.setBackgroundResource (getContext().getResources().getIdentifier(
                        String.valueOf(tempData.getColorVal())
                        ,"drawable"
                        ,"com.teamcom.smartwardrobe"));
                holder.description.setText(tempData.getColorName());
            }
        }
    }



    class Color_IV_ViewHolder extends RecyclerView.ViewHolder{
        public ImageButton image;
        public TextView description;

        public Color_IV_ViewHolder(View itemView) {
            super(itemView);
            image = (ImageButton) itemView.findViewById(R.id.item_color_title_IV);
            description = (TextView)itemView.findViewById(R.id.item_color_title_TV);
        }
    }
    class ItemColorData{
        private String colorVal;
        private String colorName;

        public ItemColorData(String colorVal, String colorName) {
            this.colorVal = colorVal;
            this.colorName = colorName;
        }

        public String getColorVal() {
            return colorVal;
        }

        public void setColorVal(String colorVal) {
            this.colorVal = colorVal;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }
    }
}




