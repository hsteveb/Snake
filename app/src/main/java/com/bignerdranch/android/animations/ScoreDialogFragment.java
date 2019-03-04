package com.bignerdranch.android.animations;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/* This fragment pops up whenever you lose the game and you want to save your name and score
 */
public class ScoreDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setView(R.layout.save_score_dialog_fragment).create();
    }
}
