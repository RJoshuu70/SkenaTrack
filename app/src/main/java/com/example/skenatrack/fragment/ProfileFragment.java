package com.example.skenatrack.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.skenatrack.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        TextView  tvDevName  = view.findViewById(R.id.tvDevName);
        TextView  tvNim      = view.findViewById(R.id.tvNim);
        TextView  tvGithub   = view.findViewById(R.id.tvGithub);
        TextView  tvInstagram = view.findViewById(R.id.tvInstagram);
        TextView  tvEmail    = view.findViewById(R.id.tvEmail);

        imgProfile.setImageResource(R.drawable.profile);
        tvDevName.setText("Kelompok 2");
        tvNim.setText("001, 009, 023, 029, 032");
        tvGithub.setText("github.com/RJoshuu70");
        tvInstagram.setText("@aduhputbool");
        tvEmail.setText("NIM@mahasiswa.upnvj.ac.id");
    }
}
