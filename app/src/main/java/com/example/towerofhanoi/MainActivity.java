package com.example.towerofhanoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutleft;
    LinearLayout layoutMiddle;
    LinearLayout layoutRight;
    ImageView ringOne;
    ImageView ringTwo;
    ImageView ringThree;

    int moveCounter;
    TextView move_text;
    boolean gameStarted = false;

    int timerCounter = 0;
    Handler timerHandler;
    Timer timer;
    TextView timetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timetText = findViewById(R.id.seconds_text);
        move_text = findViewById(R.id.moves_text);

        getViews();
        addingTouchListeners();
        buttonAction();

    }

    public void getViews() {
        // finding views
        layoutleft = findViewById(R.id.view_one);
        layoutMiddle = findViewById(R.id.view_two);
        layoutRight = findViewById(R.id.view_three);

        // finding img
        ringOne = findViewById(R.id.ring_one);
        ringTwo = findViewById(R.id.ring_three);
        ringThree = findViewById(R.id.ring_two);
    }

    public void addingTouchListeners() {
        // adding listeners to views
        layoutleft.setOnDragListener(new MyDragListener());
        layoutMiddle.setOnDragListener(new MyDragListener());
        layoutRight.setOnDragListener(new MyDragListener());

        // adding listeners to img
        ringOne.setOnTouchListener(new MyTouchListener());
        ringTwo.setOnTouchListener(new MyTouchListener());
        ringThree.setOnTouchListener(new MyTouchListener());
    }

    private final class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LinearLayout owner = (LinearLayout) v.getParent();

            View top = owner.getChildAt(0);

            if(v == top || owner.getChildCount() == 1) {

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {



        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view, 0);
                    view.setVisibility(View.VISIBLE);
                    count();
                    checkForWin();

                    break;

                    default:
                        break;
            }
            return true;
        }
    }


    private Runnable doUpdateTime = () -> {
        timetText.setText(timerCounter);
        timerCounter++;
    };

    public void timer() {
        timer = new Timer();

        try {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timerHandler.post(doUpdateTime);
                }
            }, 0, 1000);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        }
    }

    public void buttonAction() {
        Button startStop = findViewById(R.id.button);

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gameStarted) {
                    resetBoard();
                    gameStarted = false;
                    startStop.setText("start");
                } else {
                    gameStarted = true;
                    startStop.setText("restart");
                }

            }
        });
    }

    public void count() {
        moveCounter++;
        String move = moveCounter + "";
        move_text.setText(move);
    }

    public void checkForWin() {
        if(layoutRight.getChildAt(0) == ringOne && layoutRight.getChildAt(1) == ringTwo
                && layoutRight.getChildAt(2) == ringThree) {
            Toast toast = Toast.makeText(getApplicationContext(), "Gratulerer, du klarte det!", Toast.LENGTH_LONG);
            toast.show();

        }
    }

    public void resetBoard() {
        layoutleft.removeAllViews();
        layoutMiddle.removeAllViews();
        layoutRight.removeAllViews();

        layoutleft.addView(ringOne);
        layoutleft.addView(ringTwo);
        layoutleft.addView(ringThree);


        move_text.setText("0");
        moveCounter = 0;

    }
}