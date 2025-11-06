package com.example.studynet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuizzesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        Button btn_quizzes1 = view.findViewById(R.id.btn_quizzes1);
        btn_quizzes1.setOnClickListener(v -> startActivity(new Intent(getActivity(), Quizzes1.class)));

        Button btn_quizzes2 = view.findViewById(R.id.btn_quizzes2);
        btn_quizzes2.setOnClickListener(v -> startActivity(new Intent(getActivity(), Quizzes2.class)));

        Button btn_quizzes3 = view.findViewById(R.id.btn_quizzes3);
        btn_quizzes3.setOnClickListener(v -> startActivity(new Intent(getActivity(), Quizzes3.class)));

        Button btn_quizzes4 = view.findViewById(R.id.btn_quizzes4);
        btn_quizzes4.setOnClickListener(v -> startActivity(new Intent(getActivity(), Quizzes4.class)));

        Button btn_quizzes5 = view.findViewById(R.id.btn_quizzes5);
        btn_quizzes5.setOnClickListener(v -> startActivity(new Intent(getActivity(), Quizzes5.class)));

        return view;
    }
}