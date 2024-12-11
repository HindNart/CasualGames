package com.example.testsudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView sudoku = findViewById(R.id.img_Sudoku);
        ImageView flipcard = findViewById(R.id.img_FlipCard);
        ImageView caro = findViewById(R.id.img_Caro);
        ImageView food_fusion_frenzy = findViewById(R.id.food_fusion_frenzy);
        ImageView CatchTheWorld = findViewById(R.id.imgCatchTheWorld);
        sudoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, SudokuMenu.class);
                startActivity(i);
                finish();
            }
        });

        flipcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, menu_FlipCard.class);
                startActivity(i);
                finish();
            }
        });

        caro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, menu_tictactoe.class);
                startActivity(i);
                finish();
            }
        });

        caro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, menu_tictactoe.class);
                startActivity(i);
            }
        });

        food_fusion_frenzy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, duong_PageClickActivity.class);
                startActivity(i);
            }
        });
        CatchTheWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Menu.this, trong_mainActivity.class);
                startActivity(i);
            }
        });
    }


}