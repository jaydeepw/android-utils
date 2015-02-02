package nl.changer.android.opensource;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/****
 * Provides convenience methods and abstraction for storing data in the
 * {@link android.content.SharedPreferences}
 * 
 * <br/>
 * <br/>
 * 
 * @author Jay
 ****/
public class StorageManager {

	private static final String TAG = StorageManager.class.getSimpleName();

	protected Context mContext;

	// TODO: make these members private.
	protected SharedPreferences mSettings;
	protected Editor mEditor;

	public StorageManager(Context ctx, String prefFileName) {
		mContext = ctx;

		mSettings = mContext.getSharedPreferences(prefFileName,
				Context.MODE_PRIVATE);
		mEditor = mSettings.edit();
	}

	/***
	 * Set a value for the key
	 ****/
	public void setValue(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	/***
	 * Set a value for the key
	 ****/
	public void setValue(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	/***
	 * Set a value for the key
	 ****/
	public void setValue(String key, double value) {
		setValue(key, Double.toString(value));
	}

	/***
	 * Set a value for the key
	 ****/
	public void setValue(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
	}

	/****
	 * Gets the value from the settings stored natively on the device.
	 * 
	 * @param defaultValue
	 *            Default value for the key, if one is not found.
	 * **/
	public String getValue(String key, String defaultValue) {
		return mSettings.getString(key, defaultValue);
	}

	public int getIntValue(String key, int defaultValue) {
		return mSettings.getInt(key, defaultValue);
	}

	public long getLongValue(String key, long defaultValue) {
		return mSettings.getLong(key, defaultValue);
	}

	/****
	 * Gets the value from the preferences stored natively on the device.
	 * 
	 * @param defValue
	 *            Default value for the key, if one is not found.
	 * **/
	public boolean getValue(String key, boolean defValue) {
		return mSettings.getBoolean(key, defValue);
	}

	public void setValue(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	/****
	 * Clear all the preferences store in this {@link android.content.SharedPreferences.Editor}
	 ****/
	public void clear() {
		mEditor.clear().commit();
	}

	/**
	 * Removes preference entry for the given key.
	 * 
	 * @param key
	 */
	public void removeValue(String key) {
		if (mEditor != null) {
			mEditor.remove(key).commit();
		}
	}
}
