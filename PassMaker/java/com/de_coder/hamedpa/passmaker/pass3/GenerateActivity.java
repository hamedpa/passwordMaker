package com.de_coder.hamedpa.passmaker.pass3;
//Developed by Hamedpa
//Decoder();
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.util.Log;

import com.de_coder.hamedpa.passmaker.R;

public class GenerateActivity extends Activity {

	private static final String TAG = "GenerateActivity";
	
	private static final int HISTORY_ACTIVITY = 0;
	private static final int PREFERENCES_ACTIVITY = 1;
	
	private static final int MENU_PREFERENCES = 0;
	
	private SharedPreferences sharedPrefs;
	private static final String PREFS_NAME = "Passify-Generate-Prefs";
	private static final String PREFS_PASS_LENGTH_KEY = "passLength";
	private static final String PREFS_MIXED_CASE_KEY = "mixedCase";
	private static final String PREFS_SYMBOLS_KEY = "symbols";
	private static final String PREFS_NUMBERS_KEY = "numbers";
	private static final String PREFS_LAST_PASS_KEY = "lastPass";
	
	private static final int PASS_LENGTH_DIALOG = 1;
	
	private boolean mMixedCase;
	private boolean mSymbols;
	private boolean mNumbers;
	private int mPassLength;
	private String mCurrentPass = "";
	private List<String> mHistoryList;
	
	private boolean mKeepHistory;
	private int mHistoryLimit;
	
    private Button generateBT;
	private Button copyBT;
    private Spinner passLengthSP;
	private Button historyBT;
    private TextView passTV;
    private CheckBox mixedCaseCB;
    private CheckBox symbolsCB;
    private CheckBox numbersCB;
	
	private PasswordDbAdapter mDbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.generate_activity);
		Log.d(TAG, ">>>>>> In onCreate <<<<<<");
		

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        

		mDbHelper = new PasswordDbAdapter(this);
		mDbHelper.open();

        passTV = (TextView) findViewById(R.id.passTV);
        passLengthSP = (Spinner) findViewById(R.id.passLengthSP);
		copyBT = (Button) findViewById(R.id.copyBT);
        generateBT = (Button) findViewById(R.id.generateBT);
        mixedCaseCB = (CheckBox) findViewById(R.id.mixedCaseCB);
        symbolsCB = (CheckBox) findViewById(R.id.symbolsCB);
        numbersCB = (CheckBox) findViewById(R.id.numbersCB);
		historyBT = (Button) findViewById(R.id.historyBT);
        
        SharedPreferences generatePrefs = getSharedPreferences(PREFS_NAME, 0);
        mPassLength = generatePrefs.getInt(PREFS_PASS_LENGTH_KEY, 8);
        mMixedCase = generatePrefs.getBoolean(PREFS_MIXED_CASE_KEY, true);
        mSymbols = generatePrefs.getBoolean(PREFS_SYMBOLS_KEY, false);
        mNumbers = generatePrefs.getBoolean(PREFS_NUMBERS_KEY, false);
        
        mixedCaseCB.setChecked(mMixedCase);
        symbolsCB.setChecked(mSymbols);
        numbersCB.setChecked(mNumbers);

		ArrayAdapter<CharSequence> passLengthAdapter = ArrayAdapter.createFromResource(this,
			R.array.pass_length_items, android.R.layout.simple_spinner_item);
		passLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		passLengthSP.setAdapter(passLengthAdapter);
		passLengthSP.setSelection(mPassLength-4);
        
        generateBT.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
				genPass();
        	}
        });

		copyBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ClipboardManager clipManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				clipManager.setText(mCurrentPass);
				Toast confirmCopy = Toast.makeText(getApplicationContext(), R.string.pass_copied_toast, Toast.LENGTH_SHORT);
				confirmCopy.show();
			}
		});
        
		historyBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pushHistory();
			}
		});

		passLengthSP.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				TextView selectedTV = (TextView) view;
				if (selectedTV != null) {
					int prevPassLength = mPassLength;
					mPassLength = Integer.parseInt((String) selectedTV.getText());
					if (prevPassLength != mPassLength) {
						genPass();
					}
				}
			}
			
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
        
        mixedCaseCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	public void onCheckedChanged(CompoundButton button, boolean newValue) {
        		mMixedCase = newValue;
        		genPass();
        	}
        });
        
        symbolsCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	public void onCheckedChanged(CompoundButton button, boolean newValue) {
        		mSymbols = newValue;
        		genPass();
        	}
        });
        
        numbersCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	public void onCheckedChanged(CompoundButton button, boolean newValue) {
        		mNumbers = newValue;
        		genPass();
        	}
        });
        
		final String lastPass = (String) getLastNonConfigurationInstance();
		if (lastPass == null & mCurrentPass.length() == 0) {
			Log.d(TAG, "Not setting previous pass");
        	genPass();
		} else {
			Log.d(TAG, "Setting previous pass");
			setPassword(lastPass);
		}
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem preferencesMI = menu.add(0, MENU_PREFERENCES, 0, R.string.generate_menu_preferences);
		preferencesMI.setIcon(android.R.drawable.ic_menu_preferences);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case MENU_PREFERENCES:
			pushPreferences();
            return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onResume() {
    	super.onResume();
		Log.d(TAG, ">>>>>> In onResume <<<<<<");
		

		
		if (mDbHelper.isClosed()) {
			mDbHelper.open();
		}
		

		if (mDbHelper.getCount() <= 1) {
			historyBT.setEnabled(false);
		} 
    }
    
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, ">>>>>> In onRetainNonConfigurationInstance <<<<<<");
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		return prefs.getString(PREFS_LAST_PASS_KEY, null);
	}
    
    @Override
    public void onStop() {
    	super.onStop();
		Log.d(TAG, ">>>>>> In onStop <<<<<<");		
		
    }
    
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, ">>>>>> In onPause <<<<<<");
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor prefsEditor = prefs.edit();
    	prefsEditor.putInt(PREFS_PASS_LENGTH_KEY, mPassLength);
    	prefsEditor.putBoolean(PREFS_MIXED_CASE_KEY, mMixedCase);
    	prefsEditor.putBoolean(PREFS_SYMBOLS_KEY, mSymbols);
    	prefsEditor.putBoolean(PREFS_NUMBERS_KEY, mNumbers);
		prefsEditor.putString(PREFS_LAST_PASS_KEY, mCurrentPass);
    	prefsEditor.commit();
		
		// Close the DB connection
		mDbHelper.close();
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode) {

		}
	}
	
	private void pushHistory() {
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivityForResult(intent, HISTORY_ACTIVITY);
	}
	
	private void pushPreferences() {
		Intent intent = new Intent(this, PreferencesActivity.class);
		startActivityForResult(intent, PREFERENCES_ACTIVITY);
	}

    private void genPass() {
		String newPass = PasswordGenerator.generate(mPassLength, mMixedCase, mSymbols, mNumbers);
		boolean keepHistory = sharedPrefs.getBoolean(PreferencesActivity.PREFS_HISTORY_KEEP_KEY, true);
		setPassword(newPass, keepHistory);
		if (mDbHelper.getCount() > 1 & historyBT.isEnabled() == false) {
			historyBT.setEnabled(true);
		}
    }
    
    private void setPassword(String newPass) {
    	setPassword(newPass, false);
    }
    
    private void setPassword(String newPass, boolean addToHistory) {
    	mCurrentPass = newPass;
		passTV.setText(mCurrentPass);
		if (addToHistory) {
			mDbHelper.createPassword(mCurrentPass);
		}
    }
}