package com.example.testsudoku;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link duong_PushFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class duong_PushFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView tvScore;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    nhdSqlHelperClass nhdSqlHelperClass;
    public duong_PushFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PushFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static duong_PushFragment newInstance(String param1, String param2) {
        duong_PushFragment fragment = new duong_PushFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nhdSqlHelperClass = new nhdSqlHelperClass(this.getContext(), "game.db",null,1);
        View view = inflater.inflate(R.layout.duong_fragment_push, container, false);
        String score = String.valueOf(nhdSqlHelperClass.getScore());
        tvScore = view.findViewById(R.id.tvScore);
        tvScore.setText(String.valueOf(score) + " Credits");

        ImageButton buyCucumx2 = view.findViewById(R.id.buyCucumx2);
        ImageButton buyCucumx3 = view.findViewById(R.id.buyCucumx3);
        ImageButton buyPineapplesx2 = view.findViewById(R.id.buyPineapplesx2);
        ImageButton buyPineapplesx3 = view.findViewById(R.id.buyPineapplesx3);
        ImageButton buyKiwix2 = view.findViewById(R.id.buyKiwix2);
        ImageButton buyKiwix3 = view.findViewById(R.id.buyKiwix3);
        ImageButton buyDoublePoint = view.findViewById(R.id.buyDoublePoint);

//      1 buyCucumx2 | 2 buyCucumx3 | 3 buyPineapplesx2 | 4 buyPineapplesx3 | 5 buyKiwix2 | 6 buyKiwix3 | 7 buyDoublePoint

        ImageButton[] buttons = {
                buyCucumx2, buyCucumx3, buyPineapplesx2, buyPineapplesx3, buyKiwix2, buyKiwix3, buyDoublePoint
        };
// tạo if else để nếu mua thì sẽ giảm, nếu nhỏ hơn không thì không thể giảm
        View.OnClickListener listener = view1 -> {
            int id = view1.getId();
            int Score = nhdSqlHelperClass.getScore();
            if (Score > 0){
                if (id == R.id.buyCucumx2) {
                    if (Score > 10 ){
                        nhdSqlHelperClass.updateChecks(1);
                        nhdSqlHelperClass.updateScore(Score - 10);
                        buyCucumx3.setEnabled(false);
                        buyCucumx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyCucumx2.setEnabled(false);
                        buyCucumx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");
                    }
                } else if (id == R.id.buyCucumx3) {
                    if (Score > 15){
                        nhdSqlHelperClass.updateChecks(2);
                        nhdSqlHelperClass.updateScore(Score - 20);
                        buyCucumx2.setEnabled(false);
                        buyCucumx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyCucumx3.setEnabled(false);
                        buyCucumx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }
                } else if (id == R.id.buyPineapplesx2) {
                    if (Score > 30){
                        nhdSqlHelperClass.updateChecks(3);
                        nhdSqlHelperClass.updateScore(Score - 30);
                        buyPineapplesx2.setEnabled(false);
                        buyPineapplesx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyPineapplesx3.setEnabled(false);
                        buyPineapplesx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }
                } else if (id == R.id.buyPineapplesx3) {
                    if (Score > 15){
                        nhdSqlHelperClass.updateChecks(4);
                        nhdSqlHelperClass.updateScore(Score - 15);
                        buyPineapplesx2.setEnabled(false);
                        buyPineapplesx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyPineapplesx3.setEnabled(false);
                        buyPineapplesx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }
                } else if (id == R.id.buyKiwix2) {
                    if (Score > 35){
                        nhdSqlHelperClass.updateChecks(5);
                        nhdSqlHelperClass.updateScore(Score - 35);
                        buyKiwix2.setEnabled(false);
                        buyKiwix2.setImageResource(R.drawable.duong_push_buy_off);
                        buyKiwix3.setEnabled(false);
                        buyKiwix3.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }

                } else if (id == R.id.buyKiwix3) {
                    if (Score > 45){
                        nhdSqlHelperClass.updateChecks(6);
                        nhdSqlHelperClass.updateScore(Score - 45);
                        buyKiwix2.setEnabled(false);
                        buyKiwix2.setImageResource(R.drawable.duong_push_buy_off);
                        buyKiwix3.setEnabled(false);
                        buyKiwix3.setImageResource(R.drawable.duong_push_buy_off);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }
                } else if (id == R.id.buyDoublePoint) {
                    if (Score > 100){
                        nhdSqlHelperClass.updateChecks(7);
                        nhdSqlHelperClass.updateScore(Score - 100);
                        buyDoublePoint.setEnabled(false);
                        buyDoublePoint.setImageResource(R.drawable.duong_push_buy_off);
                        buyCucumx3.setEnabled(false);
                        buyCucumx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyCucumx2.setEnabled(false);
                        buyCucumx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyPineapplesx3.setEnabled(false);
                        buyPineapplesx3.setImageResource(R.drawable.duong_push_buy_off);
                        buyPineapplesx2.setEnabled(false);
                        buyPineapplesx2.setImageResource(R.drawable.duong_push_buy_off);
                        buyKiwix3.setEnabled(false);
                        buyKiwix3.setImageResource(R.drawable.duong_push_buy_off);
                        buyKiwix2.setEnabled(false);
                        buyKiwix2.setImageResource(R.drawable.duong_push_buy_off);
                        tvScore.setText(String.valueOf(Score) + " Credits");

                    }
                }
            }
        };

        for (ImageButton button : buttons) {
            button.setOnClickListener(listener);
        }

        return view;
    }
}
