package com.pcg.lu.sequential_tap;

import java.util.ArrayList;

public class QGesture {
    public ArrayList<Point> qPoints;
    public String gestureName;
    public boolean setup;

    public long runTime;

    public QGesture(){
        qPoints = new ArrayList();
        gestureName = null;
        setup = false;
        runTime = 0;
    }
}
