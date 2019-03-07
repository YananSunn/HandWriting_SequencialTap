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
    final int gestureSize = 2;

    int gestureCounter = 0;
    QSequential[] qSequentials = new QSequential[seqSize];
    QGesture[] qGestures = new QGesture[gestureSize];

    final long seqInterval = 500;
    final long synInterval = 100;
    Paint p_red = new Paint();
    Paint p_blue = new Paint();
    Paint p_white = new Paint();

    TapFlashRunnable tapRunnable = new TapFlashRunnable(0);
    ShapeFlashRunnable shapeRunnable = new ShapeFlashRunnable(0);
    public Thread tapThread;
    public Thread shapeThread;

    public DrawFlash(Context context) {
        super(context);
        p_red.setColor(Color.RED);
        p_red.setStrokeWidth(10);
        p_blue.setColor(Color.BLUE);
        p_white.setColor(Color.WHITE);

        initialQSequential();
        initialQGesture();
    }
    public void initialQSequential(){
        final long seqInterval = 500;
        final long synInterval = 100;

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
    public void initialQGesture(){
        for(int i = 0; i < seqSize; i++){
            qGestures[i] = new QGesture();
        }
        qGestures[0].qPoints.add(new Point(300,350));
        qGestures[0].qPoints.add(new Point(400,650));
        qGestures[0].qPoints.add(new Point(500,350));
        qGestures[0].qPoints.add(new Point(600,650));
        qGestures[0].qPoints.add(new Point(700,350));

        qGestures[1].qPoints.add(new Point(300,650));
        qGestures[1].qPoints.add(new Point(400,350));
        qGestures[1].qPoints.add(new Point(500,650));
        qGestures[1].qPoints.add(new Point(600,350));
        qGestures[1].qPoints.add(new Point(700,650));

        qGestures[0].gestureName = "打开微博";
        qGestures[1].gestureName = "打开视频播放器";

        for(int i = 0; i < gestureSize; i++){
            qGestures[i].runTime = qGestures[i].qPoints.size() * 500 + 1000;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public class TapFlashRunnable implements Runnable {
        int seqNum;

        public TapFlashRunnable(int seqNum){
            this.seqNum = seqNum;
        }
        public void setSeqNum(int seqNum){
            this.seqNum = seqNum;
        }

        public void run() {
            int counter = 0;
            boolean synchron = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        this.tapRunnable.setSeqNum(seqNum);
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        Paint p_blue = new Paint();
        p_blue.setColor(Color.BLUE);

        for (int i = 0; i < 5; i++){
            canvas.drawCircle(fingerX[i], fingerY[i], 50, p_blue);
        }
        invalidate();

        tapThread = new Thread(tapRunnable);
        tapThread.start();

    }


    public class ShapeFlashRunnable implements Runnable {
        int gestureNum;

        public ShapeFlashRunnable(int gestureNum){
            this.gestureNum = gestureNum;
        }
        public void setGestureNum(int gestureNum){
            this.gestureNum = gestureNum;
        }

        public void run() {

            while (gestureCounter < qGestures[gestureNum].qPoints.size()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(gestureCounter == 0){
                    handler.post(new Runnable() {
                        public void run() {
                            System.out.println("heeeere shaperun counter = 0");
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter).x, qGestures[gestureNum].qPoints.get(gestureCounter).y, 50, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
                else if(gestureCounter == qGestures[gestureNum].qPoints.size()){
                    handler.post(new Runnable() {
                        public void run() {
                            System.out.println("heeeere shaperun counter = size");
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 30, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            System.out.println("heeeere shaperun counter = mid");
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter).x, qGestures[gestureNum].qPoints.get(gestureCounter).y, 50, p_red);
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 50, p_white);
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 30, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    void drawShapeFlash(int gestureNum) {
        shapeRunnable.setGestureNum(gestureNum);
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        Paint p_blue = new Paint();
        p_blue.setColor(Color.BLUE);

        for (int i = 0; i < qGestures[gestureNum].qPoints.size(); i++) {
            System.out.println("heeeere x:"+ qGestures[gestureNum].qPoints.get(i).x + "y:" + qGestures[gestureNum].qPoints.get(i).y);
            canvas.drawCircle(qGestures[gestureNum].qPoints.get(i).x , qGestures[gestureNum].qPoints.get(i).y, 30, p_blue);
        }
        invalidate();

        gestureCounter = 0;
        shapeThread = new Thread(shapeRunnable);
        shapeThread.start();
    }

    void drawNothing(){
        bitmap = Bitmap.createBitmap(DEVICE_WIDTH, DEVICE_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        invalidate();
    }
}
