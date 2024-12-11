package com.example.testsudoku;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class trong_mainActivity extends AppCompatActivity {
//    private Button btnAddQuestion, btnPlayGame;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = true; // biến để kiểm tra trạng thái của nhạc
    private ImageView imgPlay, imgContribute, imgTutorial, imgSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.trong_activitymain);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        imgPlay = findViewById(R.id.imgPlay);
        imgContribute = findViewById(R.id.imgContribute);
        imgTutorial = findViewById(R.id.imgTutorial);
        imgSound = findViewById(R.id.imgSoundOn);

        mediaPlayer = MediaPlayer.create(this, R.raw.trong_soundbg);// file nhac
        mediaPlayer.setLooping(true); // vong lap nhac
        mediaPlayer.start(); // bat dau phat nhac

        imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying) {
                    // tam dung va thay doi icon
                    mediaPlayer.pause();
                    imgSound.setImageResource(R.drawable.trong_soundoff); //thay icon tat
                }else {
                    //phat lai nhac va thay doi icon
                    mediaPlayer.start();
                    imgSound.setImageResource(R.drawable.trong_sound);
                }
                isPlaying = !isPlaying; // doi trang thai am thanh
            }
        });
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(trong_mainActivity.this, trong_viewPlayActivity.class));
            }
        });
        imgContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(trong_mainActivity.this, trong_viewAddQuesActivity.class));
            }
        });

        imgTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(trong_mainActivity.this, trong_tutorialActivity.class));

            }
        });

    }

    //giai phong mediaplayer khi thoat ung dung
    @Override
    protected void onDestroy() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}