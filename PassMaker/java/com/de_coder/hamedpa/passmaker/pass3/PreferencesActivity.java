package com.de_coder.hamedpa.passmaker.pass3;
//Developed by Hamedpa
//Decoder();
import java.util.List;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.content.DialogInterface;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.de_coder.hamedpa.passmaker.R;

public class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	
	private static final String TAG = "PreferencesActivity";
	
	public static final String PREFS_HISTORY_KEEP_KEY = "prefs_history_keep";
	public static final String PREFS_HISTORY_LIMIT_KEY = "prefs_history_limit";
	
	private final int DIALOG_CONFIRM_HISTORY_DELETE_KEY = 0;
	
	CheckBoxPreference mKeepHistoryCBP;
	ListPreference mHistoryLimitLP;
	
	private PasswordDbAdapter mDbHelper;
	

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		addPreferencesFromResource(R.xml.preferences);
		
		mDbHelper = new PasswordDbAdapter(this);
		mDbHelper.open();
		
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		preferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		mKeepHistoryCBP = (CheckBoxPreference) preferenceScreen.findPreference(PREFS_HISTORY_KEEP_KEY);
		mHistoryLimitLP = (ListPreference) preferenceScreen.findPreference(PREFS_HISTORY_LIMIT_KEY);
		
		updatePreferenceViews();
	}
	
    @Override
    public void onResume() {
    	super.onResume();
		if (mDbHelper.isClosed()) {
			mDbHelper.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mDbHelper.close();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(PREFS_HISTORY_KEEP_KEY)) {
			if(mDbHelper.getAllPasswordsCount() > 0 && !mKeepHistoryCBP.isChecked()) {
				AlertDialog warningDialog = createDialog(DIALOG_CONFIRM_HISTORY_DELETE_KEY);
				warningDialog.show();
			}
			updatePreferenceViews();
		} else if (key.equals(PREFS_HISTORY_LIMIT_KEY)) {
			mDbHelper.enforceHistoryLimit();

		}
	}
	
	private void updatePreferenceViews() {
		if (!mKeepHistoryCBP.isChecked()) {
			mHistoryLimitLP.setEnabled(false);
		} else {
			mHistoryLimitLP.setEnabled(true);
		}
	}
	
	private AlertDialog createDialog(int dialogKey) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch(dialogKey) {
			case DIALOG_CONFIRM_HISTORY_DELETE_KEY:
				return builder.setMessage(R.string.prefs_history_confirm_delete_dialog)
				.setTitle(R.string.prefs_history_confirm_delete_dialog_title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {	
						mDbHelper.clearPasswordsTable();
					}					
				}).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mKeepHistoryCBP.setChecked(true);
						updatePreferenceViews();
					}
				}).create();
		}
		return null;
	}
}