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

import java.io.DataOutputStream;
import java.io.FileOutputStream;


public class ExampleDialog extends AppCompatDialogFragment {

    private EditText when;
    private EditText where;
    private EditText howmuch;
    private EditText tip;
    private EditText numberpeople;
    private RatingBar ratingbar;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_box,null);

        builder.setView(view)
                .setTitle("Details of the meal")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String when_data = when.getText().toString();
                        String where_data = where.getText().toString();
                        String howmuch_data = howmuch.getText().toString();
                        String tip_data  = tip.getText().toString();
                        String numberpeople_data = numberpeople.getText().toString();
                        String rating =  Integer.toString((int)ratingbar.getRating());

                        listener.toFile(when_data,where_data,howmuch_data,tip_data,numberpeople_data,rating);
                    }
                });
        when = view.findViewById(R.id.when);
        where = view.findViewById(R.id.where);
        howmuch = view.findViewById(R.id.howmuch);
        tip = view.findViewById(R.id.tip);
        numberpeople = view.findViewById(R.id.numberpeople);
        ratingbar = (RatingBar)view.findViewById(R.id.ratingbar);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");

        }
    }

    public interface ExampleDialogListener {
        void toFile(String when_data, String where_data, String howmuch_data, String tip_data,
                    String numberpeople_data, String rating);

    }
}
