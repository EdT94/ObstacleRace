package com.example.obstaclerace;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ImageView main_IMG_car;
    private ImageView main_IMG_leftRock;
    private ImageView main_IMG_middleRock;
    private ImageView main_IMG_rightRock;
    private ImageView main_IMG_3lives;
    private ImageView main_IMG_2lives;
    private ImageView main_IMG_1lives;
    private Button main_BTN_right;
    private Button main_BTN_left;
    private Handler handler = new Handler();
    private float leftRockYPosition;
    private float middleRockYPosition;
    private float rightRockYPosition;
    private short numOfLives = 3;
    private ObjectAnimator middleRockAnimation;
    private ObjectAnimator leftRockAnimation;
    private ObjectAnimator rightRockAnimation;
    private MediaPlayer turnSound ;
    private MediaPlayer carSound ;
    private MediaPlayer crashSound ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        main_IMG_car = findViewById(R.id.main_IMG_car);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_IMG_leftRock = findViewById(R.id.main_IMG_leftRock);
        main_IMG_middleRock = findViewById(R.id.main_IMG_middleRock);
        main_IMG_rightRock = findViewById(R.id.main_IMG_rightRock);
        main_IMG_3lives = findViewById(R.id.main_IMG_3lives);
        main_IMG_2lives = findViewById(R.id.main_IMG_2lives);
        main_IMG_1lives = findViewById(R.id.main_IMG_1lives);
        turnSound = MediaPlayer.create(MainActivity.this, R.raw.car_tires);
        carSound = MediaPlayer.create(MainActivity.this, R.raw.car_accelerating);
        crashSound = MediaPlayer.create(MainActivity.this, R.raw.crash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        carSound.setLooping(true);
        carSound.start();

        turnRight(main_BTN_right);
        turnLeft(main_BTN_left);
        leftRock();
        middleRock();
        rightRock();
    }

    private void toast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void rightRock() {
        main_IMG_rightRock.setVisibility(View.INVISIBLE);
        rightRockAnimation = ObjectAnimator.ofFloat(main_IMG_rightRock, "y", 2500);
        handler.postDelayed(new Runnable() {
            public void run() {
                rightRockAnimation.setDuration(9000);
                rightRockAnimation.setRepeatCount(Animation.INFINITE);
                main_IMG_rightRock.setVisibility(View.VISIBLE);
                rightRockAnimation.start();
            }
        }, 3500);


        rightRockAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightRockYPosition = (Float) animation.getAnimatedValue();
                checkCrashWithRock(rightRockYPosition, main_IMG_rightRock, rightRockAnimation);
            }
        });
    }

    private void middleRock() {
        main_IMG_middleRock.setVisibility(View.INVISIBLE);
        middleRockAnimation = ObjectAnimator.ofFloat(main_IMG_middleRock, "y", 2500);
        handler.postDelayed(new Runnable() {
            public void run() {
                middleRockAnimation.setDuration(9000);
                middleRockAnimation.setRepeatCount(Animation.INFINITE);
                main_IMG_middleRock.setVisibility(View.VISIBLE);
                middleRockAnimation.start();
            }
        }, 6500);


        middleRockAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                middleRockYPosition = (Float) animation.getAnimatedValue();
                checkCrashWithRock(middleRockYPosition, main_IMG_middleRock, middleRockAnimation);
            }
        });
    }

    private void leftRock() {
        main_IMG_leftRock.setVisibility(View.INVISIBLE);
        leftRockAnimation = ObjectAnimator.ofFloat(main_IMG_leftRock, "y", 2500);
        handler.postDelayed(new Runnable() {
            public void run() {
                leftRockAnimation.setDuration(9000);
                leftRockAnimation.setRepeatCount(Animation.INFINITE);
                main_IMG_leftRock.setVisibility(View.VISIBLE);
                leftRockAnimation.start();
            }
        }, 1000);

        leftRockAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftRockYPosition = (Float) animation.getAnimatedValue();
                checkCrashWithRock(leftRockYPosition, main_IMG_leftRock, leftRockAnimation);
            }
        });
    }

    private void vibrate() {
        // get the VIBRATOR_SERVICE system service
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final VibrationEffect vibrationEffect1;

        // this effect creates the vibration of default amplitude for 1000ms(1 sec)
        vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.cancel();
        vibrator.vibrate(vibrationEffect1);
    }


    private void checkCrashWithRock(Float objectYPosition, ImageView rock, ObjectAnimator animation) {
        if ((main_IMG_car.getY() >= objectYPosition - 2 && main_IMG_car.getY() <= objectYPosition + 2) && (rock.getX() == main_IMG_car.getX())) {
            crashSound.start();
            vibrate();
            if (numOfLives == 3) {
                toast("Crash! 2 lives left");
                numOfLives--;
                main_IMG_3lives.setVisibility(View.INVISIBLE);
            } else if (numOfLives == 2) {
                toast("Crash! 1 life left");
                numOfLives--;
                main_IMG_2lives.setVisibility((View.INVISIBLE));
            } else if (numOfLives == 1) {
                toast("Crash! no more lives left");
                numOfLives--;
                main_IMG_1lives.setVisibility((View.INVISIBLE));
            } else {
                toast("Crash! let's start again");
                numOfLives = 3;
                main_IMG_3lives.setVisibility(View.VISIBLE);
                main_IMG_2lives.setVisibility((View.VISIBLE));
                main_IMG_1lives.setVisibility(View.VISIBLE);
            }

        }
        carSound.start();
    }

    private void turnLeft(Button main_btn_left) {
        main_BTN_left.setOnClickListener(v -> {
            if (main_IMG_car.getX() != 70) {
                turnSound.start();
                if (main_IMG_car.getX() == 578) {
                    main_IMG_car.setRotation(-45);
                    main_IMG_car.setX(70);
                    myWait();
                } else {
                    main_IMG_car.setRotation(-45);
                    main_IMG_car.setX(578);
                    myWait();
                }
            }
        });
    }

    private void turnRight(Button main_btn_right) {
        main_BTN_right.setOnClickListener(v -> {
            if (main_IMG_car.getX() != 1085) {
                turnSound.start();
                if (main_IMG_car.getX() == 578) {
                    main_IMG_car.setRotation(45);
                    main_IMG_car.setX(1085);
                    myWait();
                } else {
                    main_IMG_car.setRotation(45);
                    main_IMG_car.setX(578);
                    myWait();
                }
            }
        });
    }

    //wait 80 mls before setting the car rotation to normal
    private void myWait() {
        handler.postDelayed(new Runnable() {
            public void run() {
                main_IMG_car.setRotation(0);
            }
        }, 80);
    }



}