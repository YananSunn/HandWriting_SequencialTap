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

    final static int[] fingerX = new int[]{100, 400, 580, 760, 940};
    final static int[] fingerY = new int[]{700, 500, 400, 490, 620};

    Bitmap bitmap = null;
    Canvas canvas = null;

    private static Handler handler=new Handler();

    final int seqSize = 2;
    QSequential[] qSequentials = new QSequential[seqSize];

    final long seqInterval = 300;
    final long synInterval = 15;
    Paint p_red = new Paint();
    Paint p_blue = new Paint();

    MyRunnable runnable = new MyRunnable(0);
    public Thread thread;

    public DrawFlash(Context context, QSequential[] qSequentials) {
        super(context);
        p_red.setColor(Color.RED);
        p_red.setStrokeWidth(10);
        p_blue.setColor(Color.BLUE);

        for(int i = 0; i < seqSize; i++){
            this.qSequentials[i] = new QSequential();
        }
        initialQSequential();
    }
    public void initialQSequential(){
        final long seqInterval = 300;
        final long synInterval = 15;

        for(int i = 0; i < seqSize; i++){
            qSequentials[i] = new QSequential();
        }

        qSequentials[0].qTaps.add(new QTap(1,1,true));
        qSequentials[0].qTaps.add(new QTap(1,1,false));
        qSequentials[0].qTaps.add(new QTap(1,1,true));
        qSequentials[0].qTaps.add(new QTap(1,1,false));
//        qSequentials[0].qTaps.add(new QTap(2,2,true));
//        qSequentials[0].qTaps.add(new QTap(3,3,true));
//        qSequentials[0].qTaps.add(new QTap(2,5,false));
//        qSequentials[0].qTaps.add(new QTap(3,6,false));

        qSequentials[1].qTaps.add(new QTap(1,1,true));
        qSequentials[1].qTaps.add(new QTap(4,2,true));
        qSequentials[1].qTaps.add(new QTap(1,3,false));
        qSequentials[1].qTaps.add(new QTap(4,4,false));

        qSequentials[0].tapName = "打开微信";
        qSequentials[1].tapName = "调整音量";

        for(int i = 0; i < seqSize; i++){
            for(int j = 0; j < qSequentials[i].qTaps.size() - 1; j++){
                if(qSequentials[i].qTaps.get(j).order == qSequentials[i].qTaps.get(j+1).order){
                    qSequentials[i].runTime = qSequentials[i].runTime + synInterval;
                }
                else {
                    qSequentials[i].runTime = qSequentials[i].runTime + seqInterval;
                }
            }
            qSequentials[i].runTime = qSequentials[i].runTime + 2000;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public class MyRunnable implements Runnable {
        int seqNum;

        public MyRunnable(int seqNum){
            this.seqNum = seqNum;
        }
        public void setSeqNum(int seqNum){
            this.seqNum = seqNum;
        }

        public void run() {
            int counter = 0;
            boolean synchron = false;
            while (counter < qSequentials[seqNum].qTaps.size()) {
                if(Thread.currentThread().isInterrupted())
                {
                    System.out.println("heeeere in isinterrupt()");
                    break;
                }
                else
                {
                    if(counter == qSequentials[seqNum].qTaps.size() - 1 || counter == qSequentials[seqNum].qTaps.size()){
                        // 顺序绘制
                        synchron = false;
                    }
                    else if(qSequentials[seqNum].qTaps.get(counter).order != qSequentials[seqNum].qTaps.get(counter + 1).order){
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
                    System.out.println("heeeere"+"qtaps.get(counter).index:"+qSequentials[seqNum].qTaps.get(counter).index);
                    try {
                        Thread.sleep(seqInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 开始绘制

                    if(qSequentials[seqNum].qTaps.get(counter).direction){
                        switch (qSequentials[seqNum].qTaps.get(counter).index){
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
                        switch (qSequentials[seqNum].qTaps.get(counter).index){
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
//                if(counter == qSequentials[seqNum].qTaps.size()){
//                    System.out.println("heeeere"+"试图将counter归零");
//                    counter = 0;
//                }

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
            System.out.println("heeeere"+"run 运行结束");
        }
    };

    void drawTapFlash(int seqNum) {
        this.runnable.setSeqNum(seqNum);
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

        thread = new Thread(runnable);
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
