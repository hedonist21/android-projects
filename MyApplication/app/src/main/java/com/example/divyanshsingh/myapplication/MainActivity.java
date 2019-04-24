package com.example.divyanshsingh.myapplication;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private Button mButton1;
    // private Button mButton2;
    private TextView mTextiew1;
    private TextView mTextiew2;
    Thread t1;
    Thread t2 ;
    int x=0;
    GameStart1 g1 = new GameStart1();
    GameStart2 g2 = new GameStart2();
    static int[][] Table1 = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };
    static int[][] Table2 = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        mButton1 = (Button) findViewById(R.id.button1);
        mTextiew1 = (TextView) findViewById(R.id.textview1);
        mTextiew2 = (TextView) findViewById(R.id.textview2);
        mTextiew1.setMovementMethod(new ScrollingMovementMethod());
        mTextiew2.setMovementMethod(new ScrollingMovementMethod());
        super.onCreate(savedInstanceState);

        //start game
        mButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (x == 0) {
                    mTextiew1.setTextColor(Color.parseColor("#eb8c00"));
                    mTextiew2.setTextColor(Color.parseColor("#eb8c00"));
                    x++;
                    Toast.makeText(MainActivity.this, "Game Begins", Toast.LENGTH_SHORT).show();
                    t1 = new Thread(g1);
                    t2 = new Thread(g2);

                    t1.start();
                    t2.start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted!");
                    }

                    Message msg = g1.getmHandler1().obtainMessage(1);
                    g1.getmHandler1().sendMessage(msg);
                    msg = g2.mHandler2.obtainMessage(1);
                    g2.mHandler2.sendMessage(msg);



                }
                else{mTextiew1.setTextColor(Color.parseColor("#eb8c00"));
                    mTextiew2.setTextColor(Color.parseColor("#eb8c00"));
                    Message msg = g1.mHandler1.obtainMessage(4);
                    g1.mHandler1.sendMessage(msg);
                    msg = g2.mHandler2.obtainMessage(4);
                    g2.mHandler2.sendMessage(msg);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted!");
                    }
                    new Thread(g1).start();
                    new Thread(g2).start();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted!");
                    }
                    msg = g1.getmHandler1().obtainMessage(1);
                    g1.getmHandler1().sendMessage(msg);
                    msg = g2.mHandler2.obtainMessage(1);
                    g2.mHandler2.sendMessage(msg);

                }
            }});

    }

    //thread 1 class
    public class GameStart1 implements Runnable {
        int secretno;
        int status = 0;
        int status1=0;
        int times = 0;
        int n;
        int[] result;
        Handler mHandler1;
        int len;
        Message msg1;


        public void run() {
            Looper.prepare();

            mHandler1 = new Handler() {
                public void handleMessage(Message msg) {
                    int what = msg.what;

                    switch (what) {
                        //generate number if case 1
                        case 1:
                            secretno = GenerateNumber();
                            times=0;
                            Table1 = new int[][]{
                                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                            };
//
                            mHandler.post(new Runnable() {
                                public void run() {mTextiew1.setText("Thread 1\n Secret No:" + String.valueOf(secretno));}});

                            break;

                        //guess number if case 2
                        case 2:
                            status1=1;
                            if(times<20) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                n = GuessNumber1(times);
                                times++;
                                mHandler.post(new Runnable() {
                                    public void run() {
                                        mTextiew1.append("\n  " + String.valueOf(n));
                                    }
                                });

                                msg = g2.getmHandler2().obtainMessage(2);
                                g2.getmHandler2().sendMessage(msg);
                            }
                            break;

                        //evaluate opponents number if case 3
                        case 3:
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            status=1;
                            Log.i("Should show First", Thread.currentThread().getName());

                            result = Evaluate2(msg.arg1, secretno);

                            if(result[0] != 0){
                                len = String.valueOf(result[0]).length();
                                mHandler.post(new Runnable() {
                                    public void run() {mTextiew2.append("\n  " + String.valueOf(len) + "Position   " + "At index " + String.valueOf(result[0])+ "  " + String.valueOf((result[1]) + "Similar"));}});}
                            else {
                                len = 0;
                                mHandler.post(new Runnable() {
                                    public void run() {mTextiew2.append("\n  " + String.valueOf(len) + "Position   " +  String.valueOf((result[1]) + "Similar"));}});
                            }
                            msg=g2.getmHandler2().obtainMessage(3);
                            msg.arg1=n;
                            g2.getmHandler2().sendMessage(msg);

                            break;

                        //stop if stop button pressed for case 4
                        case 4:
                            getLooper().quit();
                            break;
                    }

                }
            };
            status=0;
            status1=0;

            Looper.loop();


        }

        public Handler getmHandler1() {
            return mHandler1;
        }

        public int getStatus() {
            return status;
        }

        public int getN() {
            return n;
        }

        public int getSecretno() {
            return secretno;
        }

        public int getStatus1() {
            return status1;
        }

        public int[] getResult() {
            return result;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

    //thread2 class
    public class GameStart2 implements Runnable {
        int status = 0;
        int secretno;
        int times = 0;
        int n;
        int[] result;
        int len;
        int status1=0;

        Message msg1;
        public Handler mHandler2 ;

        public void run() {
            Looper.prepare();
            mHandler2= new Handler() {
                public void handleMessage(Message msg) {
                    int what = msg.what;
                    switch (what) {
                        //generate number if case 1
                        case 1:
                            Table1 = new int[][]{
                                    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                            };
                            times=0;
                            secretno = GenerateNumber();
                            mHandler.post(new Runnable() {
                                public void run() {mTextiew2.setText("Thread 2\n Secret No:" + String.valueOf(secretno));}});
                            msg=g1.getmHandler1().obtainMessage(2);
                            g1.getmHandler1().sendMessage(msg);
                            break;

                        //guess number if case 2
                        case 2:
                            status1=1;
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            n= GuessNumber2(times);
                            times++;
                            mHandler.post(new Runnable() {
                                public void run() {mTextiew2.append("\n"+ String.valueOf(n));}});
                            msg=g1.getmHandler1().obtainMessage(3);
                            msg.arg1=n;
                            g1.getmHandler1().sendMessage(msg);
                            break;

                        //evaluate opponents number if case 3
                        case 3:
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            status=1;
                            Log.i("Should show second", Thread.currentThread().getName());

                            result = Evaluate1(msg.arg1, secretno);
                            if(result[0] != 0){
                                len = String.valueOf(result[0]).length();
                                mHandler.post(new Runnable() {
                                    public void run() {mTextiew1.append("\n  " + String.valueOf(len) + "Position   " + "At index " + String.valueOf(result[0])+ "  " + String.valueOf((result[1]) + "Similar"));}});}
                            else {
                                len = 0;
                                mHandler.post(new Runnable() {
                                    public void run() {mTextiew1.append("\n  " + String.valueOf(len) + "Position   " +  String.valueOf((result[1]) + "Similar"));}});
                            }

                            if(len==4 ){

                                mHandler.post(new Runnable() {
                                    public void run() {mTextiew1.setTextColor(Color.parseColor("#591647"));

                                        mTextiew1.append("\n  Player 1 wins");
                                        mTextiew2.append("\n  Player 1 wins");
                                    }

                                });


                            }
                            else if(g1.len==4){
                                mHandler.post(new Runnable() {
                                    public void run()
                                    {
                                        mTextiew2.setTextColor(Color.parseColor("#591647"));
                                        mTextiew1.append("\n  Player 2 wins");
                                        mTextiew2.append("\n  Player 2 wins");
                                        getLooper().quit();


                                    }});}
                            else{
                                msg=g1.getmHandler1().obtainMessage(2);
                                g1.getmHandler1().sendMessage(msg);}
                            break;

                        //stop if stop button pressed for case 4
                        case 4:
                            getLooper().quit();

                            break;
                    }
                }
            };
            status=0;
            status1=0;
            Looper.loop();
        }


        public int getTimes() {
            return times;
        }

        public Handler getmHandler2() {
            return mHandler2;
        }

        public int getStatus() {
            return status;
        }

        public int getN() {
            return n;
        }



        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus1() {
            return status1;
        }
    }


    //Generating a random number
    int GenerateNumber() {

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        String result = "";
        for (int i = 0; i < 4; i++) {
            if (i == 0 && numbers.get(i) == 0)
                result += numbers.get(4).toString();
            else
                result += numbers.get(i).toString();
        }
        return Integer.parseInt(result);
    }


    //Guessing technique by first thread
    int GuessNumber1(int times) {
        int no;


        List<Integer> result = new ArrayList<>();
        int x = 0;
        if (times == 0)
            no = 0000;
        else if (times == 1)
            no = 1111;
        else if (times == 2)
            no = 2222;
        else if (times == 3)
            no = 3333;
        else if (times == 4)
            no = 4444;
        else if (times == 5)
            no = 5555;
        else if (times == 6)
            no = 6666;
        else if (times == 7)
            no = 7777;
        else if (times == 8)
            no = 8888;
        else if (times == 9)
            no = 9999;
        else {
            for (int i = 0; i < 10; i++) {
                if (Table1[1][i] == 1)
                    result.add(Table1[0][i]);

            }
            Collections.shuffle(result);
            String result1 = "";
            for (int i = 0; i < 4; i++) {
                result1 += result.get(i).toString();
            }
            no = Integer.parseInt(result1);

        }
        return no;

    }

    //Guessing number by second thread
    int GuessNumber2(int times) {
        int no;
        List<Integer> result = new ArrayList<>();
        int x = 0;
        if (times == 0)
            no = 1100;
        else if (times == 1)
            no = 3322;
        else if (times == 2)
            no = 5544;
        else if (times == 3)
            no = 7766;
        else if (times == 4)
            no = 9988;
        else {
            for (int i = 0; i < 10; i++) {
                if (Table2[1][i] == 1)
                    result.add(Table2[0][i]);

            }

            Collections.shuffle(result);
            String result1 = "";
            for (int i = 0; i < 4; i++) {
                result1 += result.get(i).toString();// result1 += result.get(i).toString();
            }
            no = Integer.parseInt(result1);

        }
        return no;

    }


    //Evaluating a number
    int[] Evaluate1(int guess, int sec) {
        int[] result = {0, 0};

        int len = 4;
        //split guess to array
        int[] guess1 = new int[len];
        for (int index = 0; index < len; index++) {
            guess1[index] = guess % 10;
            guess /= 10;

        }
        len = 4;
        //split guess to array
        int[] sec1 = new int[len];
        for (int index = 0; index < len; index++) {
            sec1[index] = sec % 10;
            sec /= 10;
        }
        for(int i=0;i<2;i++)
        {
            int temp = guess1[i];
            guess1[i] = guess1[3-i];
            guess1[3-i]= temp;

            temp = sec1[i];
            sec1[i] = sec1[3-i];
            sec1[3-i]= temp;
        }
//    int arr[];
        for (int i = 0; i < len; i++) {
            if (guess1[i] == sec1[i]) {
                //result[0] = result[0] + 1;
                result[0] = result[0]*10+i+1;
                sec1[i] = 11;
                //  arr[i] =1;
                Table1[1][guess1[i]] = 1;
            }

        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (guess1[i] == sec1[j])
                    result[1] = result[1] + 1;

            }
        }
        return result;
    }

    //Evaluating a number
    int[] Evaluate2(int guess, int sec) {
        int[] result = {0, 0};

        int len = 4;
        //split guess to array
        int[] guess1 = new int[len];
        for (int index = 0; index < len; index++) {
            guess1[index] = guess % 10;
            guess /= 10;
        }
        len = 4;
        //split guess to array
        int[] sec1 = new int[len];
        for (int index = 0; index < len; index++) {
            sec1[index] = sec % 10;
            sec /= 10;
        }

        for(int i=0;i<2;i++)
        {
            int temp = guess1[i];
            guess1[i] = guess1[3-i];
            guess1[3-i]= temp;

            temp = sec1[i];
            sec1[i] = sec1[3-i];
            sec1[3-i]= temp;
        }

        for (int i = 0; i < len; i++) {
            if (guess1[i] == sec1[i]) {
                result[0] = result[0]*10+i+1;
                sec1[i] = 11;
                Table2[1][guess1[i]] = 1;
            }

        }

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (guess1[j] == sec1[i]){
                    result[1] = result[1] + 1;
                    sec1[i]=11;
                    Table2[1][guess1[j]]=1;}
            }
        }
        return result;
    }


    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what;

            switch (what) {
                case 1:
                    Log.i("111111111111","hieeeee");
                    Log.i(" Main thread",Thread.currentThread().getName());
                    mTextiew1.append("\n Secret No:" + String.valueOf(msg.arg1));

                    break;
                case 2:
                    Log.i("222222222","hieeeee");
                    mTextiew2.append("\n Secret No:" + String.valueOf(msg.arg1));

                    break;
                case 3:
                    Log.i("111111111111","hieeeee");
                    mTextiew1.append("\n  " + String.valueOf(msg.arg1));

                    break;
                case 4:
                    Log.i("222222222","hieeeee");
                    mTextiew2.append("\n  " + String.valueOf(msg.arg1));

                    break;
                case 5:
                    Log.i("111111111","hieeeee");
                    int len;
                    if(msg.arg1 != 0){
                        len = String.valueOf(msg.arg1).length();
                        mTextiew2.append("\n  " + String.valueOf(len) + "Position " + "At index " +String.valueOf(msg.arg1)+ "   " +String.valueOf((msg.arg2) + "Similar"));}
                    else{
                        len=0;
                        mTextiew2.append("\n  " + String.valueOf(len) + "Position " +"   " +String.valueOf((msg.arg2) + "Similar"));}


                    break;
                case 6:
                    Log.i("22222222222","hieeeee");
                    if(msg.arg1 != 0){
                        len = String.valueOf(msg.arg1).length();
                        mTextiew1.append("\n  " + String.valueOf(len) + "Position " + "At index " +String.valueOf(msg.arg1)+ "   " +String.valueOf((msg.arg2) + "Similar"));}
                    else{
                        len=0;
                        mTextiew1.append("\n  " + String.valueOf(len) + "Position " +"   " +String.valueOf((msg.arg2) + "Similar"));}

                    break;

            }
        }
    };


}