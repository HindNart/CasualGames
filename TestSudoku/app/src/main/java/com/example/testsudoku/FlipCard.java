package com.example.testsudoku;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;


import java.util.ArrayList;
import java.util.Random;

public class FlipCard extends AppCompatActivity {

    // ------- Card class -------
    public class Card{
        private int id;
        private int idBg;
        private ImageView imgView;
        private boolean isRunning = true;

        public int getId() {
            return id;
        }
        public int getIdBg() {
            return idBg;
        }
        public void setIdBg(int idBgSet) {
            this.idBg = idBgSet;
        }
        public void setImgView(ImageView imgView) {
            this.imgView = imgView;
        }

        public ImageView getImgView() {
            return imgView;
        }

        public void setIDImg(int idSet) {
            this.id = idSet;
            this.imgView.setImageResource(idSet);
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }
    }

    // ------- Elements -------
    public TextView txtScore;
    public TextView txtScorePopup;
    ProgressBar determinateProgressBar;
    public int score = 0;

    // ------ Media ------
    private ArrayList<Integer> bgMusics;
    MediaPlayer bgMusic;
    MediaPlayer soundCorrect;
    MediaPlayer soundIncorrect;
    MediaPlayer soundFinish;

    // ------- Controller -------
    int timeSetProgress = 0;
    int lastProgress = -1;
    boolean isWin = false;
    ConstraintLayout winPopup;
    ConstraintLayout losePopup;
    ConstraintLayout settingPopup;
    int curLevel;
    boolean isMusicOn;
    boolean isSettingOn;

    public Card[][] cards;
    private ArrayList<Integer> imageList;
    private ArrayList<Integer> imageTitleLevelList;
    public int[][] idCheckImg;

    public int countCorrect;
    public int amoutOpen = 0;

    // width height table
    int widthTable = 4;
    int heightTable = 5;
    int widthCard;
    int heightCard;


    int amountCard;

    public Card preCard;
    public Card afterCard;

    public boolean CheckCard(){
        if (preCard.getId() == afterCard.getId()) return true;

        return false;
    }

