package com.example.testsudoku;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;
import java.util.Random;


public class Sudoku extends AppCompatActivity {
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private Runnable runnable;
    private int secondsElapsed = 0;
    EditText[][] sudokuCells = new EditText[9][9];
    int[][] solutionCells = new int[9][9];
    int[][] continueCells = new int[9][9];
    Random random = new Random();
    public int point = 0, fails = 0;
    public String level = "";
    JSONArray jsonArray;
    String jsonArrayString;
    String jsonArray2String;
    int hint_count = 3;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sudoku);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Khai báo các phần tử
        ConstraintLayout main = findViewById(R.id.main);
        ImageView backToSudokuMenu = findViewById(R.id.img_backToSudokuMenu);
        ImageView hint = findViewById(R.id.imgBtnHint);
        ImageView audio = findViewById(R.id.img_audio);
        ImageView noAudio = findViewById(R.id.img_noAudio);
        ImageView settingOpen = findViewById(R.id.img_setting);
        ConstraintLayout settingLayout = findViewById(R.id.settingLayout);
        View bg_overlay = findViewById(R.id.backgroundOverlay);
        Button bg_blue = findViewById(R.id.btn_blue);
        Button bg_green = findViewById(R.id.btn_green);
        Button bg_yellow = findViewById(R.id.btn_yellow);
        Button bg_red = findViewById(R.id.btn_red);
        Button bg_white = findViewById(R.id.btn_white);
        TextView tv_hint_count = findViewById(R.id.tv_hint_count);

        settingOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingLayout.setVisibility(View.VISIBLE);
                bg_overlay.setVisibility(View.VISIBLE);
            }
        });
        bg_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingLayout.setVisibility(View.INVISIBLE);
                bg_overlay.setVisibility(View.INVISIBLE);
            }
        });

        bg_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.huy_blue_bg_2);
                settingOpen.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                audio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                noAudio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                hint.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            }
        });
        bg_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.huy_yellow_bg);
                settingOpen.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                audio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                noAudio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                hint.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            }
        });
        bg_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.huy_red_bg);
                settingOpen.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                audio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                noAudio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                hint.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            }
        });
        bg_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundResource(R.drawable.huy_green_bg);
                settingOpen.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                audio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                noAudio.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
                hint.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            }
        });
        bg_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                settingOpen.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                audio.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                noAudio.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
                hint.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
            }
        });

        timerTextView = findViewById(R.id.tv_timer);
        mediaPlayer = MediaPlayer.create(this, R.raw.huy_bgm1);
        // Thiết lập phát nhạc lặp lại
        mediaPlayer.setLooping(true);
        if (mediaPlayer != null) {
            mediaPlayer.start();  // Phát âm thanh
        } Intent intent = getIntent();
        //Lấy dữ liệu về lệnh chơi hay tiếp tục
        String play_command = intent.getStringExtra("play");

        SharedPreferences sharedPreferences = getSharedPreferences("HuyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(play_command.equals("new")){
            hint_count = 3;
        }else {
            hint_count = sharedPreferences.getInt("hint_count_save", 3);
        }
        tv_hint_count.setText(String.valueOf(hint_count));


        //Lấy dữ liệu về độ khó
        int lvl_count = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("count")));

        if(lvl_count == 30){
            level = "Dễ";
        } else if (lvl_count == 40) {
            level = "Trung bình";
        }else {
            level = "Khó";
        }

        // Tạo gridlayout
        GridLayout sudokuGrid = new GridLayout(this);
        sudokuGrid.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        //Cài đặt các thuộc tính cho grid
        sudokuGrid.setColumnCount(9); // Số cột là 9 cho ván Sudoku 9x9
        sudokuGrid.setRowCount(9);    // Số hàng là 9 cho ván Sudoku 9x9
        sudokuGrid.setBackgroundColor(Color.parseColor("#FFFFFF")); // Màu nền trắng
        sudokuGrid.setBackgroundResource(R.drawable.sudoku_grid_border); // Border cho grid

        if(play_command.equals("new")){
            makeNewGame(sudokuGrid, lvl_count);
        }
        if(play_command.equals("continue")){
            continueGame(sudokuGrid, lvl_count, sharedPreferences);
        }

        //Chức năng quay lại trang chủ
        backToSudokuMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Chuyển mảng thành JSONArray
                JSONArray jsonArray2 = new JSONArray();
                jsonArray = new JSONArray();
                for (int i = 0; i < sudokuCells.length; i++) {
                    JSONArray row = new JSONArray();
                    for (int j = 0; j < sudokuCells[i].length; j++) {
                        String text = sudokuCells[i][j].getText().toString();
                        if (!text.isEmpty()) {
                            row.put(Integer.parseInt(text));
                        } else {
                            row.put(0); // Đảm bảo ô trống được lưu trữ đúng cách
                        }
                    }
                    jsonArray.put(row);
                }
                for (int i = 0; i < solutionCells.length; i++) {
                    JSONArray row = new JSONArray();
                    for (int j = 0; j < solutionCells[i].length; j++) {
                        int text = solutionCells[i][j];
                        if (text != 0) {
                            row.put(text);
                        } else {
                            row.put(0); // Đảm bảo ô trống được lưu trữ đúng cách
                        }
                    }
                    jsonArray2.put(row);
                }
                // Lưu chuỗi JSON vào SharedPreferences
                editor.putString("array_2d", jsonArray.toString());
                editor.putString("array_solution", jsonArray2.toString());
                editor.putInt("timeCount", secondsElapsed);
                editor.apply();

                if (mediaPlayer != null) {
                    mediaPlayer.release();  // Giải phóng MediaPlayer khi activity bị hủy
                    mediaPlayer = null;
                }
                Intent i = new Intent(Sudoku.this, SudokuMenu.class);
                i.putExtra("continue_status", "continue");
                startActivity(i);
                finish();
            }
        });
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hint_count <= 0){
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                boolean hinted = false;
                for (int i = 0; i<9; i++){
                    for (int j = 0; j<9; j++){
                        if(sudokuCells[i][j].getText().toString().isEmpty()){
                            sudokuCells[i][j].setText(String.valueOf(solutionCells[i][j]));
                            sudokuCells[i][j].setBackgroundColor(Color.parseColor("#C0C0C0"));
                            hinted = true;
                            break;
                        }
                    }
                    if(hinted){
                        break;
                    }
                }
                hint_count--;

                editor.putInt("hint_count_save", hint_count);
                editor.apply();
                tv_hint_count.setText(String.valueOf(hint_count));
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noAudio.setVisibility(View.VISIBLE);
                audio.setVisibility(View.INVISIBLE);

                if (mediaPlayer != null) {
                    mediaPlayer.release();  // Giải phóng MediaPlayer khi activity bị hủy
                    mediaPlayer = null;
                }
            }
        });
        noAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noAudio.setVisibility(View.INVISIBLE);
                audio.setVisibility(View.VISIBLE);
                mediaPlayer = MediaPlayer.create(Sudoku.this, R.raw.huy_bgm1);
                // Thiết lập phát nhạc lặp lại
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }

    //Tạo ván mới
    private void makeNewGame(GridLayout sudokuGrid, int lvl_count){
        // Thêm các EditText vào GridLayout
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                EditText editText = new EditText(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.rowSpec = GridLayout.spec(row, 1f);

                editText.setLayoutParams(params);
                editText.setGravity(Gravity.CENTER);
                editText.setPadding(8, 8, 8, 8);
                editText.setTextSize(18);
                editText.setBackgroundResource(R.drawable.sudoku_border); // Đặt drawable cho viền
                editText.setInputType(InputType.TYPE_CLASS_NUMBER); // Chỉ cho phép nhập số
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); // Giới hạn 1 ký tự
                editText.setEnabled(false);
                editText.setTextColor(Color.BLACK);
                // Lưu EditText vào mảng
                sudokuCells[row][col] = editText;
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Không cần xử lý gì tại đây
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        boolean finished = true; // Giả định rằng đã hoàn thành
                        if (editText.isEnabled()) { // Chỉ kiểm tra nếu ô có thể chỉnh sửa
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    String playerInput = sudokuCells[i][j].getText().toString();

                                    // Nếu người chơi nhập sai hoặc ô chưa được điền
                                    if (!playerInput.equals(String.valueOf(solutionCells[i][j])) || playerInput.isEmpty()) {
                                        finished = false;
                                        break; // Dừng kiểm tra vì chưa hoàn thành
                                    }
                                }
                                if (!finished) {
                                    break;
                                }
                            }
                            // Nếu tất cả các ô đều đúng, chuyển sang Activity Win
                            if (finished) {
                                point = lvl_count * 300 - secondsElapsed;
                                int minutes = secondsElapsed / 60;
                                int seconds = secondsElapsed % 60;
                                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                                Intent i = new Intent(Sudoku.this, WinSudoku.class);
                                i.putExtra("time",timeFormatted);
                                i.putExtra("point_win", String.valueOf(point));
                                i.putExtra("level", level);
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();  // Giải phóng MediaPlayer khi activity bị hủy
                                    mediaPlayer = null;
                                }
                                startActivity(i);
                            }
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        // Không cần xử lý gì tại đây
                    }
                });
                sudokuGrid.addView(editText);
            }
        }
        LinearLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.addView(sudokuGrid);
        // Tạo và hiển thị bảng Sudoku
        fillBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!sudokuCells[row][col].getText().toString().isEmpty()) {
                    solutionCells[row][col] = Integer.parseInt(sudokuCells[row][col].getText().toString());
                }
            }
        }
        removeNumbers(lvl_count);
        //Bắt đầu đếm giờ
        startTimer();
    }
    //Tiếp tục ván đang chơi
    private void continueGame(GridLayout sudokuGrid, int lvl_count, SharedPreferences sharedPreferences){
        //Đọc mảng từ cục bộ
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                EditText editText = new EditText(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.rowSpec = GridLayout.spec(row, 1f);

                editText.setLayoutParams(params);
                editText.setGravity(Gravity.CENTER);
                editText.setPadding(8, 8, 8, 8);
                editText.setTextSize(18);
                editText.setBackgroundResource(R.drawable.sudoku_border); // Đặt drawable cho viền
                editText.setInputType(InputType.TYPE_CLASS_NUMBER); // Chỉ cho phép nhập số
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); // Giới hạn 1 ký tự
                editText.setEnabled(false);
                editText.setTextColor(Color.BLACK);
                // Lưu EditText vào mảng
                sudokuCells[row][col] = editText;
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // Không cần xử lý gì tại đây
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        boolean finished = true; // Giả định rằng đã hoàn thành
                        if (editText.isEnabled()) { // Chỉ kiểm tra nếu ô có thể chỉnh sửa
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    String playerInput = sudokuCells[i][j].getText().toString();

                                    // Nếu người chơi nhập sai hoặc ô chưa được điền
                                    if (!playerInput.equals(String.valueOf(solutionCells[i][j])) || playerInput.isEmpty()) {
                                        finished = false;
                                        break; // Dừng kiểm tra vì chưa hoàn thành
                                    }
                                }
                                if (!finished) {
                                    break;
                                }
                            }
                            // Nếu tất cả các ô đều đúng, chuyển sang Activity Win
                            if (finished) {
                                point = lvl_count * 300 - secondsElapsed;
                                int minutes = secondsElapsed / 60;
                                int seconds = secondsElapsed % 60;
                                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                                Intent i = new Intent(Sudoku.this, WinSudoku.class);
                                i.putExtra("time",timeFormatted);
                                i.putExtra("point_win", String.valueOf(point));
                                i.putExtra("level", level);
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();  // Giải phóng MediaPlayer khi activity bị hủy
                                    mediaPlayer = null;
                                }
                                startActivity(i);
                            }
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        // Không cần xử lý gì tại đây
                    }
                });
                sudokuGrid.addView(editText);
            }
        }
        LinearLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.addView(sudokuGrid);
        //Gán giá trị cho mảng để kiểm tra
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!sudokuCells[row][col].getText().toString().isEmpty()) {
                    solutionCells[row][col] = Integer.parseInt(sudokuCells[row][col].getText().toString());
                }
            }
        }
        jsonArrayString = sharedPreferences.getString("array_2d", null);
        jsonArray2String = sharedPreferences.getString("array_solution", null);
        secondsElapsed = sharedPreferences.getInt("timeCount", 0);
        if (jsonArrayString != null) {
            try {
                jsonArray = new JSONArray(jsonArrayString);
                continueCells = new int[jsonArray.length()][];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray row = jsonArray.getJSONArray(i);
                    continueCells[i] = new int[row.length()];
                    for (int j = 0; j < row.length(); j++) {
                        continueCells[i][j] = row.getInt(j);
                    }
                }
                // Bây giờ bạn có thể sử dụng lại mảng 2 chiều
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (jsonArray2String != null) {
            try {
                jsonArray = new JSONArray(jsonArray2String);
                solutionCells = new int[jsonArray.length()][];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray row = jsonArray.getJSONArray(i);
                    solutionCells[i] = new int[row.length()];
                    for (int j = 0; j < row.length(); j++) {
                        solutionCells[i][j] = row.getInt(j);
                    }
                }
                // Bây giờ bạn có thể sử dụng lại mảng 2 chiều
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if(continueCells[row][col] == 0){
                    sudokuCells[row][col].setText("");
                    sudokuCells[row][col].setEnabled(true);
                }else{
                    sudokuCells[row][col].setText(String.valueOf(continueCells[row][col]));
                }
            }
        }
        startTimer();
    }
    //Hàm đếm thời gian chơi
    private void startTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Tính toán phút và giây từ số giây đã trôi qua
                int minutes = secondsElapsed / 60;
                int seconds = secondsElapsed % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                timerTextView.setText(timeFormatted);

                // Tăng số giây đã trôi qua
                secondsElapsed++;

                // Lên lịch cho Runnable tiếp theo sau 1 giây
                handler.postDelayed(this, 1000);
            }
        };

        // Bắt đầu bộ đếm thời gian
        handler.post(runnable);
    }
    //Hàm kiểm tra xem có hợp lệ trong hàng hay không
    public boolean UnusedInRow(int row, int value) {
        for (int x = 0; x < 9; x++) {
            if (!sudokuCells[row][x].getText().toString().isEmpty()) {
                if (Integer.parseInt(sudokuCells[row][x].getText().toString()) == value) {
                    return false;
                }
            }
        }
        return true;
    }
    //Hàm kiểm tra xem có hợp lệ trong cột hay không
    public boolean UnusedInCol(int col, int value) {
        for (int x = 0; x < 9; x++) {
            if (!sudokuCells[x][col].getText().toString().isEmpty()) {
                if (Integer.parseInt(sudokuCells[x][col].getText().toString()) == value) {
                    return false;
                }
            }
        }
        return true;
    }
    //Hàm kiểm tra xem có hợp lệ trong ô 3x3 hay không
    public boolean UnusedInBox(int startRow, int startCol, int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!sudokuCells[startRow + i][startCol + j].getText().toString().isEmpty()) {
                    if (Integer.parseInt(sudokuCells[startRow + i][startCol + j].getText().toString()) == value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //Hàm in theo hàng chéo -> giúp giảm độ phức tạp của thuật toán
    private void fillDiagonal() {
        for (int i = 0; i < 9; i += 3) {
            fillBox(i, i);
        }
    }
    //Hàm tạo số
    private int randomGenerator(int num) {
        return random.nextInt(num) + 1;
    }
    // Điền số vào 1 ô vuông 3x3
    private void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = randomGenerator(9); // Tạo số ngẫu nhiên từ 1 đến 9
                } while (!UnusedInBox(row, col, num)); // Kiểm tra số có trùng lặp trong ô vuông 3x3
                sudokuCells[row + i][col + j].setText(String.valueOf(num)); // Đặt số vào EditText
            }
        }
    }
    //Hàm kiểm tra xem số điền vào thỏa mãn 3 điều kiện hay không
    private boolean isSafe(int i, int j, int num) {
        return UnusedInRow(i, num) &&
                UnusedInCol(j, num) &&
                UnusedInBox(i - i % 3, j - j % 3, num);
    }
    //Hàm điền các số
    private boolean fillRemaining(int i, int j) {
        if (j >= 9 && i < 8) {
            i++;
            j = 0;
        }
        if (i >= 9 && j >= 9) {
            return true;
        }

        if (i < 3) {
            if (j < 3) {
                j = 3;
            }
        } else if (i < 6) {
            if (j == (i / 3) * 3) {
                j += 3;
            }
        } else {
            if (j == 6) {
                i++;
                j = 0;
                if (i >= 9) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= 9; num++) {
            if (isSafe(i, j, num)) {
                sudokuCells[i][j].setText(String.valueOf(num)); // Điền số vào EditText

                if (fillRemaining(i, j + 1)) {
                    return true;
                }
                sudokuCells[i][j].setText(""); // Reset lại ô nếu số không hợp lệ
            }
        }
        return false;
    }
    //Hàm điền số vào bảng
    private void fillBoard() {
        fillDiagonal(); // Điền các ô trên đường chéo
        fillRemaining(0, 3); // Điền các ô còn lại của bảng
    }
    //Hàm xóa số
    private void removeNumbers(int count) {
        while (count != 0) {
            int cellId = randomGenerator(81) - 1; // Lấy ngẫu nhiên 1 ô
            int i = cellId / 9; // Tính hàng của ô
            int j = cellId % 9; // Tính cột của ô

            // Kiểm tra nếu ô không trống
            if (!sudokuCells[i][j].getText().toString().isEmpty()) {
                sudokuCells[i][j].setText(""); // Xóa giá trị của ô, làm trống ô đó
                sudokuCells[i][j].setEnabled(true);
                count--; // Giảm số lượng ô cần xóa
            }
        }
    }
    // Hàm dừng bộ đếm thời gian nếu Activity bị huỷ
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}