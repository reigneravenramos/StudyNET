package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModulesIntroActivity extends AppCompatActivity {

    // Define a constant key for the Intent Extra
    public static final String NAVIGATE_TO_FRAGMENT_EXTRA = "NAVIGATE_TO_FRAGMENT";
    public static final String FRAGMENT_MODULES = "MODULES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules_intro);

        Button startButton = findViewById(R.id.btn_start_modules);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // *** FIX: Start HomeActivity and pass an Extra to navigate to Modules Fragment ***
                Intent intent = new Intent(ModulesIntroActivity.this, HomeActivity.class);
                intent.putExtra(NAVIGATE_TO_FRAGMENT_EXTRA, FRAGMENT_MODULES);

                // Clear the activity stack so the user doesn't return to the intro screen
                // when pressing back from HomeActivity.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                finish(); // Finish the intro activity so it's removed from the back stack
            }
        });
    }
}