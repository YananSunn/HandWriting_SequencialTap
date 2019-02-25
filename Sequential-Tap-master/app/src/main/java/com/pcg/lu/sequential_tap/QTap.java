package com.pcg.lu.sequential_tap;

public class QTap {
    public int index;
    public int order;
    public boolean direction;
    // up = false
    // down = true

    public  QTap(int index, int order, boolean direction){
        this.index = index;
        this.order = order;
        this.direction = direction;
    }
}
