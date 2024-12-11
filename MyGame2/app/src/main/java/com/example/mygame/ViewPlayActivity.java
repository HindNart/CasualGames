package com.example.mygame;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewPlayActivity extends AppCompatActivity {
    private ImageView imgViewPlay;
    private EditText edAnswerPlay;
    private Button btnCheck, btnHintPlay, btnNextQuestion;
    private TextView txtQues;
    private DatabaseHelper dpHelper;
    private String currentQuestionId;
    private String correctAnswer;
    private String hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgViewPlay = findViewById(R.id.imgViewPlay);
        edAnswerPlay = findViewById(R.id.edAnswerPlay);
        btnCheck = findViewById(R.id.btnCheck);
        btnHintPlay = findViewById(R.id.btnHintPlay);
        btnNextQuestion = findViewById(R.id.btnNextQues);
        txtQues = findViewById(R.id.txtQues);
        dpHelper = new DatabaseHelper(ViewPlayActivity.this);
        dpHelper.getWritableDatabase();

        loadNextQuestion();

        btnCheck.setOnClickListener(v -> checkAnswer());
        btnHintPlay.setOnClickListener(v -> showHint());
        btnNextQuestion.setOnClickListener(v -> loadNextQuestion());
    }
    private void loadNextQuestion() {
        Cursor cs = dpHelper.getRandomQuestion();
        if(cs != null && cs.moveToFirst()) {
            currentQuestionId = cs.getString(cs.getColumnIndex("id"));
            String imgname = cs.getString(cs.getColumnIndex("image_name"));
            String question = cs.getString(cs.getColumnIndex("questions"));
            correctAnswer = cs.getString(cs.getColumnIndex("answer"));
            hint = cs.getString(cs.getColumnIndex("hint"));
            txtQues.setText(question);
            //load image from uri or drawable
            if(imgname.startsWith("content://")) {
                imgViewPlay.setImageURI(Uri.parse(imgname));
            }else{
                int resId = getResources().getIdentifier(imgname, "drawable", getPackageName());
                if(resId != 0) {
                    imgViewPlay.setImageResource(resId);

                }else{
                    Log.e("Image Resource", "Resource not found: " + imgname);
                }
            }
        }else{
            Toast.makeText(this, "Không còn câu hỏi nào!", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(cs != null) cs.close();
    }

    private void showHint() {
        Toast.makeText(ViewPlayActivity.this, "Gợi ý: " + hint, Toast.LENGTH_SHORT).show();
    }

    private void checkAnswer() {
        String userAnswer = edAnswerPlay.getText().toString().trim();
        if(userAnswer.equalsIgnoreCase(correctAnswer)) {
            Toast.makeText(ViewPlayActivity.this, "Câu trả lời của bạn đúng rồi ^^!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ViewPlayActivity.this, "Sai rồi T_T! Thử lại nhé!", Toast.LENGTH_SHORT).show();
        }
    }
}