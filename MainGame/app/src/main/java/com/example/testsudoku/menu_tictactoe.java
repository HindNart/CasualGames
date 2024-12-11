package com.example.testsudoku;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menu_tictactoe extends AppCompatActivity {

    ImageView btnNewgame, btn2players, btnExit, btn3, btn5, btn8, btn10, btnCustom, btnHome, btnMusic;
    EditText edtCustomSize;
    Button btnPlay;
    View overlay;
    ConstraintLayout hiddenLayout;
    MediaPlayer mediaPlayer;
    boolean speaker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_tictactoe);

        btnNewgame = findViewById(R.id.img_newgame);
        btn2players = findViewById(R.id.img_2players);
        btnExit = findViewById(R.id.img_exit);
        btn3 = findViewById(R.id.img3x3);
        btn5 = findViewById(R.id.img5x5);
        btn8 = findViewById(R.id.img8x8);
        btn10 = findViewById(R.id.img10x10);
        btnCustom = findViewById(R.id.img_custom);
        btnHome = findViewById(R.id.img_icon_home);
        btnMusic = findViewById(R.id.img_icon_music);
        overlay = findViewById(R.id.overlay_bg);
        hiddenLayout = findViewById(R.id.hidden_layout);
        edtCustomSize = findViewById(R.id.edtCustomSize);
        btnPlay = findViewById(R.id.btnPlay);
        mediaPlayer = music(R.raw.music_bg_tictactoe, true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnNewgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                Intent intent = new Intent(menu_tictactoe.this, tictactoe.class);
                intent.putExtra("playAI", true);
                intent.putExtra("tile", 3);
                startActivity(intent);
                finish();
            }
        });

        btn2players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hiddenLayout.getVisibility() == View.GONE) {
                    hideAndShow(View.VISIBLE, View.VISIBLE);

                    Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.an_slide_up);
                    hiddenLayout.startAnimation(slideUp);
                } else {
                    hideAndShow(View.GONE, View.GONE);
                }
            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndShow(View.GONE, View.GONE);
                edtCustomSize.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                hiddenLayout.startAnimation(slideDown);
            }
        });

        hiddenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtCustomSize.getVisibility() == View.VISIBLE & btnPlay.getVisibility() == View.VISIBLE) {
                    edtCustomSize.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.GONE);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndShow(View.GONE, View.GONE);
                mediaPlayer.pause();
                switchActivity(3);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndShow(View.GONE, View.GONE);
                mediaPlayer.pause();
                switchActivity(5);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndShow(View.GONE, View.GONE);
                mediaPlayer.pause();
                switchActivity(8);
            }
        });

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAndShow(View.GONE, View.GONE);
                mediaPlayer.pause();
                switchActivity(10);
            }
        });

        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtCustomSize.getVisibility() == View.GONE & btnPlay.getVisibility() == View.GONE) {
                    edtCustomSize.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);
                } else {
                    edtCustomSize.setVisibility(View.GONE);
                    btnPlay.setVisibility(View.GONE);
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getZize = edtCustomSize.getText().toString();
                if (!getZize.isEmpty()) {
                    int size = Integer.parseInt(getZize);
                    if (size < 3 || size > 10) {
                        Toast.makeText(menu_tictactoe.this, "Sizes range from 3 to 10!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        switchActivity(size);
                        hideAndShow(View.GONE, View.GONE);
                        edtCustomSize.setVisibility(View.GONE);
                        btnPlay.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!speaker) {
                    btnMusic.setImageResource(R.drawable.an_music_on);
                    mediaPlayer.start();
                    speaker = true;
                } else {
                    btnMusic.setImageResource(R.drawable.an_music_off);
                    mediaPlayer.pause();
                    speaker = false;
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                Intent intent = new Intent(menu_tictactoe.this, Menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                Intent intent = new Intent(menu_tictactoe.this, Menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void switchActivity(int value) {
        Intent intent = new Intent(menu_tictactoe.this, tictactoe.class);
        intent.putExtra("tile", value);
        startActivity(intent);
        finish();
    }

    private void hideAndShow(int val1, int val2) {
        hiddenLayout.setVisibility(val1);
        overlay.setVisibility(val2);
    }

    private MediaPlayer music(int musicFile, boolean state) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, musicFile);
        mediaPlayer.setLooping(state);
        return mediaPlayer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}