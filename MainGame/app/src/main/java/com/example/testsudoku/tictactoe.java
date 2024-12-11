package com.example.testsudoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tictactoe extends AppCompatActivity {

    ImageView btnBack, btnMusic;
    TextView tv;
    GridLayout gridLayout;
    Button btnPlay;
    Button[][] buttons;
    boolean player1Turn = true;
    boolean playAgainstAI;
    int getTile;
    MediaPlayer bg_sound;
    MediaPlayer winSound;
    boolean speaker = false;
    CountDownTimer countDownTimer;

    ProgressBar progressBar;
    long totalTimeCountInMilliseconds = 30 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tictactoe);

        tv = findViewById(R.id.tvResult);
        gridLayout = findViewById(R.id.gridLayout);
        btnPlay = findViewById(R.id.btnPlayagain);
        gridLayout.setBackgroundResource(R.drawable.an_border);
        btnBack = findViewById(R.id.img_icon_back);
        btnMusic = findViewById(R.id.img_music);
        bg_sound = music(R.raw.curious, true);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        playAgainstAI = getIntent().getBooleanExtra("playAI", false);
        getTile = getIntent().getIntExtra("tile", 3);
        if (getTile <= 0) {
            getTile = 3;
        }
        buttons = new Button[getTile][getTile];

        createTable();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBoard();
                btnPlay.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bg_sound.pause();
                Intent intent = new Intent(tictactoe.this, menu_tictactoe.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!speaker) {
                    btnMusic.setImageResource(R.drawable.an_music_on);
                    bg_sound.start();
                    speaker = true;
                } else {
                    btnMusic.setImageResource(R.drawable.an_music_off);
                    bg_sound.pause();
                    speaker = false;
                }
            }
        });
    }

    private void createTable() {
        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                Button button = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 80;
                params.height = 80;
                params.rowSpec = GridLayout.spec(i, 1, 1f);
                params.columnSpec = GridLayout.spec(j, 1, 1f);
                button.setLayoutParams(params);
                button.setTextSize(16);
                button.setBackgroundResource(R.drawable.an_border);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onButtonClicked(button);
                    }
                });
                gridLayout.addView(button);
                buttons[i][j] = button;
            }
        }
    }

    private void onButtonClicked(Button button) {
        if (button.getText().toString().isEmpty()) {
            if (!playAgainstAI) {
                if (player1Turn) {
                    button.setTextColor(Color.TRANSPARENT);
                    button.setText("X");
                    button.setForeground(ContextCompat.getDrawable(this, R.drawable.an_x));
                } else {
                    button.setTextColor(Color.TRANSPARENT);
                    button.setText("O");
                    button.setForeground(ContextCompat.getDrawable(this, R.drawable.an_o));
                }
                startCountDownTimer();
                player1Turn = !player1Turn;
                checkWinner();
            } else {
                if (player1Turn) {
                    button.setTextColor(Color.TRANSPARENT);
                    button.setText("X");
                    button.setForeground(ContextCompat.getDrawable(this, R.drawable.an_x));
                    player1Turn = false;
                }
                if (!checkWinner() && !player1Turn) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            aiMove();
                        }
                    }, 100);
                }
            }
        }
    }

    private void aiMove() {
        int[] bestMove = minimax(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (bestMove.length == 2) {
            int row = bestMove[0];
            int col = bestMove[1];
            if (row != -1 && col != -1) {
                buttons[row][col].setTextColor(Color.TRANSPARENT);
                buttons[row][col].setText("O");
                buttons[row][col].setForeground(ContextCompat.getDrawable(this, R.drawable.an_o));
                player1Turn = true;
                checkWinner();
            }
        }
    }

    private int[] minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        String winner = getWinner();
        if (winner != null) {
            if (winner.equals("O")) return new int[]{getTile - depth, 0};
            if (winner.equals("X")) return new int[]{depth - getTile, 0};
            return new int[]{0, 0};
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                if (buttons[i][j].getText().toString().isEmpty()) {
                    buttons[i][j].setText(isMaximizing ? "O" : "X");

                    int[] score = minimax(depth + 1, !isMaximizing, alpha, beta);
                    if (isMaximizing) {
                        if (score[0] > bestScore) {
                            bestScore = score[0];
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                        alpha = Math.max(alpha, bestScore);
                    } else {
                        if (score[0] < bestScore) {
                            bestScore = score[0];
                            bestMove[0] = i;
                            bestMove[1] = j;
                        }
                        beta = Math.min(beta, bestScore);
                    }

                    buttons[i][j].setText("");
                    if (beta <= alpha) break; // Cắt tỉa alpha-beta
                }
            }
            if (beta <= alpha) break;
        }

        return depth == 0 ? bestMove : new int[]{bestScore, 0};
    }

    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        String symbol = buttons[row][col].getText().toString();
        if (symbol.isEmpty()) return false;

        int win;
        if (getTile == 3) {
            win = 3;
        } else {
            win = 4;
        }

        int count = 0;
        for (int i = 0; i < win; i++) {
            int r = row + i * rowDir;
            int c = col + i * colDir;
            if (r < 0 || r >= getTile || c < 0 || c >= getTile || !buttons[r][c].getText().toString().equals(symbol)) {
                return false;
            }
            count++;
        }
        return count == win;
    }

    private String getWinner() {
        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                if (checkDirection(i, j, 1, 0) || checkDirection(i, j, 0, 1) || checkDirection(i, j, 1, 1) || checkDirection(i, j, 1, -1)) {
                    return buttons[i][j].getText().toString();
                }
            }
        }

        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                if (buttons[i][j].getText().toString().isEmpty()) {
                    return null; // Không có người thắng và còn ô trống
                }
            }
        }

        return "Draw"; // Hòa
    }

    private boolean checkWinner() {
        String winner = getWinner();
        if (winner != null) {
            if (winner.equals("Draw")) {
                tv.setText(String.format("%s!", winner));
            } else {
                tv.setText(String.format("%s win!", winner));
            }

            if (btnPlay.getVisibility() == View.GONE) {
                btnPlay.setVisibility(View.VISIBLE);
            }
            stop();
            return true;
        }
        return false;
    }

    private void stop() {
        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
        winSound = music(R.raw.vietvg_finish, false);
        winSound.start();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void resetBoard() {
        for (int i = 0; i < getTile; i++) {
            for (int j = 0; j < getTile; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setForeground(Drawable.createFromPath(""));
                buttons[i][j].setEnabled(true);
            }
        }
        tv.setText("");
        winSound.pause();
    }

    private void startCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Tính toán phần trăm để cập nhật ProgressBar
                int progress = (int) ((millisUntilFinished * 100) / totalTimeCountInMilliseconds);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                player1Turn = !player1Turn;
                countDownTimer.start();
            }
        };
        countDownTimer.start();
    }

    private MediaPlayer music(int musicFile, boolean state) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, musicFile);
        mediaPlayer.setLooping(state);
        return mediaPlayer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bg_sound != null) {
            bg_sound.release();
            bg_sound = null;
        }
        if (winSound != null) {
            winSound.release();
            winSound = null;
        }
    }
}