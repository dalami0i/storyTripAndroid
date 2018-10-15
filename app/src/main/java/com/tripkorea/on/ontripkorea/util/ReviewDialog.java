package com.tripkorea.on.ontripkorea.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tripkorea.on.ontripkorea.R;

public class ReviewDialog extends Dialog  {
    private Context context;


    public ReviewDialog(@NonNull Context context) {
        super(context);
        new LogManager().LogManager("ReviewDialog","ReviewDialog(@NonNull Context context) 진입");
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review);
        new LogManager().LogManager("ReviewDialog","onCreate 진입");

    }


}
