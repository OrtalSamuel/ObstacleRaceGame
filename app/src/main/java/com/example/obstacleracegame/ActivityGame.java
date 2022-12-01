package com.example.obstacleracegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityGame extends AppCompatActivity {
    AppCompatImageView game_IMG_background;
    private FloatingActionButton game_FAB_left;
    private FloatingActionButton game_FAB_right;
    private ShapeableImageView[] game_IMG_hearts;
    private ShapeableImageView[][] game_IMG_stones;
    private ShapeableImageView[] game_IMG_cars;
    private Random r;
    private int carLocation;
    private int lives;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViews();
        initBackground();

        r=new Random();
        carLocation=1;
        lives=3;

        refreshUI();

        game_FAB_left.setOnClickListener(view -> {
            clicked(true);
        });
        game_FAB_right.setOnClickListener(view -> {
            clicked(false);
        });
    }

    private void refreshUI() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkCrash();
                        updateHeartsUi();
                        moveTheStones();
                    }
                });
            }
        }, 2000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshUI();
    }

    private void checkCrash() {
        if(game_IMG_stones[3][carLocation].getVisibility()==View.VISIBLE){
            if(lives>0){
                lives--;
            }
            Toast.makeText(this, "Crash", Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private void moveTheStones() {
        for(int j=0;j<3;j++) {
            for (int i = 3; i > 0; i--) {
                game_IMG_stones[i][j].setVisibility(game_IMG_stones[i - 1][j].getVisibility());
            }
        }
        for(int i=0;i<3;i++){
            game_IMG_stones[0][i].setVisibility(View.INVISIBLE);
        }
        game_IMG_stones[0][r.nextInt(3)].setVisibility(View.VISIBLE);

    }

    private void clicked(boolean isLeft) {
        game_IMG_cars[carLocation].setVisibility(View.INVISIBLE);
        if (isLeft && carLocation>0) {
            carLocation--;

        } else if(!isLeft && carLocation<2) {
            carLocation++;
        }
        game_IMG_cars[carLocation].setVisibility(View.VISIBLE);
    }

    private boolean crash() {
        return true;
    }

    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.img_road)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(game_IMG_background);
    }
    private void findViews() {
        game_IMG_background = findViewById(R.id.game_IMG_background);
        game_FAB_left = findViewById(R.id.game_FAB_left);
        game_FAB_right = findViewById(R.id.game_FAB_right);
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };
        game_IMG_stones = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_stone0), findViewById(R.id.game_IMG_stone1), findViewById(R.id.game_IMG_stone2)},
                {findViewById(R.id.game_IMG_stone3), findViewById(R.id.game_IMG_stone4), findViewById(R.id.game_IMG_stone5)},
                {findViewById(R.id.game_IMG_stone6), findViewById(R.id.game_IMG_stone7), findViewById(R.id.game_IMG_stone8)},
                {findViewById(R.id.game_IMG_stone9), findViewById(R.id.game_IMG_stone10), findViewById(R.id.game_IMG_stone11)},
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                game_IMG_stones[i][j].setVisibility(View.INVISIBLE);
            }
        }
        game_IMG_cars = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_car1),
                findViewById(R.id.game_IMG_car2),
                findViewById(R.id.game_IMG_car3),

        };
        game_IMG_cars[0].setVisibility(View.INVISIBLE);
        game_IMG_cars[2].setVisibility(View.INVISIBLE);

    }
    private void updateHeartsUi() {
        if(lives<game_IMG_hearts.length){
            game_IMG_hearts[lives].setVisibility(View.INVISIBLE);
        }
    }
}