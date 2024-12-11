package com.example.testsudoku;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.testsudoku.databinding.DuongActivityPageClickBinding;

public class duong_PageClickActivity extends AppCompatActivity {
    DuongActivityPageClickBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DuongActivityPageClickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new duong_HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new duong_HomeFragment());
            } else if (itemId == R.id.push) {
                duong_DialogMainFragment dialogFragment = new duong_DialogMainFragment();
                dialogFragment.show(getSupportFragmentManager(), "DialogFragment");
            }
            return true;
        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameHome.getId(), fragment);
        fragmentTransaction.commit();
    }
}