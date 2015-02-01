package com.boco.jersuendome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangWei on 2015/1/31.
 */
public class LockPatternView extends View {

    private static final int POINT_SIZE = 5;

    private Matrix matrix = new Matrix();

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Point[][] points = new Point[3][3];
    private boolean isInit,isSelect,isFinish,movingNoPoint;

    private float wight, height,offsetsX,offsetsY,movingX,movingY;
    private Bitmap pointNormal,pointPressed,pointError,lineError,linePressed;
    private int bitmapR;

    private List<Point> pointList = new ArrayList<Point>();

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit){
            initPoints();
        }
        points2Canvas(canvas);
    }

    /**
     * 绘制点到画布上
     * @param canvas 画布
     */
    private void points2Canvas(Canvas canvas) {
        for(int i=0;i<points.length;i++){
            for(int j = 0;j< points[i].length;j++){
                Point  point = points[i][j];
                if(point.state == Point.STATE_NORMAL){
                    canvas.drawBitmap(pointNormal,point.x - bitmapR,point.y + bitmapR,paint);
                }else if(point.state == Point.STATE_PRESSED){
                    canvas.drawBitmap(pointPressed,point.x - bitmapR,point.y + bitmapR,paint);
                }else{
                    canvas.drawBitmap(pointError,point.x - bitmapR,point.y + bitmapR,paint);
                }
            }
        }
    }

    /**
     * 划线
     * @param canvas 画布
     * @param a 第一个点
     * @param b 第二个点
     */
    private void line2Canvas(Canvas canvas,Point a,Point b){

        double lineLenght = Point.distance(a,b);
        if(a.state == Point.STATE_PRESSED){
            matrix.setScale((float) lineLenght/pointPressed.getWidth(),1);
            matrix.postTranslate(a.x,a.y);
            canvas.drawBitmap(linePressed,matrix,paint);
        }else{

        }
    }
    /**
     * 初始化点
     */
    private void initPoints() {
        wight = getWidth();
        height = getHeight();


        if(wight>height){
        //横屏
            offsetsX = (wight-height)/2;
            wight = height;
        }else{
        //竖屏
            offsetsY = (height/wight)/2;

            height = wight;
        }

        pointPressed = BitmapFactory.decodeResource(getResources(),R.drawable.locus_round_click);
        pointNormal = BitmapFactory.decodeResource(getResources(),R.drawable.locus_round_original);
        pointError = BitmapFactory.decodeResource(getResources(),R.drawable.locus_round_click_error);

        linePressed = BitmapFactory.decodeResource(getResources(),R.drawable.locus_line);
        lineError = BitmapFactory.decodeResource(getResources(),R.drawable.locus_line_error);


        points[0][0] = new Point(offsetsX + wight /4,offsetsY + wight /4);
        points[0][1] = new Point(offsetsX + wight /2,offsetsY + wight /4);
        points[0][2] = new Point(offsetsX + wight - wight / 4,offsetsY + wight /4);


        points[1][0] = new Point(offsetsX + wight /4,offsetsY + wight /2);
        points[1][1] = new Point(offsetsX + wight /2,offsetsY + wight /2);
        points[1][2] = new Point(offsetsX + wight - wight / 4,offsetsY + wight /2);


        points[2][0] = new Point(offsetsX + wight /4,offsetsY + wight - wight / 4);
        points[2][1] = new Point(offsetsX + wight /2,offsetsY + wight - wight / 4);
        points[2][2] = new Point(offsetsX + wight - wight / 4,offsetsY + wight - wight / 4);

        bitmapR = pointNormal.getWidth()/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        movingNoPoint = false;
        movingX = event.getX();
        movingY = event.getY();
        Point point = null;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                resetPoint();
                point = checkSelectedPoint();
                if(point !=null){
                    isSelect = true;
                }
                break;
            case  MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;
                break;
            case  MotionEvent.ACTION_MOVE:

                if(isSelect){
                    point = checkSelectedPoint();
                }

                if(point == null){
                    movingNoPoint = true;
                }
                break;
        }

        if(!isFinish && isSelect && point != null){

            if(pointList.contains(point)){
                movingNoPoint = true;
            }else{
                point.state = Point.STATE_PRESSED;
                pointList.add(point);
            }
        }

        if(isFinish){

            if(pointList.size()<2){
                resetPoint();
            }else if(pointList.size() < POINT_SIZE && pointList.size() >2){
                errorPoint();
            }
        }

        postInvalidate();
        return true;
    }


    public void resetPoint(){
        pointList.clear();
    }

    public void errorPoint(){
        for(Point point:pointList) {
            point.state = Point.STATE_ERROR;
        }
    }
    private Point checkSelectedPoint(){
        for(int i=0;i<points.length;i++){
            for(int j = 0;j< points[i].length;j++){
                Point  point = points[i][j];

                if(Point.with(point.x,point.y,bitmapR,movingX,movingY)){
                    return point;
                }
            }
        }

        return null;
    }


    /**
     * 点对象
     */
    public static class Point{
        public static int STATE_NORMAL = 0;
        public static int STATE_PRESSED = 1;
        public static int STATE_ERROR = 2;
        public float x,y;
        public int state=0,index=0;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point() {
            this.x = x;
            this.y = y;
        }

        /**
         * 鼠标当前是否包含在一个点中
         * @param pointX 点的X
         * @param pointY 点的Y
         * @param r 点的半径
         * @param movingX 鼠标的X
         * @param movingY 鼠标Y
         * @return 是否包含
         */
        public static boolean with(float pointX,float pointY,float r,float movingX,float movingY){
            //开方
            return Math.sqrt((pointX-movingX)*(pointX-movingX)+(pointY-movingY)*(pointY-movingY)) < r;
        }

        public static double distance (Point a ,Point b){
            return Math.sqrt((Math.abs(a.x-b.x)*Math.abs(a.x-b.x))+(Math.abs(a.y-b.y)*Math.abs(a.y-b.y)));
        }
    }


}
