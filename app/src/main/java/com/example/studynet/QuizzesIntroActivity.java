package com.example.studynet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class QuizzesIntroActivity extends AppCompatActivity {

    // Reuse the constant key from ModulesIntroActivity (or define it centrally)
    public static final String NAVIGATE_TO_FRAGMENT_EXTRA = "NAVIGATE_TO_FRAGMENT";
    public static final String FRAGMENT_QUIZZES = "QUIZZES"; // Define the specific fragment constant

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_intro);

        Button startButton = findViewById(R.id.btn_start_quizzes);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // *** FIX: Start HomeActivity and pass an Extra to navigate to Quizzes Fragment ***
                Intent intent = new Intent(QuizzesIntroActivity.this, HomeActivity.class);
                intent.putExtra(NAVIGATE_TO_FRAGMENT_EXTRA, FRAGMENT_QUIZZES);

                // Clear the activity stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Finish the intro activity
            }
        });
    }
}