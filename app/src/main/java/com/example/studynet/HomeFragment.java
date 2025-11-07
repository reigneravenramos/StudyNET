package com.example.studynet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private OnNavigateListener onNavigateListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigateListener) {
            onNavigateListener = (OnNavigateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnNavigateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // *** FIX: Revert to launching Intro Activities as requested. ***
        Button btnModules = view.findViewById(R.id.btn_modules);
        btnModules.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ModulesIntroActivity.class);
            startActivity(intent);
        });

        Button btnQuizzes = view.findViewById(R.id.btn_quizzes);
        btnQuizzes.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizzesIntroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onNavigateListener = null;
    }
}