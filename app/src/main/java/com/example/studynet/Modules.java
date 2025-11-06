package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Modules extends AppCompatActivity {
    private Button btn_modules1;
    private Button btn_modules2;
    private Button btn_modules3;
    private Button btn_modules4;
    private Button btn_modules5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        btn_modules1 = (Button) findViewById(R.id.btn_modules1);
        btn_modules2 = (Button) findViewById(R.id.btn_modules2);
        btn_modules3 = (Button) findViewById(R.id.btn_modules3);
        btn_modules4 = (Button) findViewById(R.id.btn_modules4);
        btn_modules5 = (Button) findViewById(R.id.btn_modules5);

        btn_modules1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_module = new Intent(Modules.this, Modules1.class);
                startActivity(intent_module);
            }
        });

        btn_modules2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_module = new Intent(Modules.this, Modules2.class);
                startActivity(intent_module);
            }
        });

        btn_modules3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_module = new Intent(Modules.this, Modules3.class);
                startActivity(intent_module);
            }
        });

        btn_modules4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_module = new Intent(Modules.this, Modules4.class);
                startActivity(intent_module);
            }
        });

        btn_modules5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_module = new Intent(Modules.this, Modules5.class);
                startActivity(intent_module);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_modules);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            } else if (itemId == R.id.nav_modules) {
                return true;
            } else if (itemId == R.id.nav_quizzes) {
                startActivity(new Intent(getApplicationContext(), Quizzes.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            return false;
        });
    }
}