/*
 * @author Jay
 * 
 *  Use
 *  
 *  DatePickerFragment newFragment = new DatePickerFragment();
	newFragment.show(getFragmentManager(), "datePicker", this);
	
	to show the dialog in the UI
 ***/

package nl.changer.android.components;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private static final String TAG = DatePickerFragment.class.getSimpleName();

	/***
	 * We have to call the method of {@linkplain SimpleInput} class before which this variable
	 * should be initialized.
	 * ***/
	private EditText mInputEditText;

	private Pickable mPickableInstance;

	private boolean mIsSetMinDate = false;
	private long mMinDate = 0;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
		if (mIsSetMinDate) {
			try {
				if (mMinDate > 0) {
					datePickerDialog.getDatePicker().setMinDate(mMinDate);
				} else {
					datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return datePickerDialog;
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Log.v(TAG, "#onDateSet year: " + year + " month: " + month + " day: " + day);

		// The month is zero indexed.
		month = month + 1;
		mInputEditText.setText("" + (day < 10 ? "0" + day : day) + "-" + (month < 10 ? "0" + month : month) + "-" + year);
		mPickableInstance.onPickerDismissed();
	}

	/***
	 * In Android 4.0 only a dialog is presented with 2 button, Set and Cancel. This method
	 * implementation handles the 'Cancel' button behavior in the app.
	 * ***/
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);

		mPickableInstance.onPickerDismissed();

		if (Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH || Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			Log.v(TAG, "#onDismiss text is to be cleared");
			// the dialog has been dismissed
			mInputEditText.setText(null);
		}
	}

	public void show(FragmentManager fragmentManager, String tag, EditText editText, Pickable ctx) {
		this.show(fragmentManager, tag);
		mInputEditText = editText;
		mPickableInstance = ctx;
	}

	/**
	 * Sets the minimal date to DatePickerDialog. If provided minDate value is zero then DatePicker
	 * will show current date as minimal date. This method should get called before show(...)
	 * method.
	 * 
	 * @param minDate
	 *            in milliseconds
	 */
	public void setMinimumDate(long minDate) {
		mIsSetMinDate = true;
		mMinDate = minDate;
	}
}