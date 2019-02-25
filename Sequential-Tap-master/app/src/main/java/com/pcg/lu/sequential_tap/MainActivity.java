package com.pcg.lu.sequential_tap;

import android.app.AlertDialog;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public final static int VIEW_SEQUENTIAL = 0;
    public final static int VIEW_GESTURE = 1;

    public final static int GESTURE_SETUP = 2;
    public final static int GESTURE_LEARN = 3;
    public final static int GESTURE_TEST = 4;

    public final static int SEQUENTIAL_SETUP = 5;
    public final static int SEQUENTIAL_LEARN = 6;
    public final static int SEQUENTIAL_TEST = 7;

    DrawView drawView;
    DrawView drawView2;
    DrawFlash drawFlash;
    TextView view;
    Button changeState;
    Button save;
    EditText edit;
    TextView view_s;
//    Button changeState_s;
    Button save_s;
    EditText edit_s;

    Spinner mySpinner_s;
    List<String> list_s = new ArrayList<String>();
    ArrayAdapter<String> adapter_s;

    TextView gestureName;
    Button startTest;

    TextView sequentialName;
    Button startTest_s;

    int page = VIEW_SEQUENTIAL;
    int state = GESTURE_SETUP;
    int state_s = SEQUENTIAL_SETUP;


    boolean isTesting = false;
    int testTime = 50;
    int testNum = 0;
    int rightCaseNum = 0;
    int wrongCaseNum = 0;
    String[] testCase;

    long startTime;
    long endTime;
    long usedTime;

    boolean isTesting_s = false;
    int testTime_s = 50;
    int testNum_s = 0;
    int rightCaseNum_s = 0;
    int wrongCaseNum_s = 0;
    String[] testCase_s;

    long startTime_s;
    long endTime_s;
    long usedTime_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onChangeView(VIEW_SEQUENTIAL);
        initialFindViews();
    }

    public void initialFindViews(){
        view_s = (TextView)findViewById(R.id.textView_s);
//        changeState_s = (Button)findViewById(R.id.button1_s);
        save_s = (Button)findViewById(R.id.button2_s);
        edit_s = (EditText)findViewById(R.id.editText_s);
        sequentialName = (TextView)findViewById(R.id.sequential_name);
        startTest_s = (Button)findViewById(R.id.start_test_s);


        sequentialName.setVisibility(View.GONE);
        startTest_s.setVisibility(View.GONE);

        mySpinner_s = (Spinner)findViewById(R.id.spinner);
        list_s.add("初始设置");
        list_s.add("学习模式");
        list_s.add("测试模式");
        adapter_s = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_s);
        adapter_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner_s.setAdapter(adapter_s);

        mySpinner_s.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                /* 将所选mySpinner 的值带入myTextView 中 */
                switch (arg2){
                    case 0:
                        state_s = SEQUENTIAL_SETUP;
                        break;
                    case 1:
                        state_s = SEQUENTIAL_LEARN;
                        break;
                    case 2:
                        state_s = SEQUENTIAL_TEST;
                        break;
                }
                StateOnChange_s();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    void onChangeView(int target) {
        page = target;
        switch (page) {
            case VIEW_SEQUENTIAL:
                setContentView(R.layout.sequential_tap);
                ConstraintLayout layout = findViewById(R.id.main_layout);
                drawView = new DrawView(this);
                //drawView.setBackgroundColor(Color.BLACK);
                //drawView.setAlpha(0.5f);
                layout.addView(drawView);
                drawFlash = new DrawFlash(this);
                layout.addView(drawFlash);
                break;
            case VIEW_GESTURE:
                setContentView(R.layout.gesture);
                ConstraintLayout layout_gesture = findViewById(R.id.gesture_layout);
                drawView2 = new DrawView(this);
                layout_gesture.addView(drawView2);
                break;
        }
    }

    TouchEvent[] touchEvent = new TouchEvent[10];
    ArrayList<QTouch> qtouchs = new ArrayList();
    ArrayList<QTouch> qtouchs_copy = new ArrayList();
    ArrayList<Point> qmoves = new ArrayList();
    ArrayList<Candidate> candidates = new ArrayList();
    ArrayList<Candidate_s> candidates_s = new ArrayList();

    ArrayList<QTap> qtaps = new ArrayList();


    Timer idleTimer = null;

    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);

        int n = event.getPointerCount();
        int index = event.getActionIndex();
        int pointerID = event.getPointerId(index);
        int x = (int)event.getX(index);
        int y = (int)event.getY(index);

        switch (page) {
            case VIEW_SEQUENTIAL:
                switch (state_s){
                    case SEQUENTIAL_SETUP:
                        switch (event.getActionMasked()) {
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_POINTER_DOWN:
                                touchEvent[pointerID] = new TouchEvent(this, x, y);
                                if (idleTimer != null) {
                                    idleTimer.cancel();
                                    idleTimer = null;
                                }
                                break;

                            case MotionEvent.ACTION_MOVE:
                                touchEvent[pointerID].move(x, y);
                                break;

                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_POINTER_UP:
                                TouchEvent touch = touchEvent[pointerID];
                                touch.up(x, y);
                                switch (page) {
                                    case VIEW_SEQUENTIAL:
                                        qtouchs.add(new QTouch((touch.x + touch.downX) / 2, (touch.y + touch.downY) / 2, touch.downTime, touch.currentTime));
                                        if (n - 1 == 0) {
                                            idleTimer = new Timer();
                                            idleTimer.schedule(new IdleTimerTask(), 200);
                                        }
                                        break;
                                }
                                touchEvent[pointerID] = null;
                                break;
                        }
                        break;
                    case SEQUENTIAL_TEST:
                         switch (event.getActionMasked()) {
                             case MotionEvent.ACTION_DOWN:
                             case MotionEvent.ACTION_POINTER_DOWN:
                                 touchEvent[pointerID] = new TouchEvent(this, x, y);
                                 if (idleTimer != null) {
                                     idleTimer.cancel();
                                     idleTimer = null;
                                 }
                                 break;

                             case MotionEvent.ACTION_MOVE:
                                 touchEvent[pointerID].move(x, y);
                                 break;

                             case MotionEvent.ACTION_UP:
                             case MotionEvent.ACTION_POINTER_UP:
                                 TouchEvent touch = touchEvent[pointerID];
                                 touch.up(x, y);
                                 switch (page) {
                                     case VIEW_SEQUENTIAL:
                                         qtouchs.add(new QTouch((touch.x + touch.downX) / 2, (touch.y + touch.downY) / 2, touch.downTime, touch.currentTime));
                                         if (n - 1 == 0) {
                                             idleTimer = new Timer();
                                             idleTimer.schedule(new IdleTimerTask(), 200);
                                         }
                                         break;
                                 }
                                 touchEvent[pointerID] = null;
                                 break;
                         }
                         break;
                }
                break;
            case VIEW_GESTURE:
                switch (state) {
                    case GESTURE_SETUP:
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                qmoves.clear();
                                qmoves.add(new Point(x, y));
                                break;
                            case MotionEvent.ACTION_MOVE:
                                qmoves.add(new Point(x, y));
                                break;
                            case MotionEvent.ACTION_UP:
                                qmoves.add(new Point(x, y));
                                qmoves = SetUpCandi(qmoves);
                                break;
                        }
                        break;
                    case GESTURE_TEST:
                        if(isTesting){
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    qmoves.clear();
                                    view.setText("");
                                    qmoves.add(new Point(x, y));
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    qmoves.add(new Point(x, y));
                                    break;
                                case MotionEvent.ACTION_UP:
                                    qmoves.add(new Point(x, y));
                                    String a = CompareCandi(qmoves);
                                    if(a == testCase[testNum]){
                                        rightCaseNum = rightCaseNum + 1;
                                    }
                                    else {
                                        wrongCaseNum = wrongCaseNum + 1;
                                    }
                                    testNum = testNum + 1;
                                   // System.out.println("heeeere "+testNum);
                                    if(testNum == testTime){
                                        //System.out.println("heeeere testNum=testTime");
                                        endTime = System.currentTimeMillis();
                                        usedTime = endTime-startTime;
                                        isTesting = false;
                                        testNum = 0;
                                        startTest.setEnabled(true);
                                        //System.out.println("heeeere before the dialog");
                                        AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
                                        nameNull.setTitle("测试已完成" ) ;
                                        nameNull.setMessage("用时"+usedTime+"ms\n"+"正确率"+(double)rightCaseNum/(rightCaseNum+wrongCaseNum) ) ;
                                        rightCaseNum = 0;
                                        wrongCaseNum = 0;
                                        nameNull.setPositiveButton("ok" ,  null );
                                        nameNull.show();
                                    }
                                    else{
                                        gestureName.setText("请绘制 "+testCase[testNum]);
                                    }
                                    qmoves.clear();
                                    break;
                            }
                        }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    class IdleTimerTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    QTouch.sequentialize(qtouchs);
                    drawView.drawIntervel(qtouchs);
                    qtouchs_copy.clear();
                    for(int i = 0; i < qtouchs.size(); i++){
                        QTouch p = new QTouch(qtouchs.get(i).x, qtouchs.get(i).y, qtouchs.get(i).t0, qtouchs.get(i).t1);
                        p.l = qtouchs.get(i).l;
                        p.r = qtouchs.get(i).r;
                        p.order = qtouchs.get(i).order;
                        p.index = qtouchs.get(i).index;
                        qtouchs_copy.add(p);
                    }
                    if(page == VIEW_SEQUENTIAL && state_s == SEQUENTIAL_TEST && isTesting_s){
                        String a = CompareCandi_s();
                        if(a == testCase_s[testNum_s]){
                            rightCaseNum_s = rightCaseNum_s + 1;
                        }
                        else {
                            wrongCaseNum_s = wrongCaseNum_s + 1;
                        }
                        testNum_s = testNum_s + 1;
                        //System.out.println("heeeere "+testNum_s);
                        if(testNum_s == testTime_s){
                            //System.out.println("heeeere testNum_s=testTime_s");
                            endTime_s = System.currentTimeMillis();
                            usedTime_s = endTime_s-startTime_s;
                            isTesting_s = false;
                            testNum_s = 0;
                            startTest_s.setEnabled(true);
                            //System.out.println("heeeere before the dialog");
                            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
                            nameNull.setTitle("测试已完成" ) ;
                            nameNull.setMessage("用时"+usedTime_s+"ms\n"+"正确率"+(double)rightCaseNum_s/(rightCaseNum_s+wrongCaseNum_s) ) ;
                            rightCaseNum_s = 0;
                            wrongCaseNum_s = 0;
                            nameNull.setPositiveButton("ok" ,  null );
                            nameNull.show();
                        }
                        else{
                            sequentialName.setText("请敲击 "+testCase_s[testNum_s]);
                        }
                    }
                    qtouchs = new ArrayList();
                }
            });
        }
    }

    public String CompareCandi_s(){
        System.out.println("heeeere"+"comparecandi_s");
        boolean isFound = false;
//        for(int i = 0; i < candidates_s.size(); i++){
//            for(int j = 0; j < candidates_s.get(i).touchs.size(); j++){
//                for(int k = 0; k < candidates_s.get(i).touchs.get(j).size(); k++){
//                    System.out.println("heeeere"+i + "," + j + ","+ k +":(t0, t1)");
//                    System.out.println(candidates_s.get(i).touchs.get(j).get(k).t0 + "," +candidates_s.get(i).touchs.get(j).get(k).t1);
//                    System.out.println("(order,index)"+candidates_s.get(i).touchs.get(j).get(k).order + "," +candidates_s.get(i).touchs.get(j).get(k).index);
//                }
//            }
//        }

        qtouchs_copy = clearDiffer(qtouchs_copy);

//        for(int i = 0; i < qtouchs_copy.size(); i++){
//            System.out.println("heeeere"+ "qtouchs_copy" + i +":(order,index)");
//            System.out.println(qtouchs_copy.get(i).order+","+qtouchs_copy.get(i).index);
//        }

        for(int i = 0; i < candidates_s.size(); i++){
            for(int j = 0; j < candidates_s.get(i).touchs.size(); j++){
                System.out.println(candidates_s.get(i).name);
                if(isFound == true){
                    System.out.println("isFound");
                    if(j == 0){
                        view_s.setText(candidates_s.get(i-1).name);
                        return candidates_s.get(i-1).name;
                    }
                    else{
                        view_s.setText(candidates_s.get(i).name);
                        return candidates_s.get(i).name;
                    }

                }
                isFound = true;
                if(candidates_s.get(i).touchs.get(j).size() == qtouchs_copy.size()){
                    for(int k = 0; k < qtouchs_copy.size()-1; k++){
                        System.out.println(candidates_s.get(i).touchs.get(j).get(k).index);
                        System.out.println(qtouchs_copy.get(k).index);
                        System.out.println(candidates_s.get(i).touchs.get(j).get(k).order);
                        System.out.println(qtouchs_copy.get(k).order);

                        if(candidates_s.get(i).touchs.get(j).get(k).index == qtouchs_copy.get(k).index
                                && candidates_s.get(i).touchs.get(j).get(k).order == qtouchs_copy.get(k).order
                                && AbsoluteVal((Math.pow(candidates_s.get(i).touchs.get(j).get(k).x - candidates_s.get(i).touchs.get(j).get(k+1).x, 2) + Math.pow(candidates_s.get(i).touchs.get(j).get(k).y - candidates_s.get(i).touchs.get(j).get(k+1).y, 2)),
                                (Math.pow(qtouchs_copy.get(k).x - qtouchs_copy.get(k+1).x, 2) + Math.pow(qtouchs_copy.get(k).y - qtouchs_copy.get(k+1).y, 2)),
                                200000) ){
                            System.out.println(candidates_s.size());
                            System.out.println(candidates_s.get(i).touchs.get(j).size());
                            System.out.println(isFound);
                            continue;
                        }
                        else {
                            isFound = false;
                            break;
                        }
                    }
                    if(candidates_s.get(i).touchs.get(j).get(qtouchs_copy.size()-1).index == qtouchs_copy.get(qtouchs_copy.size()-1).index
                            && candidates_s.get(i).touchs.get(j).get(qtouchs_copy.size()-1).order == qtouchs_copy.get(qtouchs_copy.size()-1).order){
                        // 最后的点的index和order
                    }
                    else{
                        isFound = false;
                        continue;
                    }
                }
                else {
                    isFound = false;
                    continue;
                }
            }
        }
        if(isFound == true){
            view_s.setText(candidates_s.get(candidates_s.size()-1).name);
            return candidates_s.get(candidates_s.size()-1).name;
        }

        view_s.setText("not Found");
        return null;
    }

    public boolean AbsoluteVal(double vala, double valb, double absolute){
        System.out.println("heeeere a:"+vala+" b:"+valb+" a-b:"+(vala-valb));
        if((vala - valb > 0) && vala - valb < absolute ){
            return true;
        }
        else if((vala - valb < 0) && vala -valb > -absolute){
            return true;
        }
        else {
            return false;
        }
    }
    public ArrayList<Point> SetUpCandi(ArrayList<Point>points){

        ArrayList<Point> newPoints = new ArrayList();

        if(PathLength(points) < 200.0)
        {
            AlertDialog.Builder shortLine  = new AlertDialog.Builder(MainActivity.this);
            shortLine.setTitle("提示" ) ;
            shortLine.setMessage("路径太短不可以作为手势" ) ;
            shortLine.setPositiveButton("是" ,  null );
            shortLine.show();
            return newPoints;
        }


        newPoints = Resample(points, 64);
        drawView2.drawShape(newPoints);
        newPoints = RotateToZero(newPoints);
        newPoints = ScaleToSquare(newPoints, 400);
        newPoints = TranslateToOrigin(newPoints);
        return newPoints;
    }

    // step 1 resample
    public ArrayList<Point> Resample(ArrayList<Point> points, int n){
        ArrayList<Point> newPoints = new ArrayList();

        double I = PathLength(points) / (n - 1);
        double D = 0.0;

        newPoints.add(points.get(0));
        double d = 0.0;

        for(int i = 1; i < points.size()-1; i++){
            Point first = points.get(i-1);
            Point next = points.get(i);
            d = Distance(first, next);
            Point q = new Point(0,0);
            if((D + d) >= I){
                q.x = (int)(first.x + ((I - D)/d) * (next.x - first.x));
                q.y = (int)(first.y + ((I - D)/d) * (next.y - first.y));
                newPoints.add(q);
                points.add(i, q);
                D = 0.0;
            }
            else{
                D = D + d;
            }
        }

        while(newPoints.size() < n){
            Point q = newPoints.get(newPoints.size() - 1);
            newPoints.add(q);
        }
        while(newPoints.size() > n){
            newPoints.remove(newPoints.size() - 1);
        }
        return newPoints;
    }

    public double PathLength(ArrayList<Point> points){
        double distance = 0.0;
        for(int i = 0; i < points.size() - 1; i++){
            Point first = points.get(i);
            Point next = points.get(i+1);
            distance = distance + Distance(first, next);
        }
        return distance;
    }

    public double Distance(Point a, Point b){
        double dis = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        return dis;
    }

    // step 2 rotate
    public ArrayList<Point> RotateToZero(ArrayList<Point> points) {

        ArrayList<Point> newPoints = new ArrayList();
        Point centre = Centroid(points);
        double tmp = 3.14159266;
        double angle = Math.atan2((points.get(0).y - centre.y),(points.get(0).x - centre.x));

        if(-tmp < angle && angle <= -(tmp * 0.75)){
            angle = angle + tmp;
        }
        else if(-(tmp * 0.75) < angle && angle <= -(tmp * 0.25)){
            angle = angle + tmp * 0.5;
        }
        else if(-(tmp * 0.25) < angle && angle <= tmp * 0.25){

        }
        else if(tmp * 0.25 < angle && angle <= tmp * 0.75){
            angle = angle - tmp * 0.5;
        }
        else{
            angle = angle - tmp;
        }
        newPoints = RotateBy(points, -angle);
        return newPoints;
    }

    public Point Centroid(ArrayList<Point> points){
        Point c = new Point(0, 0);
        double x = 0.0;
        double y = 0.0;
        for(int i = 0; i < points.size(); i++){
            x = x + points.get(i).x;
            y = y + points.get(i).y;
        }
        c.x = (int)(x / points.size());
        c.y = (int)(y / points.size());
        return c;
    }

    public ArrayList<Point> RotateBy(ArrayList<Point> points, double angle){
        ArrayList<Point> newPoints = new ArrayList();
        Point centre = Centroid(points);
        for(int i = 0; i < points.size(); i++){
            Point q = new Point(0, 0);
            Point p = points.get(i);
            q.x = (int)((p.x - centre.x) * Math.cos(angle) - (p.y - centre.y) * Math.sin(angle) + centre.x);
            q.y = (int)((p.x - centre.x) * Math.sin(angle) + (p.y - centre.y) * Math.cos(angle) + centre.y);
            newPoints.add(q);
        }
        return newPoints;
    }

    // step 3 resize
    public ArrayList<Point> ScaleToSquare(ArrayList<Point> points, double size){
        ArrayList<Point> newPoints = new ArrayList();
        Dimension B = BoundingBox(points);

        double AspectRatio = 0.0;
        if(B.x == 0 || B.y == 0){
            AspectRatio = 0;
        }
        else if(B.x > B.y){
            AspectRatio = B.x/B.y;
        }
        else {
            AspectRatio = B.y/B.x;
        }

        if(AspectRatio < 15){
            for(int i = 0; i < points.size(); i++){
                Point q = new Point(0,0);
                Point p = points.get(i);
                q.x = (int)(p.x * (size / B.x));
                q.y = (int)(p.y * (size / B.y));
                newPoints.add(q);
            }
        }
        else{
            for(int i = 0; i < points.size(); i++){
                Point q = new Point(0,0);
                Point p = points.get(i);
                if(B.x > B.y){
                    q.x = (int)(p.x * (size / B.x));
                    q.y = (int)(p.y);
                }
                else{
                    q.y = (int)(p.y * (size / B.y));
                    q.x = (int)(p.x);
                }

                newPoints.add(q);
            }
        }

        return newPoints;
    }

    public Dimension BoundingBox( ArrayList<Point> points){
        Dimension B = new Dimension(0, 0);
        int minx = 100000000;
        int miny = 100000000;
        int maxx = -1;
        int maxy = -1;
        for(int i = 0; i < points.size(); i++){
            Point tmp = points.get(i);
            if(tmp.x < minx){
                minx = tmp.x;
            }
            if(tmp.x > maxx){
                maxx = tmp.x;
            }
            if(tmp.y < miny){
                miny = tmp.y;
            }
            if(tmp.y > maxy){
                maxy = tmp.y;
            }
        }
        B.x = maxx - minx;
        B.y = maxy - miny;
        return B;
    }

    public ArrayList<Point> TranslateToOrigin(ArrayList<Point> points){
        ArrayList<Point> newPoints = new ArrayList();
        Point centre = Centroid(points);

        for(int i = 0; i < points.size(); i++){
            Point q = new Point(0, 0);
            Point p = points.get(i);
            q.x = p.x - centre.x;
            q.y = p.y - centre.y;
            newPoints.add(q);
        }
        return newPoints;
    }

    public String CompareCandi(ArrayList<Point>points){
        if(candidates.size() == 0){
            AlertDialog.Builder shortLine  = new AlertDialog.Builder(MainActivity.this);
            shortLine.setTitle("提示" ) ;
            shortLine.setMessage("您还没有预设手势" ) ;
            shortLine.setPositiveButton("是" ,  null );
            shortLine.show();
            return "";
        }

        if(PathLength(points) < 100.0)
        {
            AlertDialog.Builder shortLine  = new AlertDialog.Builder(MainActivity.this);
            shortLine.setTitle("提示" ) ;
            shortLine.setMessage("路径太短无法匹配" ) ;
            shortLine.setPositiveButton("是" ,  null );
            shortLine.show();
            return "";
        }

        String a = "";
        ArrayList<Point> newPoints = new ArrayList();
        newPoints = Resample(points, 64);
        newPoints = RotateToZero(newPoints);
        newPoints = ScaleToSquare(newPoints, 400);
        newPoints = TranslateToOrigin(newPoints);
        RecognizeResult result = Recognize(newPoints, candidates);

        view.setText(result.template.name);
        return result.template.name;
    }

    public RecognizeResult Recognize(ArrayList<Point> points, ArrayList<Candidate> templates){
        RecognizeResult returnResult = new RecognizeResult();
        double b = 1000000;

        drawView2.drawShape(points);
        for(int i = 0; i < templates.size(); i++){
            for(int j = 0; j < templates.get(i).points.size(); j++){
                double d = DistanceAtBestAngle(points, i, j,-0.785, 0.785, 0.0349);
                if(d < b){
                    b = d;
                    returnResult.template = templates.get(i);
                }
            }
        }

        // size:400
        double score = 1 - b/0.5 * Math.sqrt(Math.pow(400, 2) + Math.pow(400, 2));
        returnResult.score = score;
        return returnResult;
    }

    public double DistanceAtBestAngle(ArrayList<Point> points, int T, int num, double thetaA, double thetaB, double delta){

        double parameterF =  0.5 * (-1 + Math.sqrt(5));
        double x1 = parameterF * thetaA + (1 - parameterF) * thetaB;
        double f1 = DistanceAtAngle(points, T, num, x1);
        double x2 = (1 - parameterF) * thetaA + parameterF * thetaB;
        double f2 = DistanceAtAngle(points, T, num, x2);

        while ((thetaA - thetaB) > delta || (thetaB - thetaA) > delta){
            if(f1 < f2){
                thetaB = x2;
                x2 = x1;
                f2 = f1;
                x1 = parameterF * thetaA + (1 - parameterF) * thetaB;
                f1 = DistanceAtAngle(points, T, num, x1);
            }
            else {
                thetaA = x1;
                x1 = x2;
                f1 = f2;
                x2 = (1 - parameterF) * thetaA + parameterF * thetaB;
                f2 = DistanceAtAngle(points, T, num, x2);
            }
        }
        if(f1 < f2){
            return f1;
        }
        else {
            return f2;
        }
    }

    public double DistanceAtAngle(ArrayList<Point> points, int T, int num, double theta){
        ArrayList<Point> newPoints = RotateBy(points, theta);

        double d = PathDistance(newPoints, T, num);
        newPoints = RotateBy(points, -theta);
        return d;
    }

    public double PathDistance(ArrayList<Point> a, int T, int num){
        double d = 0;
        for(int i = 0; i < a.size(); i++){
            d = d + Distance(a.get(i), candidates.get(T).points.get(num).get(i));
        }
        return d/a.size();
    }

    public void buttonPageOnChange(View v) {
        switch (page) {
            case VIEW_SEQUENTIAL:
                onChangeView(VIEW_GESTURE);
                view = (TextView)findViewById(R.id.textView);
                changeState = (Button)findViewById(R.id.button3);
                save = (Button)findViewById(R.id.button4);
                edit = (EditText)findViewById(R.id.editText2);
                gestureName = (TextView)findViewById(R.id.gesture_name);
                startTest = (Button)findViewById(R.id.start_test);
                view.setVisibility(View.GONE);
                gestureName.setVisibility(View.GONE);
                startTest.setVisibility(View.GONE);
                break;
            case VIEW_GESTURE:
                onChangeView(VIEW_SEQUENTIAL);
                view_s = (TextView)findViewById(R.id.textView_s);
//                changeState_s = (Button)findViewById(R.id.button1_s);
                save_s = (Button)findViewById(R.id.button2_s);
                edit_s = (EditText)findViewById(R.id.editText_s);
                sequentialName = (TextView)findViewById(R.id.sequential_name);
                startTest_s = (Button)findViewById(R.id.start_test_s);
                view_s.setVisibility(View.GONE);
                sequentialName.setVisibility(View.GONE);
                startTest_s.setVisibility(View.GONE);
                break;
        }
    }

    // 切换模式
    public void StateOnChange(View v){
        switch (state) {
            case GESTURE_SETUP:
                state = GESTURE_TEST;
                changeState.setText("TEST");
                edit.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
                gestureName.setVisibility(View.VISIBLE);
                startTest.setVisibility(View.VISIBLE);
                break;
            case GESTURE_TEST:
                state = GESTURE_SETUP;
                changeState.setText("SETUP");
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                gestureName.setVisibility(View.GONE);
                startTest.setVisibility(View.GONE);
                break;
        }
    }

    public void StateOnChange_s(){
        switch (state_s) {
            case SEQUENTIAL_TEST:
//                state_s = SEQUENTIAL_TEST;
//                changeState_s.setText("TEST");
                edit_s.setVisibility(View.GONE);
                save_s.setVisibility(View.GONE);
                view_s.setVisibility(View.VISIBLE);
                sequentialName.setVisibility(View.VISIBLE);
                startTest_s.setVisibility(View.VISIBLE);
                break;
            case SEQUENTIAL_SETUP:
//                state_s = SEQUENTIAL_SETUP;
//                changeState_s.setText("SETUP");
                edit_s.setVisibility(View.VISIBLE);
                save_s.setVisibility(View.VISIBLE);
                view_s.setVisibility(View.GONE);
                sequentialName.setVisibility(View.GONE);
                startTest_s.setVisibility(View.GONE);
                break;
            case SEQUENTIAL_LEARN:
                edit_s.setVisibility(View.GONE);
                save_s.setVisibility(View.GONE);
                view_s.setVisibility(View.GONE);
                sequentialName.setVisibility(View.GONE);
                startTest_s.setVisibility(View.GONE);

                qtaps.add(new QTap(1,1,true));
                qtaps.add(new QTap(1,1,false));
                qtaps.add(new QTap(1,1,true));
                qtaps.add(new QTap(1,1,false));
                qtaps.add(new QTap(2,2,true));
                qtaps.add(new QTap(3,3,true));

                qtaps.add(new QTap(2,5,false));
                qtaps.add(new QTap(3,6,false));

                drawFlash.drawTapFlash(qtaps);
        }
    }

    // 保存手势
    public void SaveClicked(View v){
        String name = edit.getText().toString();
        if(name.equals("")){
            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
            nameNull.setTitle("提示" ) ;
            nameNull.setMessage("手势名称不可以为空" ) ;
            nameNull.setPositiveButton("是" ,  null );
            nameNull.show();
        }
        else{
            if(checkDupli(name, candidates)){
                ArrayList<Point> tmp = new ArrayList();
                for(int i = 0; i < qmoves.size(); i++){
                    Point p = new Point(qmoves.get(i).x, qmoves.get(i).y);
                    tmp.add(p);
                }
                Candidate myCandi = new Candidate(name, tmp);
                edit.setText("");
                candidates.add(myCandi);
                qmoves.clear();
                drawView2.drawShape(qmoves);
            }
            else{
//                AlertDialog.Builder nameDupli  = new AlertDialog.Builder(MainActivity.this);
//                nameDupli.setTitle("提示" ) ;
//                nameDupli.setMessage("手势名称不可以重复" ) ;
//                nameDupli.setPositiveButton("是" ,  null );
//                nameDupli.show();
//                edit.setText("");
                for(int i = 0; i < candidates.size(); i++){
                    if(candidates.get(i).name.equals(name)){
                        ArrayList<Point> tmp = new ArrayList();
                        for(int j = 0; j < qmoves.size(); j++){
                            Point p = new Point(qmoves.get(j).x, qmoves.get(j).y);
                            tmp.add(p);
                        }
                        candidates.get(i).points.add(tmp);
                        edit.setText("");
                        qmoves.clear();
                        drawView2.drawShape(qmoves);
                        break;
                    }
                }
            }
        }
    }

    public void TestStart(View v){
        int caseSize = candidates.size();
        if(caseSize == 0){
            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
            nameNull.setTitle("错误" ) ;
            nameNull.setMessage("预设的手势为空") ;
            nameNull.setPositiveButton("ok" ,  null );
            nameNull.show();
        }
        else{
            testCase = new String[testTime];
            Random r = new Random();
            for(int i = 0; i < testTime; i++){
                testCase[i] = candidates.get(r.nextInt(caseSize)).name;
            }
            startTime = System.currentTimeMillis();
            gestureName.setText("请绘制 "+testCase[0]);
            isTesting = true;
            startTest.setEnabled(false);
            for(int i = 0; i < testTime; i++){
                System.out.println("testCase["+i+"]:"+testCase[i]);
            }
        }
    }

    public void TestStart_s(View v){
        int caseSize = candidates_s.size();
        if(caseSize == 0){
            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
            nameNull.setTitle("错误" ) ;
            nameNull.setMessage("预设的手势为空") ;
            nameNull.setPositiveButton("ok" ,  null );
            nameNull.show();
        }
        else{
            testCase_s = new String[testTime_s];
            Random r = new Random();
            for(int i = 0; i < testTime_s; i++){
                testCase_s[i] = candidates_s.get(r.nextInt(caseSize)).name;
            }
            startTime_s = System.currentTimeMillis();
            sequentialName.setText("请敲击 "+testCase_s[0]);
            isTesting_s = true;
            startTest_s.setEnabled(false);
            for(int i = 0; i < testTime_s; i++){
                System.out.println("testCase_s["+i+"]:"+testCase_s[i]);
            }
        }
    }

    public ArrayList<QTouch> clearDiffer(ArrayList<QTouch> qtouches){
//        for(int i = 0; i < qtouchs_copy.size(); i++){
//            System.out.println("heeeere "+ "clearDiffer    before qtouchs_copy" + i +":(order,index)");
//            System.out.println(qtouchs_copy.get(i).order+","+qtouchs_copy.get(i).index);
//            System.out.println("(t0,t1)" + qtouchs_copy.get(i).t0+","+qtouchs_copy.get(i).t1);
//        }
        Collections.sort(qtouchs_copy, new Comparator<QTouch>() {
            public int compare(QTouch lhs, QTouch rhs) {
                                 if ( lhs.t0 > rhs.t0 ) {
                                        return 1;
                                   } else {
                                       return -1;
                                  }
                            }
         });
//        for(int i = 0; i < qtouchs_copy.size(); i++){
//            System.out.println("heeeere "+ "clearDiffer    sort qtouchs_copy" + i +":(order,index)");
//            System.out.println(qtouchs_copy.get(i).order+","+qtouchs_copy.get(i).index);
//            System.out.println("(t0,t1)" + qtouchs_copy.get(i).t0+","+qtouchs_copy.get(i).t1);
//        }
        for(int i = 0; i < qtouches.size()-1; i++){
            if(AbsoluteVal(qtouches.get(i).t0,qtouches.get(i+1).t0,50)) {
                //System.out.println("heeeere "+ "change order here1");
                if(qtouches.get(i).order < qtouches.get(i+1).order){
                    qtouches.get(i+1).order = qtouches.get(i).order;
                }
                if(qtouches.get(i).order > qtouches.get(i+1).order){
                    qtouches.get(i).order = qtouches.get(i+1).order;
                }
            }
        }
        for(int i = qtouches.size()-1 ; i > 0; i--){
            if(AbsoluteVal(qtouches.get(i).t0,qtouches.get(i-1).t0,50)){
                //System.out.println("heeeere "+ "change order here2");
                if(qtouches.get(i).order < qtouches.get(i-1).order){
                    qtouches.get(i-1).order = qtouches.get(i).order;
                }
                if(qtouches.get(i).order > qtouches.get(i-1).order){
                    qtouches.get(i).order = qtouches.get(i-1).order;
                }
            }
        }
        Collections.sort(qtouchs_copy, new Comparator<QTouch>() {
            public int compare(QTouch lhs, QTouch rhs) {
                if ( lhs.index > rhs.index ) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
//        for(int i = 0; i < qtouchs_copy.size(); i++){
//            System.out.println("heeeere "+ "clearDiffer    after qtouchs_copy" + i +":(order,index)");
//            System.out.println(qtouchs_copy.get(i).order+","+qtouchs_copy.get(i).index);
//            System.out.println("(t0,t1)" + qtouchs_copy.get(i).t0+","+qtouchs_copy.get(i).t1);
//        }
        return qtouches;
    }

    public void SaveClicked_s(View v){
        String name = edit_s.getText().toString();
        if(name.equals("")){
            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
            nameNull.setTitle("提示" ) ;
            nameNull.setMessage("手势名称不可以为空" ) ;
            nameNull.setPositiveButton("是" ,  null );
            nameNull.show();
        }
        else if(name.equals("not found")){
            AlertDialog.Builder nameNull  = new AlertDialog.Builder(MainActivity.this);
            nameNull.setTitle("提示" ) ;
            nameNull.setMessage("手势名称非法" ) ;
            nameNull.setPositiveButton("是" ,  null );
            nameNull.show();
        }
        else{
            if(checkDupli_s(name, candidates_s)){
                ArrayList<QTouch> tmp = new ArrayList();
                for(int i = 0; i < qtouchs_copy.size(); i++){
//                    System.out.println("add qtouch into tmp round " + i);
                    qtouchs_copy = clearDiffer(qtouchs_copy);
                    QTouch p = new QTouch(qtouchs_copy.get(i).x, qtouchs_copy.get(i).y, qtouchs_copy.get(i).t0, qtouchs_copy.get(i).t1);
                    p.l = qtouchs_copy.get(i).l;
                    p.r = qtouchs_copy.get(i).r;
                    p.order = qtouchs_copy.get(i).order;
                    p.index = qtouchs_copy.get(i).index;
                    tmp.add(p);
                }
                Candidate_s myCandi = new Candidate_s(name, tmp);
                edit_s.setText("");
                candidates_s.add(myCandi);
            }
            else{
//                AlertDialog.Builder nameDupli  = new AlertDialog.Builder(MainActivity.this);
//                nameDupli.setTitle("提示" ) ;
//                nameDupli.setMessage("手势名称不可以重复" ) ;
//                nameDupli.setPositiveButton("是" ,  null );
//                nameDupli.show();
//                edit_s.setText("");
                for(int i = 0; i < candidates_s.size(); i++){
                    if(candidates_s.get(i).name.equals(name)){
                        ArrayList<QTouch> tmp = new ArrayList();
                        for(int j = 0; j < qtouchs_copy.size(); j++){
                            QTouch p = new QTouch(qtouchs_copy.get(j).x, qtouchs_copy.get(j).y, qtouchs_copy.get(j).t0, qtouchs_copy.get(j).t1);
                            p.l = qtouchs_copy.get(j).l;
                            p.r = qtouchs_copy.get(j).r;
                            p.order = qtouchs_copy.get(j).order;
                            p.index = qtouchs_copy.get(j).index;
                            tmp.add(p);
                        }
                        candidates_s.get(i).touchs.add(tmp);
                        edit_s.setText("");
                    }
                }
            }
        }
    }

    public boolean checkDupli(String a, ArrayList<Candidate> b){
        for(int i = 0; i < b.size(); i++){
            if(b.get(i).name.equals(a)){
                return false;
            }
        }
        return true;
    }

    public boolean checkDupli_s(String a, ArrayList<Candidate_s> b){
        for(int i = 0; i < b.size(); i++){
            if(b.get(i).name.equals(a)){
                return false;
            }
        }
        return true;
    }
}
