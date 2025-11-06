package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Quizzes extends AppCompatActivity {
    private Button btn_quizzes1;
    private Button btn_quizzes2;
    private Button btn_quizzes3;
    private Button btn_quizzes4;
    private Button btn_quizzes5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);

        btn_quizzes1 = (Button) findViewById(R.id.btn_quizzes1);
        btn_quizzes2 = (Button) findViewById(R.id.btn_quizzes2);
        btn_quizzes3 = (Button) findViewById(R.id.btn_quizzes3);
        btn_quizzes4 = (Button) findViewById(R.id.btn_quizzes4);
        btn_quizzes5 = (Button) findViewById(R.id.btn_quizzes5);

        btn_quizzes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_session = new Intent(Quizzes.this, Quizzes1.class);
                startActivity(intent_session);
            }
        });

        btn_quizzes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_session = new Intent(Quizzes.this, Quizzes2.class);
                startActivity(intent_session);
            }
        });

        btn_quizzes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_session = new Intent(Quizzes.this, Quizzes3.class);
                startActivity(intent_session);
            }
        });

        btn_quizzes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_session = new Intent(Quizzes.this, Quizzes4.class);
                startActivity(intent_session);
            }
        });

        btn_quizzes5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_session = new Intent(Quizzes.this, Quizzes5.class);
                startActivity(intent_session);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_quizzes);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            } else if (itemId == R.id.nav_sessions) {
                startActivity(new Intent(getApplicationContext(), Sessions.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            } else if (itemId == R.id.nav_quizzes) {
                return true;
            }
            return false;
        });
    }
}