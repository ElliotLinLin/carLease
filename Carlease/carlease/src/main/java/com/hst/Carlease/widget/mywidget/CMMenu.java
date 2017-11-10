package com.hst.Carlease.widget.mywidget;


import com.hst.Carlease.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CMMenu extends LinearLayout implements View.OnClickListener {

    private View mRootView;
    private int checkedColor=0xff2A8AE0;
    private int unCheckedColor=0xff707171;
    private LinearLayout menu_query;
    private LinearLayout menu_add;
    private LinearLayout menu_count;
    private LinearLayout menu_more;

    private TextView menu_add_icon;
    private TextView menu_add_title;
    private TextView menu_count_icon;
    private TextView menu_count_title;
    
    private TextView menu_more_icon;
    private TextView menu_more_title;
    private TextView menu_query_icon;
    private TextView menu_query_title;

    private OnCheckedChanged mListener;//变化监听
    public CMMenu(Context context) {
        this(context,null);
    }

    public CMMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @SuppressLint("NewApi") public CMMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        mRootView = View.inflate(getContext(), R.layout.menu_layout,this);
        menu_query=(LinearLayout)findViewById(R.id.menu_query);
        menu_add=(LinearLayout)findViewById(R.id.menu_add);
        menu_count=(LinearLayout)findViewById(R.id.menu_count);
        menu_more=(LinearLayout)findViewById(R.id.menu_more);
        
        menu_add_icon=(TextView)findViewById(R.id.menu_add_icon);
        menu_add_title=(TextView)findViewById(R.id.menu_add_title);
        menu_count_title=(TextView)findViewById(R.id.menu_count_title);
        menu_more_icon=(TextView)findViewById(R.id.menu_more_icon);
        menu_count_icon=(TextView)findViewById(R.id.menu_count_icon);
        menu_more_title=(TextView)findViewById(R.id.menu_more_title);
        menu_query_icon=(TextView)findViewById(R.id.menu_query_icon);
        menu_query_title=(TextView)findViewById(R.id.menu_query_title);
        
      
        
        
        menu_query.setOnClickListener(this);
        menu_add.setOnClickListener(this);
        menu_count.setOnClickListener(this);
        menu_more.setOnClickListener(this);
        setChecked(MenuItem.QUERY);
    }
    public void setChecked(MenuItem item){
        switch (item){
            case ADD:
                setAddChecked(true);
                setQueryChecked(false);
                setCountChecked(false);
                setMoreChecked(false);
                break;
            case QUERY:
                setAddChecked(false);
                setQueryChecked(true);
                setCountChecked(false);
                setMoreChecked(false);
                break;
            case COUNT:
                setQueryChecked(false);
                setAddChecked(false);
                setCountChecked(true);
                setMoreChecked(false);
                break;
            case MORE:
                setQueryChecked(false);
                setAddChecked(false);
                setCountChecked(false);
                setMoreChecked(true);
                break;
        }
        if(mListener!=null){
            mListener.onChanged(item,true);
        }
    }
    private void setAddChecked(boolean checked){
        menu_add.setTag(checked);
        menu_add_icon.setBackgroundResource(checked?R.drawable.car_click:R.drawable.car);
        menu_add_title.setTextColor(checked?checkedColor:unCheckedColor);
    }
    private void setQueryChecked(boolean checked){
        menu_query.setTag(checked);
       
        menu_query_icon.setBackgroundResource(checked?R.drawable.index_click:R.drawable.index);
        menu_query_title.setTextColor(checked?checkedColor:unCheckedColor);
    }
    private void setCountChecked(boolean checked){
        menu_count.setTag(checked);
//        menu_count_icon.setBackgroundResource(checked?R.drawable.clock_click:R.drawable.clock);
        menu_count_title.setTextColor(checked?checkedColor:unCheckedColor);
    }
    private void setMoreChecked(boolean checked){
        menu_more.setTag(checked);
        menu_more_icon.setBackgroundResource(checked?R.drawable.user_click:R.drawable.user);
        menu_more_title.setTextColor(checked?checkedColor:unCheckedColor);
    }
    public void setOnCheckedChanged(OnCheckedChanged listener){
        this.mListener=listener;
    }
    @Override
    public void onClick(View v) {
        boolean state =false;
        if(v.getTag()!=null && v.getTag() instanceof Boolean){
            state=(Boolean)v.getTag();
        }
        if(state){
            return;
        }
        switch (v.getId()){
            case R.id.menu_query:
                setChecked(MenuItem.QUERY);
                break;
            case R.id.menu_add:
                setChecked(MenuItem.ADD);
                break;
            case R.id.menu_count:
                setChecked(MenuItem.COUNT);
                break;
            case R.id.menu_more:
                setChecked(MenuItem.MORE);
                break;
        }
    }
} 
