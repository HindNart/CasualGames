package com.example.testsudoku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class duong_HomeFragment extends Fragment {
    private Integer Point = 0;
    private Integer PointTemp = 0;
    private MediaPlayer mediaPlayer;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    public duong_HomeFragment() {}

    public static duong_HomeFragment newInstance(String param1, String param2) {
        duong_HomeFragment fragment = new duong_HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.duong_fragment_home, container, false);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.correct);
        TextView pointView = view.findViewById(R.id.point);

        ImageButton backToMainMenu = view.findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Menu.class);
                startActivity(i);
            }
        });

        int[] fruits = {
                R.drawable.duong_cap1, R.drawable.duong_cap2, R.drawable.duong_cap3, R.drawable.duong_cap4,
                R.drawable.duong_cap5, R.drawable.duong_cap6, R.drawable.duong_cap7, R.drawable.duong_cap8,
                R.drawable.duong_cap9, R.drawable.duong_cap10, R.drawable.duong_cap11, R.drawable.duong_cap12,
                R.drawable.duong_cap13, R.drawable.duong_cap14, R.drawable.duong_cap15, R.drawable.duong_cap16, R.drawable.duong_cap17
        };
        Point = 0;
        AtomicReference<GridLayout> gridLayout = new AtomicReference<>(view.findViewById(R.id.gridLayout));
        for (int items = 0; items < 20; items++) {
            ImageView imageView = new ImageView(getContext());
            int tagItem = new Random().nextInt(3);
            imageView.setImageResource(fruits[tagItem]);
            imageView.setPadding(10, 10, 10, 10);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            imageView.setTag(tagItem);

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = 0;
            layoutParams.height = 0;
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            imageView.setLayoutParams(layoutParams);
            gridLayout.get().addView(imageView);

            imageView.setOnTouchListener((view1, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view1);
                    view1.startDragAndDrop(null, shadowBuilder, view1, 0);
                    return true;
                }
                return false;
            });

            nhdSqlHelperClass nhdSqlHelperClass = new nhdSqlHelperClass(this.getContext(), "game.db", null, 1);

            imageView.setOnDragListener((v, event) -> {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    View draggedView = (View) event.getLocalState();
                    if (v == draggedView) return false;

                    ImageView draggedImageView = (ImageView) draggedView;
                    ImageView targetImageView = (ImageView) v;

                    int draggedImageViewTag = (Integer) draggedImageView.getTag();
                    int targetImageViewTag = (Integer) targetImageView.getTag();

                    GridLayout grid = (GridLayout) v.getParent();
                    int draggedIndex = grid.indexOfChild(draggedImageView);
                    int targetIndex = grid.indexOfChild(targetImageView);
                    int columnCount = grid.getColumnCount();

                    boolean isHorizontalMove = (targetIndex == draggedIndex - 1 || targetIndex == draggedIndex + 1) &&
                            (draggedIndex / columnCount == targetIndex / columnCount);
                    boolean isVerticalMove = (targetIndex == draggedIndex - columnCount || targetIndex == draggedIndex + columnCount);


                    List<Integer> CheckList = nhdSqlHelperClass.getSelectedChecks();
                    int scoreMultiplier = 1; // Default multiplier
                    if (targetImageViewTag == 5) {
                        if (CheckList.contains(1)) {
                            scoreMultiplier *= 2; // x2 if 1 is in the list
                        }
                        if (CheckList.contains(4)) {
                            scoreMultiplier *= 3; // x3 if 4 is in the list
                        }
                    }

                    if (targetImageViewTag == 6) {
                        if (CheckList.contains(2)) {
                            scoreMultiplier *= 2; // x2 if 2 is in the list
                        }
                        if (CheckList.contains(5)) {
                            scoreMultiplier *= 3; // x3 if 5 is in the list
                        }
                    }

                    if (targetImageViewTag == 7) {
                        if (CheckList.contains(3)) {
                            scoreMultiplier *= 2; // x2 if 3 is in the list
                        }
                        if (CheckList.contains(6)) {
                            scoreMultiplier *= 3; // x3 if 6 is in the list
                        }
                    }

                    if (isHorizontalMove || isVerticalMove) {
                        if (draggedImageViewTag == targetImageViewTag) {
                            if (draggedImageViewTag == targetImageViewTag && draggedImageViewTag == 0) {
                                playCorrectSound();
                                targetImageView.setImageResource(fruits[targetImageViewTag + 1]);
                                targetImageView.setTag(targetImageViewTag + 1);
                                Point += ((targetImageViewTag + 1) * 10) * scoreMultiplier;
                                Log.d("Math", String.valueOf(scoreMultiplier) + String.valueOf(targetImageViewTag));
                                draggedImageView.setImageResource(fruits[0]);
                                draggedImageView.setTag(0);
                                pointView.setText(Point + (Point > 1 ? " Points" : " Point"));
                                return true;
                            }

                            if (draggedImageViewTag == targetImageViewTag) {
                                playCorrectSound();
                                targetImageView.setImageResource(fruits[targetImageViewTag + 1]);
                                targetImageView.setTag(targetImageViewTag + 1);
                                Point += ((targetImageViewTag + 1) * 10) * scoreMultiplier;
                                int tagItem1 = new Random().nextInt(targetImageViewTag);
                                draggedImageView.setImageResource(fruits[tagItem1]);
                                pointView.setText(Point + (Point > 1 ? " Points" : " Point"));
                                draggedImageView.setTag(tagItem1);
                                return true;
                            }
                        }
                    }
                }
                nhdSqlHelperClass.updatePoint(Point);
                Log.d("Test", String.valueOf(Point));
                return true;
            });
        }
        ImageButton btnReload = view.findViewById(R.id.regame);
        btnReload.setOnClickListener(v ->  {
            resultScore(Point);
            duong_DialogFragment dialog = new duong_DialogFragment();
            dialog.show(getParentFragmentManager(), "DialogFragment");
        });

        return view;
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

    private void playCorrectSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.seekTo(0);
        }
    }
}
