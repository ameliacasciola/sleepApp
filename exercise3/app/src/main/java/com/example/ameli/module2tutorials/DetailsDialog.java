package com.example.ameli.module2tutorials;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import android.widget.RatingBar;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.FileOutputStream;


public class DetailsDialog extends AppCompatDialogFragment {

    private TextView when;
    private TextView where;
    private TextView howmuch;
    private TextView tip;
    private TextView numberpeople;
    private RatingBar ratingbar;
    private DetailsDialogListener listener;

// show details of restaurant when creating dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.details_dialog_box,null);

        listener.fromFile();

        builder.setView(view)
                .setTitle("Details of the meal")
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        when = view.findViewById(R.id.when);
        where = view.findViewById(R.id.where);
        howmuch = view.findViewById(R.id.howmuch);
        tip = view.findViewById(R.id.tip);
        numberpeople = view.findViewById(R.id.numberpeople);
        ratingbar = (RatingBar)view.findViewById(R.id.ratingbar);

        when.setText(listener.getWhen_data());
        where.setText(listener.getWhere_data());
        howmuch.setText(listener.getHowmuch_data());
        tip.setText(listener.getTip_data());
        numberpeople.setText(listener.getNumberpeople_data());
        ratingbar.setRating(Integer.parseInt(listener.getRating_data()));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DetailsDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement DetailsDialogListener");
        }
    }

    public interface DetailsDialogListener {
        void fromFile();
        String getWhen_data();
        String getWhere_data();
        String getHowmuch_data();
        String getNumberpeople_data();
        String getTip_data();
        String getRating_data();
    }
}
