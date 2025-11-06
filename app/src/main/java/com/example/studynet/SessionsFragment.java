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

public class SessionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        Button btn_sessions1 = view.findViewById(R.id.btn_sessions1);
        btn_sessions1.setOnClickListener(v -> startActivity(new Intent(getActivity(), Sessions1.class)));

        Button btn_sessions2 = view.findViewById(R.id.btn_sessions2);
        btn_sessions2.setOnClickListener(v -> startActivity(new Intent(getActivity(), Sessions2.class)));

        Button btn_sessions3 = view.findViewById(R.id.btn_sessions3);
        btn_sessions3.setOnClickListener(v -> startActivity(new Intent(getActivity(), Sessions3.class)));

        Button btn_sessions4 = view.findViewById(R.id.btn_sessions4);
        btn_sessions4.setOnClickListener(v -> startActivity(new Intent(getActivity(), Sessions4.class)));

        Button btn_sessions5 = view.findViewById(R.id.btn_sessions5);
        btn_sessions5.setOnClickListener(v -> startActivity(new Intent(getActivity(), Sessions5.class)));

        return view;
    }
}