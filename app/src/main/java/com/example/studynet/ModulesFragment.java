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

public class ModulesFragment extends Fragment { // Renamed class

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modules, container, false); // Assumes you rename fragment_sessions.xml to fragment_modules.xml

        Button btn_modules1 = view.findViewById(R.id.btn_modules1); // Assumes R.id is renamed
        btn_modules1.setOnClickListener(v -> startActivity(new Intent(getActivity(), Modules1.class))); // Renamed class

        Button btn_modules2 = view.findViewById(R.id.btn_modules2);
        btn_modules2.setOnClickListener(v -> startActivity(new Intent(getActivity(), Modules2.class)));

        Button btn_modules3 = view.findViewById(R.id.btn_modules3);
        btn_modules3.setOnClickListener(v -> startActivity(new Intent(getActivity(), Modules3.class)));

        Button btn_modules4 = view.findViewById(R.id.btn_modules4);
        btn_modules4.setOnClickListener(v -> startActivity(new Intent(getActivity(), Modules4.class)));

        Button btn_modules5 = view.findViewById(R.id.btn_modules5);
        btn_modules5.setOnClickListener(v -> startActivity(new Intent(getActivity(), Modules5.class)));

        return view;
    }
}