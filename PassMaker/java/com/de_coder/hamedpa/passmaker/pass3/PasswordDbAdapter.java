package com.de_coder.hamedpa.passmaker.pass3;
//Developed by Hamedpa
//Decoder();
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.util.Log;

public class PasswordDbAdapter {
	
	private static final String TAG = "PasswordDbAdapter";
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PASSWORD = "password";
	

	private static final String DB_CREATE_STMT = "create table passwords (_id integer primary key autoincrement, password text not null);";
	private static final String DB_DESTROY_STMT = "DROP TABLE IF EXISTS passwords;";
	private static final String DB_NAME = "data";
	private static final String DB_TABLE = "passwords";
	private static final int DB_VERSION = 1;
	

	private final Context mCtx;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	

	private SharedPreferences mSharedPrefs;
	

	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		private boolean open;
		
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			this.open = true;
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE_STMT);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DB_DESTROY_STMT);
			onCreate(db);
		}
		@Override
		public synchronized void close() {
			super.close();
			this.open = false;
		}
		
		public boolean isOpen() {
			return this.open;
		}
		
		public boolean isClosed() {
			return !this.open;
		}
		
	}
	

	public PasswordDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	

	public PasswordDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
		return this;
	}
	

	public void close() {
		mDbHelper.close();
	}
	

	public boolean isOpen() {
		return mDbHelper.isOpen();
	}
	

	public boolean isClosed() {
		return !isOpen();
	}
	
	

	public long createPassword(String password) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PASSWORD, password);
		long newPassKey = mDb.insert(DB_TABLE, null, initialValues);
		enforceHistoryLimit();
		return newPassKey;
	}
	

	public boolean deletePassword(long passwordKey) {
		return mDb.delete(DB_TABLE, KEY_ROWID + "=" + passwordKey, null) > 0;
	}
	

	public Cursor getAllPasswords() {
		return mDb.query(DB_TABLE, new String[] {KEY_ROWID, KEY_PASSWORD}, null, null, null, null, KEY_ROWID);
	}
	
	public Cursor getAllPasswordsDescending() {
		return mDb.query(DB_TABLE, new String[] {KEY_ROWID, KEY_PASSWORD}, null, null, null, null, KEY_ROWID + " DESC");
	}
	

	public Cursor getAllOldPasswords() {
		Cursor allPasswords = getAllPasswords();
		if (!allPasswords.isAfterLast()) {
			allPasswords.moveToNext();
		}
		return allPasswords;
	}
	

	public Cursor getAllOldPasswordsDescending() {
		Cursor allPasswords = getAllPasswordsDescending();
		// Just return if there are no pwds
		if (allPasswords.getCount() == 0) {
			return allPasswords;
		}
		
		
		return allPasswords;
	}
	
	public List<String> getAllPasswordsAsList() {
		Cursor cursor = getAllPasswords();
		return cursorToPasswordList(cursor);
	}
	
	public List<String> getAllPasswordsAsDescendingList() {
		Cursor cursor = getAllPasswordsDescending();
		return cursorToPasswordList(cursor);
	}
	
	public int getCount() {
		return getAllPasswordsCount();
	}
	
	public int getAllPasswordsCount() {
		Cursor allPasswords = getAllPasswords();
		Log.d(TAG, "getAllPasswordsCount is " + allPasswords.getCount());
		return allPasswords.getCount();
	}
	

	public boolean replaceHistoryWithList(List<String> passwordList) {
		if (clearPasswordsTable() > -1) {
			for (Iterator<String> listIterator = passwordList.iterator(); listIterator.hasNext();) {
				String nextPassword = listIterator.next();
				createPassword(nextPassword);
			}
			return true;
		} else {
			return false;
		}
	}
	

	public int clearPasswordsTable() {
		return mDb.delete(DB_TABLE, "1", null);
	}
	

	public Cursor getPassword(long passwordKey) throws SQLException {
		Cursor mCursor = mDb.query(true, DB_TABLE, new String[] {KEY_ROWID,
                				   KEY_PASSWORD}, KEY_ROWID + "=" + passwordKey, null,
                				   null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public int enforceHistoryLimit() {
		int deleteCount = 0;
		int historyLimit = Integer.parseInt(mSharedPrefs.getString(PreferencesActivity.PREFS_HISTORY_LIMIT_KEY, "5"));
		

		
	 	while (getCount() > historyLimit + 1) {
			Cursor allPasswords = getAllPasswords();
			long oldestPassKey;
			
			if(allPasswords.moveToFirst()) {
				oldestPassKey = allPasswords.getLong(allPasswords.getColumnIndex(KEY_ROWID));
				deletePassword(oldestPassKey);
			}
			deleteCount++;
			Log.d(TAG, "Deleting password: " + allPasswords.getString(allPasswords.getColumnIndex(KEY_PASSWORD)));
			Log.d(TAG, "Delete count is: " + deleteCount);
		}
		return deleteCount;
	}
	
	private List<String> cursorToPasswordList(Cursor cursor) {
		List<String> returnList = new ArrayList<String>();
		int passwordColIndex = cursor.getColumnIndex(KEY_PASSWORD);
		
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); cursor.isAfterLast() == false; cursor.moveToNext()) {
				returnList.add(cursor.getString(passwordColIndex));
			}
		}
		
		return returnList;
	}
}