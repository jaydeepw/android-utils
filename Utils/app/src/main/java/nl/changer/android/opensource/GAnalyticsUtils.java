package nl.changer.android.opensource;

import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

/***
 * Helper utility class for older and deprecated version of Google Analytics.
 ***/
public class GAnalyticsUtils {

	private static final String TAG = GAnalyticsUtils.class.getSimpleName();
	
	public static void trackScreen(Context ctx, String screenName) {
		// May return null if EasyTracker has not yet been initialized with a property ID.
	     Tracker easyTracker = EasyTracker.getInstance(ctx);

	     // This screen name value will remain set on the tracker and sent with
	     // hits until it is set to a new value or to null.
	     // easyTracker.set( Fields.SCREEN_NAME, screenName );

	     // Set dispatch period.
	     easyTracker.send( MapBuilder
	         .createAppView()
	         .set( Fields.SCREEN_NAME, screenName )
	         .set( Fields.HIT_TYPE, "appview" )
	         .build() );
	}
	
	/****
	 * 
	 * @param ctx Context.
	 * @param eventCatagory Required. For e.g. ui_event, ux_event
	 * @param eventName Required. Name of the event. For e.g. button_press
	 * @param eventLabel 
	 * @param eventValue
	 * ***/
	public static void trackEvent(Context ctx, String eventCatagory, String eventName, String eventLabel, long eventValue) {
		// May return null if a EasyTracker has not yet been initialized with a
		  // property ID.
		  EasyTracker easyTracker = EasyTracker.getInstance(ctx);

		  // MapBuilder.createEvent().build() returns a Map of event fields and values
		  // that are set and sent with the hit.
		  easyTracker.send(MapBuilder
		      .createEvent(eventCatagory,     // Event category (required)
		    		  		eventName,  // Event action (required)
		    		  		eventLabel,   // Event label
		    		  		eventValue)            // Event value
		      .build()
		  );
	}
	
}