package com.example.skenatrack.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.skenatrack.fragment.AboutAppFragment;
import com.example.skenatrack.fragment.ProfileFragment;

public class AboutPagerAdapter extends FragmentStateAdapter {

    public AboutPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:  return new AboutAppFragment();
            case 1:  return new ProfileFragment();
            default: return new AboutAppFragment();
        }
    }
}
