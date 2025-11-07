package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements OnNavigateListener {

    // Define the constants used in the Intro Activities (for clarity, they should be in a single utility class or defined here)
    public static final String NAVIGATE_TO_FRAGMENT_EXTRA = "NAVIGATE_TO_FRAGMENT";
    public static final String FRAGMENT_MODULES = "MODULES";
    public static final String FRAGMENT_QUIZZES = "QUIZZES";


    final FragmentManager fm = getSupportFragmentManager();
    final Fragment homeFragment = new HomeFragment();
    final Fragment modulesFragment = new ModulesFragment();
    final Fragment quizzesFragment = new QuizzesFragment();
    Fragment activeFragment = homeFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("StudyNet");
        }

        // Initialize fragments (ensure homeFragment is added last and shown initially)
        fm.beginTransaction().add(R.id.fragment_container, quizzesFragment, "quizzes").hide(quizzesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, modulesFragment, "modules").hide(modulesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fm.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                return true;
            } else if (itemId == R.id.nav_modules) {
                fm.beginTransaction().hide(activeFragment).show(modulesFragment).commit();
                activeFragment = modulesFragment;
                return true;
            } else if (itemId == R.id.nav_quizzes) {
                fm.beginTransaction().hide(activeFragment).show(quizzesFragment).commit();
                activeFragment = quizzesFragment;
                return true;
            }
            return false;
        });

        // *** NEW LOGIC: Check if launched from an Intro Activity ***
        handleIntentNavigation(getIntent());
    }

    private void handleIntentNavigation(Intent intent) {
        if (intent != null && intent.hasExtra(NAVIGATE_TO_FRAGMENT_EXTRA)) {
            String fragmentToLoad = intent.getStringExtra(NAVIGATE_TO_FRAGMENT_EXTRA);

            if (FRAGMENT_MODULES.equals(fragmentToLoad)) {
                // Navigate to Modules Fragment
                fm.beginTransaction().hide(activeFragment).show(modulesFragment).commit();
                activeFragment = modulesFragment;
                bottomNavigationView.setSelectedItemId(R.id.nav_modules);
            } else if (FRAGMENT_QUIZZES.equals(fragmentToLoad)) {
                // Navigate to Quizzes Fragment
                fm.beginTransaction().hide(activeFragment).show(quizzesFragment).commit();
                activeFragment = quizzesFragment;
                bottomNavigationView.setSelectedItemId(R.id.nav_quizzes);
            }
        }
    }

    @Override
    public void navigateToModules() {
        bottomNavigationView.setSelectedItemId(R.id.nav_modules);
    }

    @Override
    public void navigateToQuizzes() {
        bottomNavigationView.setSelectedItemId(R.id.nav_quizzes);
    }
}