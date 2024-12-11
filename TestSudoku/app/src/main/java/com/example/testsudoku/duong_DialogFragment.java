package com.example.testsudoku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class duong_DialogFragment extends androidx.fragment.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want reload game?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    reloadFragment();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }

    private void reloadFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment homeFragment = duong_HomeFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, homeFragment)
                    .commit();
        }
    }
}
