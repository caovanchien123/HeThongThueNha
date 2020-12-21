package com.example.hethongthuenha.PersonSetting.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hethongthuenha.R;

public class DialogProgress extends Dialog {
    TextView title_progress;
    public DialogProgress(@NonNull Context context, String title) {
        super(context);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        title_progress = findViewById(R.id.title_progress);
        title_progress.setText(title);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        getWindow().setLayout(width, height);
    }
}
