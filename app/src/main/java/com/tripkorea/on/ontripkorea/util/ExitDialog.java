package com.tripkorea.on.ontripkorea.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.tripkorea.on.ontripkorea.R;

public class ExitDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private Button cancelBtn;
    private Button exitBtn;

    public ExitDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit);

        cancelBtn = (Button) findViewById(R.id.btn_exit_cancel);
        cancelBtn.setOnClickListener(this);
        exitBtn = (Button) findViewById(R.id.btn_exit_exit);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_cancel:
                cancel();
                break;
            case R.id.btn_exit_exit:
                cancel();
                ((Activity)context).finish();
                break;
        }
    }
}