    public void ResetCard(){
        ObjectAnimator animatorControl = ObjectAnimator.ofFloat(preCard.getImgView(), "rotationY", 0f, -90f);
        animatorControl.setDuration(250);
        animatorControl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                preCard.getImgView().setImageResource(preCard.getIdBg());
                ObjectAnimator animatorControl = ObjectAnimator.ofFloat(preCard.getImgView(), "rotationY", -90f, 0f);
                animatorControl.setDuration(250);
                animatorControl.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        preCard.setRunning(false);
                    }
                });
                animatorControl.start();
            }
        });
        animatorControl.start();

        animatorControl = ObjectAnimator.ofFloat(afterCard.getImgView(), "rotationY", 0f, -90f);
        animatorControl.setDuration(250);
        animatorControl.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                afterCard.getImgView().setImageResource(afterCard.getIdBg());
                ObjectAnimator animatorControl = ObjectAnimator.ofFloat(afterCard.getImgView(), "rotationY", -90f, 0f);
                animatorControl.setDuration(250);
                animatorControl.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        afterCard.setRunning(false);
                    }
                });
                animatorControl.start();
            }
        });
        animatorControl.start();

        amoutOpen = 0;
    }

    public void CheckWin(int amountCard){
        if (countCorrect == amountCard){
            isWin = true;
            Handler handler = new Handler();
            // Thực hiện hàm sau 3 giây
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hàm sẽ được gọi sau 3 giây
                    soundFinish.start();
                    ShowWinPopup();
                }
            }, 1000);

        }else {
            Log.e("Win:", "false");
        }
    }

    public void ShowWinPopup(){
        winPopup.setVisibility(View.VISIBLE);
    }
    public void ShowLosePopup(){
        losePopup.setVisibility(View.VISIBLE);
    }
    public void ShowSettingPopup(){
        isSettingOn = true;
        settingPopup.setVisibility(View.VISIBLE);
    }
    public void HideSettingPopup(){
        isSettingOn = false;
        settingPopup.setVisibility(View.INVISIBLE);
        SetProgress(timeSetProgress);
    }

    public void CreateTable(){
        imageList = new ArrayList<>();
        imageList.add(R.drawable.vietvg_bgcard);
        imageList.add(R.drawable.vietvg_grape);
        imageList.add(R.drawable.vietvg_apple);
        imageList.add(R.drawable.vietvg_orange);
        imageList.add(R.drawable.vietvg_pinaapple);
        imageList.add(R.drawable.vietvg_apricot);
        imageList.add(R.drawable.vietvg_kiwi);
        imageList.add(R.drawable.vietvg_avocado);
        imageList.add(R.drawable.vietvg_mango);
        imageList.add(R.drawable.vietvg_banana);
        imageList.add(R.drawable.vietvg_chicken);
        imageList.add(R.drawable.vietvg_woods);
        imageList.add(R.drawable.vietvg_hamburger);
        imageList.add(R.drawable.vietvg_passion);
        imageList.add(R.drawable.vietvg_pear);

        GridLayout gridCard = findViewById(R.id.gridLayoutCard);

        gridCard.removeAllViews();

        int amountChoose = 0;

        gridCard.setRowCount(widthTable);
        gridCard.setColumnCount(heightTable);

        idCheckImg = new int[amountCard][2];

        for (int i = 0; i < amountCard; i++){
            idCheckImg[i][0] = -1;
        }
        for (int i = 0; i < amountCard; i++){
            idCheckImg[i][1] = 0;
        }

        cards = new Card[widthTable][heightTable];

        for (int i = 0; i < widthTable; i++){
            for (int j = 0; j < heightTable; j++){
                Card card = new Card();
                ImageView imgCard = new ImageView(FlipCard.this);

                card.setImgView(imgCard);

                Random random = new Random();
                int min = 1;
                int max = imageList.size();
                int randomNumber;
                int indexIdCheckImg = 0;
                boolean checkArr = false;
                int resultId = 0;
                do {
                    //Log.e("dowhile", "");
                    if (amountChoose < amountCard){
                        randomNumber = random.nextInt(max - min) + min;
                        checkArr = true;
                        for (int k = 0; k < amountCard; k++) {
                            if (idCheckImg[k][0] != randomNumber && idCheckImg[k][0] != -1){
                                continue;
                            }
                            else if (idCheckImg[k][0] != randomNumber && idCheckImg[k][1] == 2){
                                continue;
                            }else if (idCheckImg[k][0] == randomNumber && idCheckImg[k][1] == 2){
                                break;
                            }

                            if (randomNumber == idCheckImg[k][0]){
                                if (idCheckImg[k][1] < 2){
                                    checkArr = false;
                                }
                                else if (idCheckImg[k][1] == 2){
                                    break;
                                }
                            }
                            else if (randomNumber != idCheckImg[k][0]) {
                                amountChoose += 1;
                                checkArr = false;
                            }

                            if (!checkArr){
                                indexIdCheckImg = k;
                                resultId = randomNumber;
                                break;
                            }
                        }
                    }
                    else {
                        randomNumber = random.nextInt(amountCard);
                        int countIndex = idCheckImg[randomNumber][1];

                        if (countIndex < 2){
                            checkArr = false;
                            indexIdCheckImg = randomNumber;
                            resultId = idCheckImg[randomNumber][0];
                        }
                        else {
                            checkArr = true;
                        }
                    }
                }while (checkArr);

                card.setIDImg(imageList.get(resultId));
                card.setIdBg(imageList.get(0));

                idCheckImg[indexIdCheckImg][0] = resultId;
                idCheckImg[indexIdCheckImg][1] += 1;

                GridLayout.LayoutParams gridChild = new GridLayout.LayoutParams();

                gridChild.width = widthCard;
                gridChild.height = heightCard;

                int spacingRowColumn = 5;
                gridChild.setMargins(spacingRowColumn, spacingRowColumn, spacingRowColumn, spacingRowColumn);
                gridChild.rowSpec = GridLayout.spec(i, 1, 1);
                gridChild.columnSpec = GridLayout.spec(j, 1, 1);

                card.getImgView().setLayoutParams(gridChild);
                gridCard.addView(card.getImgView());

                cards[i][j] = card;
            }
        }
    }

    public void StartGame(){
        for (int i = 0; i < widthTable; i++) {
            for (int j = 0; j < heightTable; j++) {
                Card card = cards[i][j];

                ObjectAnimator animatorControl = ObjectAnimator.ofFloat(card.getImgView(), "rotationY", 0f, -90f);
                animatorControl.setDuration(250); // thời gian quay (1000 = 1s)
                animatorControl.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        card.getImgView().setImageResource(card.getIdBg());
                        ObjectAnimator animatorControl = ObjectAnimator.ofFloat(card.getImgView(), "rotationY", -90f, 0f);
                        animatorControl.setDuration(250);
                        animatorControl.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                card.setRunning(false);
                            }
                        });
                        animatorControl.start();
                    }
                });
                animatorControl.start();
            }
        }
    }

    public void AddListennerToCard() {
        for (int i = 0; i < widthTable; i++) {
            for (int j = 0; j < heightTable; j++) {
                Card card = cards[i][j];

                card.getImgView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (card.isRunning()) return;

                        card.setRunning(true);
                        ObjectAnimator animatorControl = ObjectAnimator.ofFloat(card.getImgView(), "rotationY", 0f, -90f);
                        animatorControl.setDuration(250); // thời gian quay (1000 = 1s)
                        animatorControl.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                card.getImgView().setImageResource(card.getId());
                                ObjectAnimator animatorControl = ObjectAnimator.ofFloat(card.getImgView(), "rotationY", -90f, 0f);
                                animatorControl.setDuration(250);
                                animatorControl.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (amoutOpen == 0){
                                            preCard = card;
                                            amoutOpen += 1;
                                        }else if (amoutOpen == 1){
                                            afterCard = card;
                                            amoutOpen += 1;
                                        }

                                        if (amoutOpen == 2){
                                            if (CheckCard()){
                                                countCorrect += 1;

                                                soundCorrect.start();
                                                //preCard.getImgView().setVisibility(View.INVISIBLE);
                                                //afterCard.getImgView().setVisibility(View.INVISIBLE);

                                                score += 20;
                                                txtScore.setText(String.valueOf(score));
                                                txtScorePopup.setText(String.valueOf(score));

                                                CheckWin(amountCard);
                                                amoutOpen = 0;
                                            }
                                            else {
                                                soundIncorrect.start();
                                                ResetCard();
                                            }
                                        }
                                    }
                                });
                                animatorControl.start();
                            }
                        });
                        animatorControl.start();
                    }
                });
            }
        }
    }

    public void SetLevel(){
        Intent i = getIntent();
        int levelGame = Integer.parseInt(i.getStringExtra("level"));
        curLevel = levelGame;

        determinateProgressBar = findViewById(R.id.progressBar);

        AddTitleLevel(levelGame - 1);

        if (levelGame == 1){
            widthTable = 3;
            heightTable = 2;
            widthCard = 325;
            heightCard = 325;
            timeSetProgress = 20000;
        }
        else if (levelGame == 2){
            widthTable = 4;
            heightTable = 2;
            widthCard = 250;
            heightCard = 250;
            timeSetProgress = 25000;
        }
        else if (levelGame == 3){
            widthTable = 4;
            heightTable = 3;
            widthCard = 250;
            heightCard = 250;
            timeSetProgress = 30000;
        }
        else if (levelGame == 4){
            widthTable = 4;
            heightTable = 4;
            widthCard = 225;
            heightCard = 225;
            timeSetProgress = 35000;
        }
        else if (levelGame == 5){
            widthTable = 5;
            heightTable = 4;
            widthCard = 200;
            heightCard = 200;
            timeSetProgress = 40000;
        }
        else if (levelGame == 6){
            widthTable = 6;
            heightTable = 4;
            widthCard = 180;
            heightCard = 180;
            timeSetProgress = 45000;
        }

        SetProgress(timeSetProgress);
        amountCard = widthTable * heightTable / 2;
    }

    public void SetEventButton(){
        ImageView btnQuit = findViewById(R.id.btnQuitToLevel);
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FlipCard.this, menu_level_FlipCard.class);
                startActivity(i);
                finish();
            }
        });

        ImageView btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowSettingPopup();
            }
        });
        ImageView btnOffSetting = findViewById(R.id.btnOffSetting);
        btnOffSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideSettingPopup();
            }
        });

        ImageView btnCheck = findViewById(R.id.btnNextlevel);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnlockNewLevel();

                Intent i = new Intent(FlipCard.this, menu_level_FlipCard.class);
                startActivity(i);
                finish();
            }
        });
        ImageView btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });

        ImageView btnMusic = findViewById(R.id.btnMusic);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMusicOn){
                    btnMusic.setImageResource(R.drawable.off);

                    bgMusic.pause();

                    isMusicOn = false;
                }
                else {
                    btnMusic.setImageResource(R.drawable.on);

                    bgMusic.start();

                    isMusicOn = true;
                }
            }
        });
    }

    public void UnlockNewLevel(){
        SharedPreferences sharedPreferences = getSharedPreferences("vietvg_Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int nextLv = curLevel + 1;
        editor.putBoolean(String.format("level%d", nextLv), false);
        editor.apply();
    }

    public void SetProgress(float timerSet){
        // Tăng tiến trình dần dần (ví dụ như tải xuống)
        new Thread(new Runnable() {
            @Override
            public void run() {
                int setPr;
                if (lastProgress == -1){
                    setPr = 100;
                }
                else {
                    setPr = lastProgress;
                }

                for (int i = setPr; i >= 0; i--) {
                    if (i == 0 && !isWin){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowLosePopup();
                            }
                        });

                        break;
                    } else if (isWin || isSettingOn) {
                        break;
                    }

                    final int progress = i;
                    lastProgress = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            determinateProgressBar.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep((long) timerSet/60); // Chờ 100ms giữa mỗi lần tăng tiến trình
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void AddTitleLevel(int index){
        imageTitleLevelList = new ArrayList<>();
        imageTitleLevelList.add(R.drawable.vietvg_titlelv1);
        imageTitleLevelList.add(R.drawable.vietvg_titlelv2);
        imageTitleLevelList.add(R.drawable.vietvg_titlelv3);
        imageTitleLevelList.add(R.drawable.vietvg_titlelv4);
        imageTitleLevelList.add(R.drawable.vietvg_titlelv5);
        imageTitleLevelList.add(R.drawable.vietvg_titlelv6);

        ImageView imgTitle = findViewById(R.id.imgTitleLevel);
        imgTitle.setImageResource(imageTitleLevelList.get(index));
    }

    public void CreateSound(){
        bgMusics = new ArrayList<>();
        bgMusics.add(R.raw.vietvg_bgmusic);
        bgMusics.add(R.raw.vietvg_bgmusic2);
        bgMusics.add(R.raw.vietvg_bgmusic3);

        Random random = new Random();
        int rdBgMusicIndex = random.nextInt(3);
        bgMusic = MediaPlayer.create(this, bgMusics.get(rdBgMusicIndex));

        soundCorrect = MediaPlayer.create(this, R.raw.vietvg_correct);
        soundIncorrect = MediaPlayer.create(this, R.raw.vietvg_wrong);
        soundFinish = MediaPlayer.create(this, R.raw.vietvg_finish);

        bgMusic.setVolume(0.8f,0.8f);
        bgMusic.setLooping(true);
        bgMusic.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flip_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isWin = false;
        isMusicOn = true;
        isSettingOn = false;

        SetEventButton();
        SetLevel();

        // ---------- GetbyId element ---------
        winPopup = findViewById(R.id.vietvg_winPanel);
        losePopup = findViewById(R.id.vietvg_losePanel);
        settingPopup = findViewById(R.id.vietvg_settingPanel);
        txtScore = findViewById(R.id.txtScore);
        txtScorePopup = findViewById(R.id.txtWinPopup);

        CreateSound();
        CreateTable();
        Handler handler = new Handler();
        // Thực hiện hàm sau 3 giây
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hàm sẽ được gọi sau 3 giây
                StartGame();
            }
        }, 1500);

        AddListennerToCard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        imageList.clear();
        imageList = null;

        imageTitleLevelList.clear();
        imageTitleLevelList = null;

        soundCorrect.release();
        soundCorrect = null;
        soundIncorrect.release();
        soundIncorrect = null;
        soundFinish.release();
        soundFinish = null;

        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
    }
}