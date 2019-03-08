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

    final int seqSize = 7;
    final int gestureSize = 7;

    int gestureCounter = 0;
    QSequential[] qSequentials = new QSequential[seqSize];
    QGesture[] qGestures = new QGesture[gestureSize];

    final long seqInterval = 300;
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
        final long seqInterval = 300;
        final long synInterval = 100;

        for(int i = 0; i < seqSize; i++){
            qSequentials[i] = new QSequential();
        }

        //打开微信 23
        qSequentials[0].tapName = "打开微信";
        qSequentials[0].qTaps.add(new QTap(1,1,true));
        qSequentials[0].qTaps.add(new QTap(1,2,false));
        qSequentials[0].qTaps.add(new QTap(2,3,true));
        qSequentials[0].qTaps.add(new QTap(2,4,false));

        //打开信息 24
        qSequentials[1].tapName = "打开信息";
        qSequentials[1].qTaps.add(new QTap(1,1,true));
        qSequentials[1].qTaps.add(new QTap(1,2,false));
        qSequentials[1].qTaps.add(new QTap(3,3,true));
        qSequentials[1].qTaps.add(new QTap(3,4,false));

        //打开电话 234
        qSequentials[2].tapName = "打开电话";
        qSequentials[2].qTaps.add(new QTap(1,1,true));
        qSequentials[2].qTaps.add(new QTap(1,2,false));
        qSequentials[2].qTaps.add(new QTap(2,3,true));
        qSequentials[2].qTaps.add(new QTap(2,4,false));
        qSequentials[2].qTaps.add(new QTap(3,3,true));
        qSequentials[2].qTaps.add(new QTap(3,4,false));

        //打开微博 32
        qSequentials[3].tapName = "打开微博";
        qSequentials[3].qTaps.add(new QTap(2,1,true));
        qSequentials[3].qTaps.add(new QTap(2,2,false));
        qSequentials[3].qTaps.add(new QTap(1,3,true));
        qSequentials[3].qTaps.add(new QTap(1,4,false));

        //打开视频播放器 324
        qSequentials[4].tapName = "打开视频播放器";
        qSequentials[4].qTaps.add(new QTap(2,1,true));
        qSequentials[4].qTaps.add(new QTap(2,2,false));
        qSequentials[4].qTaps.add(new QTap(1,3,true));
        qSequentials[4].qTaps.add(new QTap(1,4,false));
        qSequentials[4].qTaps.add(new QTap(3,3,true));
        qSequentials[4].qTaps.add(new QTap(3,4,false));

        //打开音乐播放器 432
        qSequentials[5].tapName = "打开音乐播放器";
        qSequentials[5].qTaps.add(new QTap(3,1,true));
        qSequentials[5].qTaps.add(new QTap(3,2,false));
        qSequentials[5].qTaps.add(new QTap(2,3,true));
        qSequentials[5].qTaps.add(new QTap(2,4,false));
        qSequentials[5].qTaps.add(new QTap(1,5,true));
        qSequentials[5].qTaps.add(new QTap(1,6,false));

        //打开支付宝 42
        qSequentials[6].tapName = "打开支付宝";
        qSequentials[6].qTaps.add(new QTap(3,1,true));
        qSequentials[6].qTaps.add(new QTap(3,2,false));
        qSequentials[6].qTaps.add(new QTap(1,3,true));
        qSequentials[6].qTaps.add(new QTap(1,4,false));

        for(int i = 0; i < seqSize; i++){
            for(int j = 0; j < qSequentials[i].qTaps.size() - 1; j++){
                if(qSequentials[i].qTaps.get(j).order == qSequentials[i].qTaps.get(j+1).order){
                    qSequentials[i].runTime = qSequentials[i].runTime + synInterval;
                }
                else {
                    qSequentials[i].runTime = qSequentials[i].runTime + seqInterval;
                }
            }
            qSequentials[i].runTime = qSequentials[i].runTime + 1000;
        }
    }
    public void initialQGesture(){
        for(int i = 0; i < seqSize; i++){
            qGestures[i] = new QGesture();
        }
        // 打开微信 W
        qGestures[0].gestureName = "打开微信";
        qGestures[0].qPoints.add(new Point(300,400));
        qGestures[0].qPoints.add(new Point(350,550));
        qGestures[0].qPoints.add(new Point(400,700));
        qGestures[0].qPoints.add(new Point(450,550));
        qGestures[0].qPoints.add(new Point(500,400));
        qGestures[0].qPoints.add(new Point(550,550));
        qGestures[0].qPoints.add(new Point(600,700));
        qGestures[0].qPoints.add(new Point(650,550));
        qGestures[0].qPoints.add(new Point(700,400));

        // 打开信息 X
        qGestures[1].gestureName = "打开信息";
        qGestures[1].qPoints.add(new Point(400,400));
        qGestures[1].qPoints.add(new Point(500,550));
        qGestures[1].qPoints.add(new Point(600,700));
        qGestures[1].qPoints.add(new Point(600,550));
        qGestures[1].qPoints.add(new Point(600,400));
        qGestures[1].qPoints.add(new Point(500,550));
        qGestures[1].qPoints.add(new Point(400,700));

        // 打开电话 D
        qGestures[2].gestureName = "打开电话";
        qGestures[2].qPoints.add(new Point(400,700));
        qGestures[2].qPoints.add(new Point(400,550));
        qGestures[2].qPoints.add(new Point(400,400));

        qGestures[2].qPoints.add(new Point(460,440));
        qGestures[2].qPoints.add(new Point(520,480));
        qGestures[2].qPoints.add(new Point(550,550));
        qGestures[2].qPoints.add(new Point(520,620));
        qGestures[2].qPoints.add(new Point(460,660));
        qGestures[2].qPoints.add(new Point(400,700));

        // 打开微博 椭圆
        qGestures[3].gestureName = "打开微博";
        qGestures[3].qPoints.add(new Point(550,400));
        qGestures[3].qPoints.add(new Point(475,420));
        qGestures[3].qPoints.add(new Point(400,475));
        qGestures[3].qPoints.add(new Point(475,530));
        qGestures[3].qPoints.add(new Point(550,550));
        qGestures[3].qPoints.add(new Point(625,530));
        qGestures[3].qPoints.add(new Point(700,475));
        qGestures[3].qPoints.add(new Point(625,420));
        qGestures[3].qPoints.add(new Point(550,400));

        // 打开音乐播放器 音符
        qGestures[4].gestureName = "打开音乐播放器";
        qGestures[4].qPoints.add(new Point(650,400));
        qGestures[4].qPoints.add(new Point(600,400));
        qGestures[4].qPoints.add(new Point(550,400));
        qGestures[4].qPoints.add(new Point(550,450));
        qGestures[4].qPoints.add(new Point(550,500));
        qGestures[4].qPoints.add(new Point(550,550));
        qGestures[4].qPoints.add(new Point(550,600));
        qGestures[4].qPoints.add(new Point(550,650));
        qGestures[4].qPoints.add(new Point(550,700));

        qGestures[4].qPoints.add(new Point(525,690));
        qGestures[4].qPoints.add(new Point(500,650));
        qGestures[4].qPoints.add(new Point(525,610));
        qGestures[4].qPoints.add(new Point(550,600));


        // 打开视频播放器 三角
        qGestures[5].gestureName = "打开视频播放器";
        qGestures[5].qPoints.add(new Point(450,400));
        qGestures[5].qPoints.add(new Point(450,550));
        qGestures[5].qPoints.add(new Point(450,700));
        qGestures[5].qPoints.add(new Point(550,625));
        qGestures[5].qPoints.add(new Point(650,550));
        qGestures[5].qPoints.add(new Point(550,475));
        qGestures[5].qPoints.add(new Point(450,400));

        // 打开支付宝 S
        qGestures[6].gestureName = "打开支付宝";
        qGestures[6].qPoints.add(new Point(650,475));
        qGestures[6].qPoints.add(new Point(600,420));

        qGestures[6].qPoints.add(new Point(550,400));
        qGestures[6].qPoints.add(new Point(500,420));

        qGestures[6].qPoints.add(new Point(450,475));
        qGestures[6].qPoints.add(new Point(500,530));

        qGestures[6].qPoints.add(new Point(550,550));

        qGestures[6].qPoints.add(new Point(600,570));
        qGestures[6].qPoints.add(new Point(650,625));

        qGestures[6].qPoints.add(new Point(600,680));
        qGestures[6].qPoints.add(new Point(550,700));

        qGestures[6].qPoints.add(new Point(500,680));
        qGestures[6].qPoints.add(new Point(450,625));


        for(int i = 0; i < gestureSize; i++){
            qGestures[i].runTime = qGestures[i].qPoints.size() * 200 + 1000;
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
                                        invalidate();
                                    }
                                });
                                break;
                            case 1:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[1], fingerY[1], 50, p_red);
                                        invalidate();
                                    }
                                });
                                break;
                            case 2:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[2], fingerY[2], 50, p_red);
                                        invalidate();
                                    }
                                });
                                break;
                            case 3:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[3], fingerY[3], 50, p_red);
                                        invalidate();
                                    }
                                });
                                break;
                            case 4:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[4], fingerY[4], 50, p_red);
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
                                        invalidate();
                                    }
                                });
                                break;
                            case 1:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[1], fingerY[1], 50, p_blue);
                                        invalidate();
                                    }
                                });
                                break;
                            case 2:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[2], fingerY[2], 50, p_blue);
                                        invalidate();
                                    }
                                });
                                break;
                            case 3:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[3], fingerY[3], 50, p_blue);
                                        invalidate();
                                    }
                                });
                                break;
                            case 4:
                                handler.post(new Runnable(){
                                    public void run(){
                                        canvas.drawCircle(fingerX[4], fingerY[4], 50, p_blue);
                                        invalidate();
                                    }
                                });
                                break;
                        }}
                    counter = counter + 1;
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
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(gestureCounter == 0){
                    handler.post(new Runnable() {
                        public void run() {
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter).x, qGestures[gestureNum].qPoints.get(gestureCounter).y, 30, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
                else if(gestureCounter == qGestures[gestureNum].qPoints.size()){
                    handler.post(new Runnable() {
                        public void run() {
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 10, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
                else {
                    handler.post(new Runnable() {
                        public void run() {
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter).x, qGestures[gestureNum].qPoints.get(gestureCounter).y, 30, p_red);
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 30, p_white);
                            canvas.drawCircle(qGestures[gestureNum].qPoints.get(gestureCounter-1).x, qGestures[gestureNum].qPoints.get(gestureCounter-1).y, 10, p_red);
                            invalidate();
                            gestureCounter = gestureCounter + 1;
                        }
                    });

                }
            }
            try {
                Thread.sleep(200);
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
            canvas.drawCircle(qGestures[gestureNum].qPoints.get(i).x , qGestures[gestureNum].qPoints.get(i).y, 10, p_blue);
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
