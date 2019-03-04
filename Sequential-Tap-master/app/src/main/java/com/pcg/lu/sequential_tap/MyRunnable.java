package com.pcg.lu.sequential_tap;

import android.graphics.Canvas;
import android.os.Handler;

import java.util.ArrayList;

public class MyRunnable implements Runnable {
    final static int[] fingerX = new int[]{100, 350, 550, 750, 950};
    final static int[] fingerY = new int[]{600, 400, 300, 400, 530};

    public boolean isDrawing = false;
    public ArrayList<QTap> qtaps;

    int tapSize = qtaps.size();
    final long seqInterval = 300;
    final long synInterval = 15;

    private static Handler handler=new Handler();
    Canvas canvas = null;

    public void run() {
//        int counter = 0;
//        boolean synchron = false;
//        while (isDrawing) {
//            if(counter == qtaps.size() - 1 || counter == qtaps.size()){
//                // 顺序绘制
//                synchron = false;
//            }
//            else if(qtaps.get(counter).order != qtaps.get(counter + 1).order){
//                // 顺序绘制
//                synchron = false;
//            }
//            else{
//                // 同时绘制
//                synchron = true;
//            }
//            System.out.println("heeeere in the while");
//            System.out.println("heeeere"+"synchron:" + synchron);
//            System.out.println("heeeere"+"counter:"+counter);
//            System.out.println("heeeere"+"qtaps.get(counter).index:"+qtaps.get(counter).index);
//            try {
//                Thread.sleep(seqInterval);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            // 开始绘制
//
//            if(qtaps.get(counter).direction){
//                switch (qtaps.get(counter).index){
//                    case 0:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[0], fingerY[0], 50, p_red);
//                                System.out.println("heeeere"+"已经将第0个触点绘制为红色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 1:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[1], fingerY[1], 50, p_red);
//                                System.out.println("heeeere"+"已经将第1个触点绘制为红色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 2:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[2], fingerY[2], 50, p_red);
//                                System.out.println("heeeere"+"已经将第2个触点绘制为红色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 3:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[3], fingerY[3], 50, p_red);
//                                System.out.println("heeeere"+"已经将第3个触点绘制为红色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 4:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[4], fingerY[4], 50, p_red);
//                                System.out.println("heeeere"+"已经将第4个触点绘制为红色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                }
//
//            }
//            else{
//                switch (qtaps.get(counter).index){
//                    case 0:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[0], fingerY[0], 50, p_blue);
//                                System.out.println("heeeere"+"已经将第0个触点绘制为蓝色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 1:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[1], fingerY[1], 50, p_blue);
//                                System.out.println("heeeere"+"已经将第1个触点绘制为蓝色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 2:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[2], fingerY[2], 50, p_blue);
//                                System.out.println("heeeere"+"已经将第2个触点绘制为蓝色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 3:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[3], fingerY[3], 50, p_blue);
//                                System.out.println("heeeere"+"已经将第3个触点绘制为蓝色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                    case 4:
//                        handler.post(new Runnable(){
//                            public void run(){
//                                canvas.drawCircle(fingerX[4], fingerY[4], 50, p_blue);
//                                System.out.println("heeeere"+"已经将第4个触点绘制为蓝色");
//                                invalidate();
//                            }
//                        });
//                        break;
//                }}
//
//            counter = counter + 1;
//            if(counter == qtaps.size()){
//                System.out.println("heeeere"+"试图将counter归零");
//                counter = 0;
//            }
//
//            if(synchron){
//                try {
//                    Thread.sleep(synInterval);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            else{
//                try {
//                    Thread.sleep(seqInterval);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
