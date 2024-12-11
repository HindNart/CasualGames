package com.example.testsudoku;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class duong_DialogMainFragment extends androidx.fragment.app.DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want go to push?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    nhdSqlHelperClass nhdSqlHelperClass = new nhdSqlHelperClass(this.getContext(), "game.db", null, 1);
                    int point = nhdSqlHelperClass.getPoint();
                    resultScore(point);
                    nextFragment();
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                });
        return builder.create();
    }

    private void nextFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment pushFragment = duong_PushFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, pushFragment)
                    .commit();
        }
    }

    private void resultScore(Integer Point){
        nhdSqlHelperClass nhdSqlHelperClass = new nhdSqlHelperClass(this.getContext(), "game.db", null, 1);
        int oldScore = nhdSqlHelperClass.getScore();
        int newScore = Point / 100;
        int updatedScore = oldScore + newScore;
        nhdSqlHelperClass.updateScore(updatedScore);
        nhdSqlHelperClass.updatePoint(0);
        Log.d("resultPointFragment", String.valueOf(Point));
        Log.d("resultScoreFragment", String.valueOf(oldScore));

    }
}
