package com.blogspot.passovich3;


import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ErrorDialog extends DialogFragment implements View.OnClickListener {
    private Button btnYes;
    private TextView textView;
    private String text = "Чёта пошло не так";
    public Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Resources res = getResources();
        getDialog().setTitle(res.getString(R.string.warning_header));
        View v = inflater.inflate(R.layout.dialog_no_saved_game,null);
        btnYes = (Button) v.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(this);
        btnYes.setText("ОК");
        textView = (TextView) v.findViewById(R.id.textView1);
        textView.setText(text);
        return v;
    }
    public void setText(String text){
        this.text = text;
   //     textView.setText(this.text);
    }
    @Override
    public void onClick(View v) {
        Animation anim = AnimationUtils.loadAnimation(context,R.anim.translate);
        switch (v.getId()){
            case R.id.btnYes:
                btnYes.startAnimation(anim);
                break;
        }
        dismiss();
    }
}
