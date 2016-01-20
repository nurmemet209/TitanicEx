package com.cn.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by nurmemet on 2016/1/15.
 */
public class TitanicTextViewEx extends TextView {

    private Scroller mScroller;
    private Drawable wave;
    private BitmapShader mShader;
    // shader matrix
    private Matrix shaderMatrix;
    private int width;
    private int height;
    boolean up=false;
    private boolean running =false;
    public TitanicTextViewEx(Context context) {
        super(context);
        init();
    }

    public TitanicTextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitanicTextViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mScroller=new Scroller(this.getContext(),new LinearInterpolator());
        shaderMatrix = new Matrix();


    }

    public void start(){
        running =true;
        invalidate();
    }
    @Override
    public void computeScroll() {
        //动画还未结束
        if(running){
            if(mScroller.computeScrollOffset()){
                shaderMatrix.setTranslate(mScroller.getCurrX(),mScroller.getCurrY() );
                invalidate();
            }
            else{
                if(height!=0){
                    if(up){
                        mScroller.startScroll(0,-height/2,10*width,height,10000);
                        up=false;
                    }
                    else{
                        mScroller.startScroll(0,height/2,10*width,-height,10000);
                        up=true;
                    }

                }else{
                    shaderMatrix.setTranslate(0,height/2 );
                }
                invalidate();

            }
        }

}

    private void createShalder(){
        if(wave==null){
            wave=getResources().getDrawable(R.drawable.wave);
        }
        width=wave.getIntrinsicWidth();
        height=this.getHeight();
        Bitmap b=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        wave.setBounds(0,0,width,height);
        Canvas c=new Canvas(b);
        c.drawColor(getCurrentTextColor());
        wave.draw(c);
        mShader = new BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        shaderMatrix.setTranslate(0,height/2 );

    }

    @Override
    public void draw(Canvas canvas) {
        if(running){
            if(mShader==null){
                createShalder();
            }
            mShader.setLocalMatrix(shaderMatrix);
            this.getPaint().setShader(mShader);
        }
        super.draw(canvas);
    }
}
