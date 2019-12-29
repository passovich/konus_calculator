package com.blogspot.passovich3;


import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "myLogs";

    private EditText DText,dText,LText;
    private EditText aDegText,aMinText,aSecText;
    private EditText ADegText,AMinText,ASecText;
    private Resources res;

    private double L = 0, D = 0, d = 0, angle = 0;
    private int deg, min, sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DText = (EditText) findViewById (R.id.editText);
        dText = (EditText) findViewById (R.id.editText2);
        LText = (EditText) findViewById (R.id.editText3);

        ADegText = (EditText) findViewById (R.id.A_deg);
        aDegText = (EditText) findViewById (R.id.a_deg);
        AMinText = (EditText) findViewById (R.id.A_min);
        aMinText = (EditText) findViewById (R.id.a_min);
        ASecText = (EditText) findViewById (R.id.A_sec);
        aSecText = (EditText) findViewById (R.id.a_sec);

        res = getResources();
    }

    @Override
    public void onClick(View view){
        //Animation anim =AnimationUtils.loadAnimation(this,R.anim.translate);
        resetInputs();
        switch(view.getId()) {
            case R.id.calc_angle:
                calculateAngle();
                break;
            case R.id.calc_D:
                calculateD();
                break;
            case R.id.calc_d:
                calculated();
                break;
            case R.id.calc_L:
                calculateL();
                break;
            case R.id.exit:
                finish();
                break;
        }
    }

    public void calculateAngle(){
        if (!getD()) return;
        if (!getd()) return;
        if (!getL()) return;
        if (!checkDandd()) return;
        //Расчёт
        angle = Math.toDegrees( Math.atan( ( ( D / 2 ) - ( d / 2 ) ) / L ) );
        double temp = (angle % 1) * 0.6;
        deg = (int) (angle - temp);
        min = (int) ((temp * 100) - (temp * 100) % 1);
        sec = (int) ((((temp * 100) % 1) * 0.6) * 100 - ((((temp * 100) % 1) * 0.6) * 100) % 1);

        aDegText.setText(""+deg);
        aMinText.setText(""+min);
        aSecText.setText(""+sec);
        setDoubleAngle();
    }

    public void calculateD(){
        if ( !getd() ) return;
        if ( !getAngle() ) return;
        if ( !getL() ) return;
        angle = getDecimalAngle(deg,min,sec);
        D =( (L * Math.tan( Math.toRadians( angle ) )) * 2 ) + d;
        DText.setText(String.format("%.4f", D).replaceAll("[,.]", "."));
        setDoubleAngle();
    }

    public void calculated() {
        if (!getD()) return;
        if (!getAngle()) return;
        if (!getL()) return;
        angle = getDecimalAngle(deg, min, sec);
        double a = (L * Math.sin(Math.toRadians(angle))) / Math.sin(Math.toRadians(90.0f - angle));
        d = D - a * 2;
        dText.setText( String.format("%.4f", d).replaceAll("[,.]", ".") );
        setDoubleAngle();
    }

    public void calculateL() {
        if (!getD()) return;
        if (!getd()) return;
        if (!getAngle()) return;
        if (!checkDandd()) return;
        //расчёт
        angle = getDecimalAngle(deg, min, sec);
        L = ((D - d) / 2.0f) / Math.tan(Math.toRadians(angle));
        LText.setText(String.format("%.4f", L).replaceAll("[,.]", "."));
        setDoubleAngle();
    }

    public void showErrorDialog(String message) {
        ErrorDialog errorDialog = new ErrorDialog();
        errorDialog.context = this;
        errorDialog.setText(message);
        errorDialog.show(getFragmentManager(), "no_saved_game_dialog");
    }

    private boolean getD() {
        Resources res = getResources();
        try {
            D = Double.parseDouble(DText.getText().toString());
        } catch (Exception e) {
            showErrorDialog(res.getString(R.string.wrong_D));
            return false;
        }
        if (!checkD()) {
            D = 0;
            showErrorDialog(res.getString(R.string.wrong_D));
            return false;
        }
        return true;
    }

    private boolean checkD() {
        if (D <= 0) {
            D = 0;
            return false;
        }
        return true;
    }

    private boolean getd() {
        try {
            d = Double.parseDouble(dText.getText().toString());
        } catch (Exception e) {
            showErrorDialog(res.getString(R.string.wrong_d));
            return false;
        }
        if (!checkd()) {
            d = 0;
            showErrorDialog(res.getString(R.string.wrong_d));
            return false;
        }
        return true;
    }

    private boolean checkd() {
        if (d < 0) {
            return false;
        }
        return true;
    }

    private boolean getL() {
        try {
            this.L = Double.parseDouble(LText.getText().toString());
        } catch (Exception e) {
            showErrorDialog(res.getString(R.string.wrong_L));
            return false;
        }
        if (!checkL()) {
            L = 0;
            showErrorDialog(res.getString(R.string.wrong_d));
            return false;
        }
        return true;
    }

    private boolean checkL() {
        if (L <= 0) {
            L = 0;
            showErrorDialog(res.getString(R.string.L_notZero));
            return false;
        }
        return true;
    }

    private boolean getAngle() {
        try {
            deg = Integer.parseInt(aDegText.getText().toString());
            try {
                min = Integer.parseInt(aMinText.getText().toString());
                try {
                    sec = Integer.parseInt(aSecText.getText().toString());
                } catch (Exception e) {
                    showErrorDialog(res.getString(R.string.wrong_A));
                    return false;
                }
            } catch (Exception e) {
                showErrorDialog(res.getString(R.string.wrong_A));
                return false;
            }
        } catch (Exception e) {
            showErrorDialog(res.getString(R.string.wrong_A));
            return false;
        }
        if (!checkAngle()) {
            deg = 0;
            min = 0;
            sec = 0;
            return false;
        }
        return true;
    }

    private boolean checkAngle() {
        if (deg >= 90 || min > 60 || min < 0 || sec > 60 || sec < 0) {
            showErrorDialog(res.getString(R.string.wrong_A));
            return false;
        }
        return true;
    }

    private boolean checkDandd() {
        if (D < d) {
            showErrorDialog(res.getString(R.string.wrong_D_less_d));
            return false;
        }
        return true;
    }

    private void setDoubleAngle() {
        double deg = 0, min = 0, sec = 0;
        double temp = 0;
        temp = (this.deg * 3600 + this.min * 60 + this.sec) * 2;
        deg = (temp - (temp % 3600)) / 3600;
        temp = temp - deg * 3600;
        min = (temp - (temp % 60)) / 60;
        sec = temp - min * 60;

        ADegText.setText(String.format("%.0f", deg));
        AMinText.setText(String.format("%.0f", min));
        ASecText.setText(String.format("%.0f", sec));
    }

    private void resetInputs(){ L = 0; D = 0; d = 0; angle = 0;}

    private double getDecimalAngle(double deg, double min, double sec) {
        double decimalSec = (min * 60.0f + sec) / 3600.0f;
        double result = deg + decimalSec;
        return result;
    }
}

