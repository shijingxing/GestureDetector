package com.shijingxing.gesturelistviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DeleteListView lv;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = ((DeleteListView) findViewById(R.id.lv_main));
        initData();
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list_view,R.id.list_tv,mList);
        lv.setAdapter(adapter);
        lv.setOnClickDeleteListeer(new DeleteListView.OnClickDeleteListener() {
            @Override
            public void onClickDelete(int position) {
                mList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void initData(){
        mList=new ArrayList<String>();
        for (int i = 0; i <30 ; i++) {
            mList.add("android-"+i);
        }
    }
}
