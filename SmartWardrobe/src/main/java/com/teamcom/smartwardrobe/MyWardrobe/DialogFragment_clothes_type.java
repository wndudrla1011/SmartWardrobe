package com.teamcom.smartwardrobe.MyWardrobe;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamcom.smartwardrobe.R;


public class DialogFragment_clothes_type extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    Context context;
    View view;  //layoutInflater로 inflate한 뷰를 담을 변수
    ArrayAdapter<CharSequence> oSpin1, oSpin2;
    //라디오버튼을 통한 외투 상의 하의종류 지정
    private int seletedKind=1;

    DialogFragment_clothes_type.OnDialogResult dialogResult;

    private int selecetedPosition = -1;

    public DialogFragment_clothes_type() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.dialog_clothes_type, container, false);

        //set Context
        this.context = view.getContext();

        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // remove dialog background
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RadioButton rOuter = (RadioButton) view.findViewById(R.id.radio_outer);

        rOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //외투 선택 분류 지정
                seletedKind=1;
                radio_outer();
            }
        });

        RadioButton rTop = (RadioButton) view.findViewById(R.id.radio_top);

        rTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //상의 선택 분류 지정
                seletedKind=2;
                radio_top();
            }
        });

        RadioButton rBottom = (RadioButton) view.findViewById(R.id.radio_bottom);

        rBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //하의 선택 분류 지정
                seletedKind=3;
                radio_bottom();
            }
        });

        TextView commit_type = (TextView)view.findViewById(R.id.commit_type);
        TextView cancel_type = (TextView)view.findViewById(R.id.cancel_type);

        commit_type.setOnClickListener(this);

        cancel_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return this.view;
    }

    private void radio_outer() {
        final Spinner spin1 = (Spinner) view.findViewById(R.id.type);
        final Spinner spin2 = (Spinner) view.findViewById(R.id.list);

        oSpin1 = ArrayAdapter.createFromResource(getContext(), R.array.outer, android.R.layout.simple_dropdown_item_1line);
        oSpin1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        spin1.setAdapter(oSpin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (oSpin1.getItem(i).equals("가디건")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.cardigan, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("가디건")){
                                selecetedPosition = 1;
                            }else if(selItem.equals("롱 가디건")){
                                selecetedPosition = 2;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("조끼")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.chokki, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("조끼")){
                                selecetedPosition = 3;
                            }else if(selItem.equals("니트조끼")){
                                selecetedPosition = 4;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("남방")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.nambang, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("남방")){
                                selecetedPosition = 5;
                            }else if(selItem.equals("스트라이프 남방")){
                                selecetedPosition = 6;
                            }else if(selItem.equals("청남방")){
                                selecetedPosition = 23;
                            }else if(selItem.equals("체크 남방")){
                                selecetedPosition = 24;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("바람막이")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.windbreaker, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("바람막이")){
                                selecetedPosition = 7;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("후드집업")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.hoods, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("후드집업")){
                                selecetedPosition = 8;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("코트")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.coat, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("트렌치 코트")){
                                selecetedPosition = 9;
                            }else if(selItem.equals("코트")){
                                selecetedPosition = 10;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("자켓")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.jacket, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("청자켓")){
                                selecetedPosition = 11;
                            }else if(selItem.equals("가죽자켓")){
                                selecetedPosition = 12;
                            }else if(selItem.equals("면자켓")){
                                selecetedPosition = 13;
                            }else if(selItem.equals("트랙자켓")){
                                selecetedPosition = 14;
                            }else if(selItem.equals("블레이저")){
                                selecetedPosition = 18;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("점퍼")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.jumper, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("야구점퍼")){
                                selecetedPosition = 15;
                            }else if(selItem.equals("항공점퍼")){
                                selecetedPosition = 16;
                            }else if(selItem.equals("블루종")){
                                selecetedPosition = 17;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("무스탕")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.mustang, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("무스탕")){
                                selecetedPosition = 19;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("패딩")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.padding, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("패딩")){
                                selecetedPosition = 20;
                            }else if(selItem.equals("패딩조끼")){
                                selecetedPosition = 21;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("야상")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.yasang, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("야상")){
                                selecetedPosition = 22;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void radio_top(){
        final Spinner spin1 = (Spinner) view.findViewById(R.id.type);
        final Spinner spin2 = (Spinner) view.findViewById(R.id.list);

        oSpin1 = ArrayAdapter.createFromResource(getContext(), R.array.top, android.R.layout.simple_dropdown_item_1line);
        oSpin1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spin1.setAdapter(oSpin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (oSpin1.getItem(i).equals("T-shirts")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.t_shirts, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("반팔티")){
                                selecetedPosition = 50;
                            }else if(selItem.equals("스트라이프 반팔티")){
                                selecetedPosition = 51;
                            }else if(selItem.equals("반팔 카라티")){
                                selecetedPosition = 52;
                            }else if(selItem.equals("칠부티")){
                                selecetedPosition = 53;
                            }else if(selItem.equals("스트라이프 칠부")){
                                selecetedPosition = 54;
                            }else if(selItem.equals("긴팔티")){
                                selecetedPosition = 55;
                            }else if(selItem.equals("스트라이프 긴팔티")){
                                selecetedPosition = 56;
                            }else if(selItem.equals("후드티")){
                                selecetedPosition = 57;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("맨투맨")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.mtom, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("맨투맨")){
                                selecetedPosition = 59;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("니트/스웨터")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.knit_sweater, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("니트/스웨터")){
                                selecetedPosition = 60;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("폴라")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.pola, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("폴라티")){
                                selecetedPosition = 58;
                            }else if(selItem.equals("폴라니트")){
                                selecetedPosition = 61;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("셔츠")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.shirts, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("셔츠")){
                                selecetedPosition = 62;
                            }else if(selItem.equals("스트라이프 셔츠")){
                                selecetedPosition = 63;
                            }else if(selItem.equals("체크셔츠")){
                                selecetedPosition = 64;
                            }else if(selItem.equals("청셔츠")){
                                selecetedPosition = 65;
                            }else if(selItem.equals("반팔셔츠")){
                                selecetedPosition = 66;
                            }else if(selItem.equals("반팔 스트라이프 셔츠")){
                                selecetedPosition = 67;
                            }else if(selItem.equals("반팔 체크 셔츠")){
                                selecetedPosition = 68;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void radio_bottom(){
        final Spinner spin1 = (Spinner) view.findViewById(R.id.type);
        final Spinner spin2 = (Spinner) view.findViewById(R.id.list);

        oSpin1 = ArrayAdapter.createFromResource(getContext(), R.array.bottom, android.R.layout.simple_dropdown_item_1line);
        oSpin1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spin1.setAdapter(oSpin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (oSpin1.getItem(i).equals("면바지")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.cotton, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("루즈핏 면바지")){
                                selecetedPosition = 100;
                            }else if(selItem.equals("기본핏 면바지")){
                                selecetedPosition = 101;
                            }else if(selItem.equals("면반바지")){
                                selecetedPosition = 102;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("청바지")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.jean, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("루즈핏 청바지")){
                                selecetedPosition = 103;
                            }else if(selItem.equals("기본핏 청바지")){
                                selecetedPosition = 104;
                            }else if(selItem.equals("청반바지")){
                                selecetedPosition = 105;
                            }else if(selItem.equals("워싱 청바지")){
                                selecetedPosition = 106;
                            }else if(selItem.equals("워싱 청반바지")){
                                selecetedPosition = 107;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("슬랙스")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.slacks, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("루즈핏 슬랙스")){
                                selecetedPosition = 108;
                            }else if(selItem.equals("기본핏 슬랙스")){
                                selecetedPosition = 109;
                            }else if(selItem.equals("슬랙스 반바지")){
                                selecetedPosition = 110;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else if (oSpin1.getItem(i).equals("린넨바지")) {
                    oSpin2 = ArrayAdapter.createFromResource(getContext(), R.array.linen, android.R.layout.simple_dropdown_item_1line);
                    oSpin2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spin2.setAdapter(oSpin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String selItem = (String)spin2.getItemAtPosition(i);
                            if(selItem.equals("기본핏 린넨바지")){
                                selecetedPosition = 111;
                            }else if(selItem.equals("루즈핏 린넨바지")){
                                selecetedPosition = 112;
                            }else if(selItem.equals("린넨 반바지")){
                                selecetedPosition = 113;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.commit_type:
            {
                if(dialogResult!=null)
                {
                    dialogResult.result(selecetedPosition);
                    dismiss();
                }
                break;
            }
        }
    }

    public void setDialogResult(DialogFragment_clothes_type.OnDialogResult dialogResult){
        this.dialogResult = dialogResult;
    }



    public interface OnDialogResult{
        void result(int result);
    }
}
