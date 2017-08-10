package com.example.ywx.viewlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ywx on 2017/8/10.
 *
 */

public class LoadingButton extends View implements View.OnClickListener{
    //按钮颜色
    private String buttonColor=null;
    //加载条颜色
    private String loadColor=null;
    //文本内容
    private String text=null;
    //文本大小
    private float textSize;
    //各个画笔
    private Paint mPaint,pathPaint,textPaint;
    //控件实际宽高
    private int mWidth,mHeight;
    //当前矩形左右边界
    private int currentLeft,currentRight;
    //控件默认宽高，指定为wrap_content就是默认宽高
    private int defaultWidth,defaultHeight;
    private ValueAnimator valueAnimator;
    //路径绘制所需要的变量
    private PathMeasure pathMeasure;
    private Path drawPath,dst;
    private float length,value;
    //是否收缩
    private boolean isFold=false;
    public LoadingButton(Context context) {
        this(context,null);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.LoadingButton);
        buttonColor=ta.getString(R.styleable.LoadingButton_button_background);
        loadColor=ta.getString(R.styleable.LoadingButton_load_color);
        text=ta.getString(R.styleable.LoadingButton_text);
        textSize=ta.getDimension(R.styleable.LoadingButton_textSize,45);
        ta.recycle();
        init(context,attrs,defStyleAttr);
        setOnClickListener(this);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //设置默认宽高
        defaultWidth=400;
        defaultHeight=120;

        //初始化画笔
        textPaint=new Paint();
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //如果设置了颜色就设置进去，否则默认灰色
        if(buttonColor!=null) {
            mPaint.setColor(Color.parseColor(buttonColor));
        }else {
            mPaint.setColor(Color.GRAY);
        }

        pathPaint=new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(6);
        if(loadColor!=null){
            pathPaint.setColor(Color.parseColor(loadColor));
        }else {
            pathPaint.setColor(Color.GRAY);
        }

        drawPath=new Path();
        dst=new Path();
        //反复执行，让加载条不停加载
        ValueAnimator animator=ValueAnimator.ofFloat(0,1);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode==MeasureSpec.AT_MOST){
            widthSize=defaultWidth;
        }
        if(heightMode==MeasureSpec.AT_MOST){
            heightSize=defaultHeight;
        }

        mWidth=widthSize;
        mHeight=heightSize;
        //默认矩形左右边界
        currentLeft=mHeight/2;
        currentRight=mWidth-mHeight/2;
        drawPath.addCircle(mWidth/2,mHeight/2,mHeight/4, Path.Direction.CW);
        pathMeasure=new PathMeasure(drawPath,true);
        length=pathMeasure.getLength();
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制矩形
         drawRect(canvas);

        //绘制半圆弧
         drawSemicircle(canvas);

        //绘制加载动画
        if(isFold) {
            drawLoad(canvas);
        }

        if(!isFold){
            drawText(canvas);
        }
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        if(!TextUtils.isEmpty(text)) {
            Paint.FontMetrics fm=textPaint.getFontMetrics();
            int textHeight= (int) fm.descent-(int)fm.ascent;
            Log.d("textHeight",textHeight+"");
            Log.d("mHeight",mHeight+"");
            float textWidth = textPaint.measureText(text);
            float left=mWidth/2-textWidth/2;
            int bottom=mHeight/2+textHeight/2-6;
            canvas.drawText(text,left,bottom,textPaint);
        }
    }

    /**
     * 绘制加载动画
     */
    private void drawLoad(Canvas canvas) {
        dst.reset();
        dst.lineTo(0,0);
        float end=length*value;
        float start=(end-(0.5f-Math.abs(value-0.5f))*length);
        pathMeasure.getSegment(start,end,dst,true);

        canvas.drawPath(dst,pathPaint);
    }

    /**
     * 绘制半圆
     * @param canvas
     */
    private void drawSemicircle(Canvas canvas) {
        //绘制左半圆
        RectF rect=new RectF();
        rect.top=0;
        rect.left=currentLeft-mHeight/2;
        rect.bottom=mHeight;
        rect.right=currentLeft+mHeight/2;
        canvas.drawArc(rect,-90,-180,true,mPaint);
        //绘制右半圆
        rect.top=0;
        rect.left=currentRight-mHeight/2;
        rect.bottom=mHeight;
        rect.right=currentRight+mHeight/2;
        canvas.drawArc(rect,-90,180,true,mPaint);
    }

    /**
     * 绘制矩形的方法
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        RectF rect=new RectF();
        rect.top=0;
        rect.left=currentLeft;
        rect.bottom=mHeight;
        rect.right=currentRight;
        canvas.drawRect(rect,mPaint);
    }

    @Override
    public void onClick(View view) {
        setClickable(false);
        setEnabled(false);
        if (!isFold) {
            isFold=true;
            valueAnimator = ValueAnimator.ofInt(mHeight / 2, mWidth / 2).setDuration(800);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    currentLeft = (int) valueAnimator.getAnimatedValue();
                    currentRight = mWidth - currentLeft;
                    invalidate();
                    if(currentLeft==mWidth/2){
                        setClickable(true);
                        setEnabled(true);
                    }
                }
            });
            valueAnimator.start();
        }
    }

    /**
     * 重置按钮
     */
    public void reset(){
            valueAnimator = ValueAnimator.ofInt(mWidth / 2, mHeight / 2).setDuration(800);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    currentLeft = (int) valueAnimator.getAnimatedValue();
                    currentRight = mWidth - currentLeft;
                    invalidate();
                    if(currentLeft==mHeight/2){
                        isFold=false;
                        setClickable(true);
                        setEnabled(true);
                    }
                }
            });
            valueAnimator.start();
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
        mPaint.setColor(Color.parseColor(buttonColor));
        invalidate();
    }

    public void setLoadColor(String loadColor) {
        this.loadColor = loadColor;
        pathPaint.setColor(Color.parseColor(loadColor));
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
        invalidate();
    }
}
