package com.pcg.lu.sequential_tap;

import java.util.ArrayList;

public class Candidate {
    String name;
    ArrayList<ArrayList<Point>> points;
    Candidate(String a, ArrayList<Point> b){
        name = a;
        points = new ArrayList();
        points.add(b);
    }
}

class Candidate_s{
    String name;
    ArrayList<ArrayList<QTouch>> touchs;
    Candidate_s(String a, ArrayList<QTouch> b){
        name = a;
        touchs = new ArrayList();
        touchs.add(b);
    }
}

class RecognizeResult{
    Candidate template;
    double score;
    RecognizeResult(){

    }
    RecognizeResult(Candidate a, double b){
        template = a;
        score = b;
    }
}
