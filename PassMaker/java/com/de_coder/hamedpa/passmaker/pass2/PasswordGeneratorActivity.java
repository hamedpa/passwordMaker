package com.de_coder.hamedpa.passmaker.pass2;
//Developed by Hamedpa

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.de_coder.hamedpa.passmaker.R;
import com.de_coder.hamedpa.passmaker.pass2.charactergroup.CharacterGroup;
import com.de_coder.hamedpa.passmaker.pass2.charactergroup.Lowercase;
import com.de_coder.hamedpa.passmaker.pass2.charactergroup.Numbers;
import com.de_coder.hamedpa.passmaker.pass2.charactergroup.Special;
import com.de_coder.hamedpa.passmaker.pass2.charactergroup.Uppercase;
import com.de_coder.hamedpa.passmaker.pass2.generator.PasswordGenerator;
import com.de_coder.hamedpa.passmaker.pass2.generator.PasswordGeneratorImpl;

import java.util.ArrayList;
import java.util.List;


public class PasswordGeneratorActivity extends ActionBarActivity {

    private PasswordGenerator passwordGenerator;
    private EditText passwordLengthEditText;
    private EditText generatedPasswordEditText;

    public PasswordGeneratorActivity() {
        this(new PasswordGeneratorImpl());
    }

    public PasswordGeneratorActivity(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_generator);
        findViewById(R.id.button_generate_password).requestFocus();

        passwordLengthEditText = (EditText) findViewById(R.id.password_length);
        generatedPasswordEditText = (EditText) findViewById(R.id.generated_password);

        createClickListeners();
    }

    private void createClickListeners() {
        createMinusButtonClickListener();
        createPlusButtonClickListener();
        createGeneratePasswordButtonClickListener();
    }




    private void createMinusButtonClickListener() {
        Button minusButton = (Button) findViewById(R.id.button_minus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passwordLength = getPasswordLength();
                setPasswordLength(--passwordLength);
            }
        });
    }

    private void createPlusButtonClickListener() {
        Button minusButton = (Button) findViewById(R.id.button_plus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passwordLength = getPasswordLength();
                setPasswordLength(++passwordLength);
            }
        });
    }

    private void createGeneratePasswordButtonClickListener() {
        Button generatePasswordButton = (Button) findViewById(R.id.button_generate_password);
        generatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = getPasswordLength();
                List<CharacterGroup> characterGroups = new ArrayList<>();
                addCharacterGroup(characterGroups, R.id.checkbox_uppercase, Uppercase.class);
                addCharacterGroup(characterGroups, R.id.checkbox_lowercase, Lowercase.class);
                addCharacterGroup(characterGroups, R.id.checkbox_numbers, Numbers.class);
                addCharacterGroup(characterGroups, R.id.checkbox_special, Special.class);
                String generatedPassword = getPasswordGenerator().generatePassword(length, characterGroups);
                getGeneratedPasswordEditText().setText(generatedPassword);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void addCharacterGroup(List<CharacterGroup> characterGroups, int id, Class<? extends CharacterGroup> characterGroupClass) {
        if (isChecked(id)) {
            try {
                characterGroups.add(characterGroupClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new PasswordGeneratorException(e);
            }
        }
    }

    private boolean isChecked(int id) {
        return ((CheckBox) findViewById(id)).isChecked();
    }

    int getPasswordLength() {
        return Integer.parseInt(getPasswordLengthEditText().getText().toString());
    }

    private void setPasswordLength(int passwordLength) {
        getPasswordLengthEditText().setText(String.valueOf(passwordLength));
    }

    PasswordGenerator getPasswordGenerator() {
        return passwordGenerator;
    }

    EditText getPasswordLengthEditText() {
        return passwordLengthEditText;
    }

    EditText getGeneratedPasswordEditText() {
        return generatedPasswordEditText;
    }

}
