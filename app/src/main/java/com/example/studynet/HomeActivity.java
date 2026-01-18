package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements OnNavigateListener {

    public static final String NAVIGATE_TO_FRAGMENT_EXTRA = "NAVIGATE_TO_FRAGMENT";
    public static final String FRAGMENT_MODULES = "MODULES";
    public static final String FRAGMENT_QUIZZES = "QUIZZES";

    private FragmentManager fm;
    private Fragment homeFragment;
    private Fragment modulesFragment;
    private Fragment quizzesFragment;
    private Fragment activeFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("StudyNet");
        }

        fm = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            // First time initialization - Create and add fragments
            homeFragment = new HomeFragment();
            modulesFragment = new ModulesFragment();
            quizzesFragment = new QuizzesFragment();

            fm.beginTransaction().add(R.id.fragment_container, quizzesFragment, "quizzes").hide(quizzesFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, modulesFragment, "modules").hide(modulesFragment).commit();
            fm.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();
            
            activeFragment = homeFragment;
        } else {
            // Restore existing fragments to prevent duplication
            homeFragment = fm.findFragmentByTag("home");
            modulesFragment = fm.findFragmentByTag("modules");
            quizzesFragment = fm.findFragmentByTag("quizzes");
            
            // Re-determine which fragment was active
            if (homeFragment != null && !homeFragment.isHidden()) {
                activeFragment = homeFragment;
            } else if (modulesFragment != null && !modulesFragment.isHidden()) {
                activeFragment = modulesFragment;
            } else if (quizzesFragment != null && !quizzesFragment.isHidden()) {
                activeFragment = quizzesFragment;
            } else {
                activeFragment = homeFragment;
            }
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment nextFragment = null;
            
            if (itemId == R.id.nav_home) nextFragment = homeFragment;
            else if (itemId == R.id.nav_modules) nextFragment = modulesFragment;
            else if (itemId == R.id.nav_quizzes) nextFragment = quizzesFragment;

            if (nextFragment != null && nextFragment != activeFragment) {
                fm.beginTransaction().hide(activeFragment).show(nextFragment).commit();
                activeFragment = nextFragment;
                return true;
            }
            return itemId == R.id.nav_home || itemId == R.id.nav_modules || itemId == R.id.nav_quizzes;
        });

        // Handle navigation if started from Intro activities
        handleIntentNavigation(getIntent());
    }

    private void handleIntentNavigation(Intent intent) {
        if (intent != null && intent.hasExtra(NAVIGATE_TO_FRAGMENT_EXTRA)) {
            String fragmentToLoad = intent.getStringExtra(NAVIGATE_TO_FRAGMENT_EXTRA);
            Fragment targetFragment = null;
            int navId = -1;

            if (FRAGMENT_MODULES.equals(fragmentToLoad)) {
                targetFragment = modulesFragment;
                navId = R.id.nav_modules;
            } else if (FRAGMENT_QUIZZES.equals(fragmentToLoad)) {
                targetFragment = quizzesFragment;
                navId = R.id.nav_quizzes;
            }

            if (targetFragment != null && targetFragment != activeFragment) {
                fm.beginTransaction().hide(activeFragment).show(targetFragment).commit();
                activeFragment = targetFragment;
                bottomNavigationView.setSelectedItemId(navId);
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