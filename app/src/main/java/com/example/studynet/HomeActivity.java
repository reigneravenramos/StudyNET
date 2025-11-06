package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements OnNavigateListener {

    final FragmentManager fm = getSupportFragmentManager();
    final Fragment homeFragment = new HomeFragment();
    final Fragment sessionsFragment = new SessionsFragment();
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

        fm.beginTransaction().add(R.id.fragment_container, quizzesFragment, "quizzes").hide(quizzesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, sessionsFragment, "sessions").hide(sessionsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fm.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;
                return true;
            } else if (itemId == R.id.nav_sessions) {
                fm.beginTransaction().hide(activeFragment).show(sessionsFragment).commit();
                activeFragment = sessionsFragment;
                return true;
            } else if (itemId == R.id.nav_quizzes) {
                fm.beginTransaction().hide(activeFragment).show(quizzesFragment).commit();
                activeFragment = quizzesFragment;
                return true;
            }
            return false;
        });
    }

    @Override
    public void navigateToSessions() {
        bottomNavigationView.setSelectedItemId(R.id.nav_sessions);
    }

    @Override
    public void navigateToQuizzes() {
        bottomNavigationView.setSelectedItemId(R.id.nav_quizzes);
    }
}