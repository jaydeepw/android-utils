package nl.changer.android.opensource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/***
 * Provides utility methods and convenience methods for View controls.
 * 
 * <br/><br/>
 * @author Jay
 ****/
public class ViewUtils {

	private static final String TAG = ViewUtils.class.getSimpleName();


	/***
	 * Show live character counter for the number of characters typed in the parameter {@link android.widget.EditText}
	 * 
	 * @param editTextView Characters to count from
	 * @param textCounterView {@link android.widget.TextView} to show live character count in
	 * @param maxCharCount Max characters that can be typed in into the parameter edittext
	 * @param countdown if true, only the remaining of the max character count will be displayed. If false, 
	 * current character count as well as max character count will be displayed in the UI.
	 ****/
	public static void setLiveCharCounter(EditText editTextView, final TextView textCounterView, final int maxCharCount, final boolean countdown) {
		
		if(editTextView == null) {
			throw new NullPointerException("View to count text characters on cannot be null");	
		}
		
		if(textCounterView == null) {
			throw new NullPointerException("View to display count cannot be null");	
		}
		
		// initialize the TextView initial state
		if(countdown) {
			textCounterView.setText(String.valueOf(maxCharCount));	
		}
		else {
			textCounterView.setText(String.valueOf("0 / " + maxCharCount));	
		}
		
		// initialize the edittext
		setMaxLength(editTextView, maxCharCount);
		
		editTextView.addTextChangedListener(new TextWatcher() {
	        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        	
	        }
	
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	
	        	if(countdown) {
	        		// show only the remaining number of characters
	        		int charsLeft = maxCharCount - s.length();
		        	
		        	if( charsLeft >= 0 ) {
		        		textCounterView.setText(String.valueOf(charsLeft));
		        	}
	        	} else {
	        		// show number of chars / maxChars in the UI
	        		textCounterView.setText(s.length() + " / " + maxCharCount);
	        	}
	        	
	        }
	
	        public void afterTextChanged(Editable s) {
	        	
	        }
		});
	}

	/***
	 * Set max text length for textview
	 ****/
	public static void setMaxLength(TextView textView, int maxLength) {
		
		if(textView == null) {
			throw new NullPointerException("TextView cannot be null");
		}
		
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(maxLength);
		textView.setFilters(fArray);
	}
	
    /***
     * Tiles the background of the for a view with viewId as a parameter.
     * ***/
	public static void tileBackground( Context ctx, int viewId, int resIdOfTile ) {
    	
    	try {
    		// Tiling the background.
        	Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resIdOfTile);
        	BitmapDrawable bitmapDrawable = new BitmapDrawable( ctx.getResources(), bmp);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            View view = ((Activity) ctx).findViewById( viewId );
            
            if( view == null ) {
            	throw new NullPointerException("View to which the tile has to be applied should not be null");	
            }
            else {
            	setBackground( view, bitmapDrawable);	
            }
		} catch (Exception e) {
			Log.w(TAG, "#tileBackground Exception while tiling the background of the view");
		}
	}
	
	/***
	 * Sets the passed-in drawable parameter as a background to the 
	 * passed in target parameter in an SDK independent way. This
	 * is the recommended way of setting background rather
	 * than using native background setters provided by {@link android.view.View}
	 * class. This method should NOT be used for setting background of an {@link android.widget.ImageView}
	 * 
	 * @param target View to set background to.
	 * @param drawable background image
	 * ***/
	@SuppressLint("NewApi")
	public static void setBackground(View target, Drawable drawable) {
		if( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
    		target.setBackgroundDrawable(drawable);
    	} else {
    		target.setBackground(drawable);
    	}
	}
    
	
    public static void tileBackground( Context ctx, int layoutId, View viewToTileBg, int resIdOfTile ) {
    	
    	try {
            //Tiling the background.
        	Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resIdOfTile);
        	// deprecated constructor
            // BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        	BitmapDrawable bitmapDrawable = new BitmapDrawable( ctx.getResources(), bmp);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            View view = viewToTileBg.findViewById(layoutId);
            
            if( view != null ) {
            	setBackground(view, bitmapDrawable);	
            }
            
		} catch (Exception e) {
			Log.e(TAG, "#tileBackground Exception while tiling the background of the view");
		}
	}
    
    /****
	 * Show a photo with a rounded corners.
	 * @param cornerRadius Should NOT be too large, ideally the value should be 8 or 10. 
	 * Pass -1 if you dont want the rounded corners.
	 ****/
	public static void showPhotoWithRoundedCorners( ImageView photo, String url, int cornerRadius ) {
		/*DisplayImageOptions options = null;
		
		if( cornerRadius != -1 ) {
			options = new DisplayImageOptions.Builder()
						 .cacheInMemory( true )
					     .cacheOnDisc( true )
					     .displayer( new RoundedBitmapDisplayer(cornerRadius) ) // rounded corner bitmap
					     .build();	
		} else {
			// no rounded corners
			options = new DisplayImageOptions.Builder()
						 .cacheInMemory( true )
					     .cacheOnDisc( true )
					     .build();
		}
		
		if( !TextUtils.isEmpty( url ) && !url.equalsIgnoreCase("null") ) {
			photo.setVisibility( View.VISIBLE );
			ImageLoader.getInstance().displayImage( url, photo, options );
		} else {
			// hide the photos in a converted view so that 
			// older photos are not visible 
	    	// and user does not get a perception of wrong photos
			// photo.setVisibility( View.INVISIBLE );
		}*/
	}
}
