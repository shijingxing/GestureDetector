package com.shijingxing.gesturelistviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/6/15.
 */
public class DeleteListView extends ListView {

    private GestureDetector gestureDetector;
    private int selectionPosition;
    private ViewGroup itemGroup;
    private  ImageView iv;
    private boolean isDeleteIvShow;
    private OnClickDeleteListener onClickDeleteListener;
    public interface OnClickDeleteListener{
        public void onClickDelete(int position);
    }
    public void setOnClickDeleteListeer(OnClickDeleteListener onClickDeleteListener){
        this.onClickDeleteListener=onClickDeleteListener;
    }

    public DeleteListView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){


            @Override
            public boolean onDown(MotionEvent e) {
                if(!isDeleteIvShow){
                    selectionPosition = pointToPosition(((int) e.getX()), ((int) e.getY()));
                }
                return super.onDown(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(!isDeleteIvShow&&e1.getX()>e2.getX()){
                    itemGroup= (RelativeLayout) getChildAt(selectionPosition-getFirstVisiblePosition());
                    iv=new ImageView(context);
                    RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rl.addRule(RelativeLayout.CENTER_VERTICAL);
                    rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    iv.setLayoutParams(rl);
                    iv.setImageResource(android.R.drawable.ic_delete);
                    iv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TranslateAnimation translateAnimation=new TranslateAnimation(0,-itemGroup.getWidth(),0,0);
                            translateAnimation.setDuration(500);
                            translateAnimation.setFillAfter(true);
                            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    TranslateAnimation upAnimation = new TranslateAnimation(0, 0, itemGroup.getHeight(), 0);
                                    upAnimation.setDuration(200);
                                    upAnimation.setFillAfter(true);
                                    for (int i = selectionPosition-getFirstVisiblePosition() ; i < getChildCount(); i++) {
                                        getChildAt(i).startAnimation(upAnimation);
                                    }
                                    if(onClickDeleteListener!=null){
                                        itemGroup.removeView(iv);
                                        onClickDeleteListener.onClickDelete(selectionPosition);
                                    }
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });


                            itemGroup.startAnimation(translateAnimation);
                            //itemGroup.removeView(iv);//刷新数据源之前删除已经添加的delete图标
                            //onClickDeleteListener.onClickDelete(selectionPosition);
                        }
                    });
                    itemGroup.addView(iv);
//                    TranslateAnimation ta=new TranslateAnimation(0,-iv.getWidth(),0,0);
//                    ta.setDuration(500);
//                    itemGroup.startAnimation(ta);
                    isDeleteIvShow=true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(isDeleteIvShow){
                        itemGroup.removeView(iv);
                        isDeleteIvShow=false;
                    }
                    return gestureDetector.onTouchEvent(event);
                }
            });
    }

}
