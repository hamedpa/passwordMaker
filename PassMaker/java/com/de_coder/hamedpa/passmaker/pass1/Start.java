package com.de_coder.hamedpa.passmaker.pass1;
//Developed by Hamedpa

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.de_coder.hamedpa.passmaker.R;

public class Start extends Activity {

    TextView text_lower_case_left, text_lower_case_right, text_upper_case_left, text_upper_case_right,
            text_number_left, text_number_right, text_special_left, text_special_right,
            text_digits_left, text_digits_more, text_digits_less, this_password, text_generate;

    ImageView symbol_this_copy;

    GenPwd pwd = new GenPwd();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        /*
         *
         *  LOWER CASE SELECTOR
         *
         *
         */
        final TextView low_l = (TextView)findViewById(R.id.text_lower_case_left);
        final TextView low_r = (TextView)findViewById(R.id.text_lower_case_right);
        low_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(low_r.isSelected() == true){
                    low_l.setBackgroundColor(0xFFFFFFFF);
                    low_r.setSelected(false);
                    pwd.setLower(false);
                } else {
                    low_l.setBackgroundColor(0x9999cc00);
                    low_r.setSelected(true);
                    pwd.setLower(true);
                }
            }
        });

        low_r.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    low_r.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    low_r.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  UPPER CASE SELECTOR
         *
         *
         */
        final TextView up_l = (TextView)findViewById(R.id.text_upper_case_left);
        final TextView up_r = (TextView)findViewById(R.id.text_upper_case_right);
        up_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(up_r.isSelected() == true){
                    up_l.setBackgroundColor(0xFFFFFFFF);
                    up_r.setSelected(false);
                    pwd.setUpper(false);
                } else {
                    up_l.setBackgroundColor(0x9999cc00);
                    up_r.setSelected(true);
                    pwd.setUpper(true);
                }
            }
        });

        up_r.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    up_r.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    up_r.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  NUMBERS SELECTOR
         *
         *
         */
        final TextView n_l = (TextView)findViewById(R.id.text_number_left);
        final TextView n_r = (TextView)findViewById(R.id.text_number_right);
        n_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(n_r.isSelected() == true){
                    n_l.setBackgroundColor(0xFFFFFFFF);
                    n_r.setSelected(false);
                    pwd.setNumber(false);
                } else {
                    n_l.setBackgroundColor(0x9999cc00);
                    n_r.setSelected(true);
                    pwd.setNumber(true);
                }
            }
        });

        n_r.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    n_r.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    n_r.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  SPECIAL SELECTOR
         *
         *
         */
        final TextView s_l = (TextView)findViewById(R.id.text_special_left);
        final TextView s_r = (TextView)findViewById(R.id.text_special_right);
        s_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(s_r.isSelected() == true){
                    s_l.setBackgroundColor(0xFFFFFFFF);
                    s_r.setSelected(false);
                    pwd.setSpecialchar(false);
                } else {
                    s_l.setBackgroundColor(0x9999cc00);
                    s_r.setSelected(true);
                    pwd.setSpecialchar(true);
                }
            }
        });

        s_r.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    s_r.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    s_r.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  DIGIT SELECTOR
         *
         *
         */
        final TextView dig_l = (TextView)findViewById(R.id.text_digits_left);
        final TextView dig_plus = (TextView)findViewById(R.id.text_digits_more);
        final TextView dig_minus = (TextView)findViewById(R.id.text_digits_less);
        dig_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pwd.getDigits() == 32) {
                    pwd.setDigits(32);
                } else {
                    int phold = pwd.getDigits();
                    phold++;
                    pwd.setDigits(phold);
                }
                dig_l.setText(""+pwd.getDigits());
            }
        });

        dig_plus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dig_plus.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dig_plus.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        dig_minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pwd.getDigits() == 0) {
                    pwd.setDigits(0);
                } else {
                    int mhold = pwd.getDigits();
                    mhold--;
                    pwd.setDigits(mhold);
                }
                dig_l.setText(""+pwd.getDigits());
            }
        });

        dig_minus.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    dig_minus.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    dig_minus.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  FINAL GENERATE
         *
         *
         */
        final TextView gen = (TextView)findViewById(R.id.text_generate);
        final TextView pwd_cp = (TextView)findViewById(R.id.this_password);
        gen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pwd.isLower() == false && pwd.isUpper() == false && pwd.isSpecialchar() == false && pwd.isNumber() == false) {
                    Toast.makeText(getApplicationContext(), "Please choose minimum one option", Toast.LENGTH_SHORT).show();
                    pwd_cp.setText("");
                } else {
                    String pwdoutput = pwd.generatePassword();
                    pwd_cp.setText(pwdoutput);
                }
            }
        });

        gen.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    gen.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    gen.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        /*
         *
         *  COPY PASTE
         *
         *
         */
        final ImageView copy = (ImageView)findViewById(R.id.symbol_this_copy);
        copy.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    copy.setBackgroundColor(0x9999cc00);
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    copy.setBackgroundColor(0xFFFFFFFF);
                }
                return false;
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("pwd_cp",pwd_cp.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Saved to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
