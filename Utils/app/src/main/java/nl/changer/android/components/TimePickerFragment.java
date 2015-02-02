package nl.changer.android.components;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

	private static final String TAG = TimePickerFragment.class.getSimpleName();
	
	/***
	 * We have to call the method of {@linkplain SimpleInput} class
	 * before which this variable should be initialized.
	 * ***/
	private EditText mInputEditText;
	
	private Pickable mPickableInstance;
	
	@Override
	public Dialog onCreateDialog( Bundle savedInstanceState ) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		
 
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog( getActivity(), this, hour, minute, DateFormat.is24HourFormat( getActivity() ) );
	}
	
	public void show( FragmentManager fragmentManager, String tag, EditText editText, Pickable ctx ) {
		this.show( fragmentManager, tag );
		mInputEditText = editText;
		mPickableInstance = ctx;
	}

	public void onTimeSet( TimePicker view, int hour, int minute ) {
		
		Log.v(TAG, "#onTimeSet " + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) );
		mInputEditText.setText( "" + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) );
		
		mPickableInstance.onPickerDismissed();
	}
	
	@Override
	public void onDismiss( DialogInterface dialog ) {
		super.onDismiss(dialog);
	}
}