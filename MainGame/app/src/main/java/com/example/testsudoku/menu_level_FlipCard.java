package com.example.testsudoku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class menu_level_FlipCard extends AppCompatActivity {
    public class LevelButton{
        public int level;
        private ImageView imgView;
        public boolean isLock;

        public ImageView getImgView() {
            return imgView;
        }

        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }
    }

    private ArrayList<Integer> imageList;
    private LevelButton[] levelButtons;

    public void LoadLevel(){
        levelButtons[0].isLock = false;
        for (int i = 0; i < 6; i++){
            if (levelButtons[i].isLock){
                levelButtons[i].getImgView().setImageResource(imageList.get(0));
            }
        }
    }

    public void LoadButton(){
        ImageView btnLv1 = findViewById(R.id.imgBtn1);
        ImageView btnLv2 = findViewById(R.id.imgBtn2);
        ImageView btnLv3 = findViewById(R.id.imgBtn3);
        ImageView btnLv4 = findViewById(R.id.imgBtn4);
        ImageView btnLv5 = findViewById(R.id.imgBtn5);
        ImageView btnLv6 = findViewById(R.id.imgBtn6);

        ArrayList<ImageView> imgViews = new ArrayList<>();
        levelButtons = new LevelButton[6];
        imgViews.add(btnLv1);
        imgViews.add(btnLv2);
        imgViews.add(btnLv3);
        imgViews.add(btnLv4);
        imgViews.add(btnLv5);
        imgViews.add(btnLv6);

        for (int i = 0; i < 6; i++){
            LevelButton lvbtn = new LevelButton();
            lvbtn.level = i+1;
            lvbtn.setImgView(imgViews.get(i));
            int levelIndex = i + 1;

            SharedPreferences sharedPreferences = getSharedPreferences("vietvg_Prefs", MODE_PRIVATE);
            lvbtn.isLock = sharedPreferences.getBoolean(String.format("level%d", levelIndex), true);

            lvbtn.getImgView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lvbtn.isLock) return;

                    Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
                    i.putExtra("level", String.valueOf(levelIndex));
                    startActivity(i);
                    finish();
                }
            });

            levelButtons[i] = lvbtn;
        }
//        btnLv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("1"));
//                startActivity(i);
//            }
//        });
//        btnLv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("2"));
//                startActivity(i);
//            }
//        });
//        btnLv3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("3"));
//                startActivity(i);
//            }
//        });
//        btnLv4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("4"));
//                startActivity(i);
//            }
//        });
//        btnLv5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("5"));
//                startActivity(i);
//            }
//        });
//        btnLv6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(menu_level_FlipCard.this, FlipCard.class);
//                i.putExtra("level", String.valueOf("6"));
//                startActivity(i);
//            }
//        });
    }

    public void SetEventButton(){
        ImageView btnBack = findViewById(R.id.btnBackToHome);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_level_FlipCard.this, menu_FlipCard.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_level_flip_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageList = new ArrayList<>();
        imageList.add(R.drawable.vietvg_locklevel);
        imageList.add(R.drawable.vietvg_lv1);
        imageList.add(R.drawable.vietvg_lv2);
        imageList.add(R.drawable.vietvg_lv3);
        imageList.add(R.drawable.vietvg_lv4);
        imageList.add(R.drawable.vietvg_lv5);
        imageList.add(R.drawable.vietvg_lv6);

        SetEventButton();
        LoadButton();
        LoadLevel();
    }
}