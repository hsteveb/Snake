package com.bignerdranch.android.animations;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/* This fragment pops up whenever you lose the game and you want to save your name and score
 */
public class SaveScoreDialogFragment extends DialogFragment{

    private final String TAG = "SaveScoreDialogFragment";
    private static final String mpScore = "Score";


    private EditText mplayerName;
    private TextView mplayerScore;

    private int score;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
            score = getArguments().getInt(mpScore);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.save_score_dialog_fragment, null,false);
        mplayerScore = (TextView) v.findViewById(R.id.save_score_show_score_dialog_fragment);
        mplayerScore.setText(String.format("Score: %d", score));
        mplayerName = (EditText) v.findViewById(R.id.save_score_player_name_dialog_fragment);

        mplayerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.high_score);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.cancel,null);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    public static SaveScoreDialogFragment create(int score)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(mpScore, score);

        SaveScoreDialogFragment fragment = new SaveScoreDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

}
