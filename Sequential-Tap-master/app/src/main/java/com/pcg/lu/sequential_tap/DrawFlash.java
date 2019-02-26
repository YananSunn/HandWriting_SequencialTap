package com.pcg.lu.sequential_tap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class DrawFlash extends View {
    final static int DEVICE_WIDTH = 1440;
    final static int DEVICE_HEIGHT = 2960;

    final static int[] fingerX = new int[]{100, 350, 550, 750, 950};
    final static int[] fingerY = new int[]{600, 400, 300, 400, 530};

    Bitmap bitmap = null;
    Canvas canvas = null;

    private static Handler handler=new Handler();


    public DrawFlash(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    void drawTapFlash(final ArrayList<QTap> qtaps) {
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        final Paint p_red = new Paint();
        p_red.setColor(Color.RED);
        p_red.setStrokeWidth(10);

       final Paint p_blue = new Paint();
        p_blue.setColor(Color.BLUE);

        for (int i = 0; i < 5; i++){
            canvas.drawCircle(fingerX[i], fingerY[i], 50, p_blue);
        }

        int tapSize = qtaps.size();
        final long seqInterval = 300;
        final long synInterval = 15;


        Runnable runnable = new Runnable() {
            public void run() {
                int counter = 0;
                boolean synchron = false;
                while (true) {
                    if(counter == qtaps.size() - 1 || counter == qtaps.size()){
                        // 顺序绘制
                        synchron = false;
                    }
                    else if(qtaps.get(counter).order != qtaps.get(counter + 1).order){
                        // 顺序绘制
                        synchron = false;
                    }
                    else{
                        // 同时绘制
                        synchron = true;
                    }
                    System.out.println("heeeere in the while");
                    System.out.println("heeeere"+"synchron:" + synchron);
                    System.out.println("heeeere"+"counter:"+counter);
                    System.out.println("heeeere"+"qtaps.get(counter).index:"+qtaps.get(counter).index);
                    try {
                        Thread.sleep(seqInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 开始绘制

                    if(qtaps.get(counter).direction){
                        switch (qtaps.get(counter).index){
                            case 0:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[0], fingerY[0], 50, p_red);
                                        System.out.println("heeeere"+"已经将第0个触点绘制为红色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 1:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[1], fingerY[1], 50, p_red);
                                        System.out.println("heeeere"+"已经将第1个触点绘制为红色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 2:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[2], fingerY[2], 50, p_red);
                                        System.out.println("heeeere"+"已经将第2个触点绘制为红色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 3:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[3], fingerY[3], 50, p_red);
                                        System.out.println("heeeere"+"已经将第3个触点绘制为红色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 4:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[4], fingerY[4], 50, p_red);
                                        System.out.println("heeeere"+"已经将第4个触点绘制为红色");
                                        invalidate();
                                    }
                                });
                                break;
                        }

                    }
                    else{
                        switch (qtaps.get(counter).index){
                            case 0:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[0], fingerY[0], 50, p_blue);
                                        System.out.println("heeeere"+"已经将第0个触点绘制为蓝色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 1:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[1], fingerY[1], 50, p_blue);
                                        System.out.println("heeeere"+"已经将第1个触点绘制为蓝色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 2:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[2], fingerY[2], 50, p_blue);
                                        System.out.println("heeeere"+"已经将第2个触点绘制为蓝色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 3:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[3], fingerY[3], 50, p_blue);
                                        System.out.println("heeeere"+"已经将第3个触点绘制为蓝色");
                                        invalidate();
                                    }
                                });
                                break;
                            case 4:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[4], fingerY[4], 50, p_blue);
                                        System.out.println("heeeere"+"已经将第4个触点绘制为蓝色");
                                        invalidate();
                                    }
                                });
                                break;
                        }}

                    counter = counter + 1;
                    if(counter == qtaps.size()){
                        System.out.println("heeeere"+"试图将counter归零");
                        counter = 0;
                    }

                    if(synchron){
                        try {
                            Thread.sleep(synInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            Thread.sleep(seqInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }


    void drawNothing(){
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        invalidate();
    }

    void drawShape(ArrayList<Point> qmoves) {
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        Paint p_blue = new Paint();
        p_blue.setColor(Color.BLUE);

        for (int i = 0; i < qmoves.size(); i++) {
            Point m_point = qmoves.get(i);
            canvas.drawCircle(m_point.x , m_point.y - 250, 10, p_blue);
        }
        invalidate();
    }
}
