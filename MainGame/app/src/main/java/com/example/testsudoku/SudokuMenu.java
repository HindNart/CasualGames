package com.example.testsudoku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SudokuMenu extends AppCompatActivity {
    private LinearLayout layoutDifficulty;
    private View opacityView;
    int highest_point = 0;
    int getPoint = 0;
    String status_continue = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sudoku_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tham chiếu đến layout chọn độ khó
        layoutDifficulty = findViewById(R.id.layoutDifficulty);
        Button quitSudoku = findViewById(R.id.button_QuitSudoku);
        Button continueSudoku = findViewById(R.id.button_continueSudoku);
        TextView highest_point_tv = findViewById(R.id.tv_highestPoint);

        Intent iGet = getIntent();
        if(iGet.hasExtra("continue_status")){
            status_continue = iGet.getStringExtra("continue_status");
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //Ghi dữ liệu vào cục bộ
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(iGet.hasExtra("reset") && iGet.getStringExtra("reset").equals("1")){
            editor.remove("continue_status");
            editor.apply();
        }
        if(sharedPreferences.getString("continue_status", "").equals("continue")){
            status_continue = sharedPreferences.getString("continue_status", "");
        }

        if(status_continue.equals("continue")){
            continueSudoku.setVisibility(View.VISIBLE);
            editor.putString("continue_status", status_continue);
            editor.apply();  // Sử dụng apply() hoặc commit()
        }else{
            continueSudoku.setVisibility(View.INVISIBLE);
        }

        if (iGet.hasExtra("point_win") && !iGet.getStringExtra("point_win").isEmpty()) {
            getPoint = Integer.parseInt(iGet.getStringExtra("point_win"));
            if (getPoint >= highest_point) {
                highest_point = getPoint;
                highest_point_tv.setText("Điểm cao nhất : " + highest_point);
                editor.putInt("highest_point", highest_point);
                editor.apply(); // Lưu điểm cao nhất vào SharedPreferences
            }
        }

        //Đọc dữ liệu
        highest_point = sharedPreferences.getInt("highest_point", 0);

        Button newGame = findViewById(R.id.button_newgameSudoku);

        Intent i = new Intent(SudokuMenu.this, Sudoku.class);

        highest_point_tv.setText("Điểm cao nhất : " + highest_point);

        continueSudoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.putExtra("play", "continue");
                i.putExtra("count", "30");
                startActivity(i);
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDifficultyLayout();
                Button easy = findViewById(R.id.btnEasy);
                Button medium = findViewById(R.id.btnMedium);
                Button hard = findViewById(R.id.btnHard);
                easy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i.putExtra("play", "new");
                        i.putExtra("count", "30");
                        startActivity(i);
                        finish();
                    }
                });
                medium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i.putExtra("play", "new");
                        i.putExtra("count", "40");
                        startActivity(i);
                        finish();
                    }
                });
                hard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i.putExtra("play", "new");
                        i.putExtra("count", "50");
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        opacityView = findViewById(R.id.backgroundOverlay);
        opacityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutDifficulty.startAnimation(slideDown);  // Áp dụng animation
                layoutDifficulty.setVisibility(View.GONE);
                opacityView.setVisibility(View.INVISIBLE);
            }
        });
        quitSudoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SudokuMenu.this, Menu.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void showDifficultyLayout() {
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        opacityView = findViewById(R.id.backgroundOverlay);
        layoutDifficulty.setVisibility(View.VISIBLE);  // Hiển thị layout
        layoutDifficulty.startAnimation(slideUp);
        opacityView.setVisibility(View.VISIBLE);// Áp dụng animation
    }
}