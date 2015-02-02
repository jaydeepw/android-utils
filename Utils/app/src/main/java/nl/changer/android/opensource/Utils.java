
package nl.changer.android.opensource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.changer.GlobalConstants;
import nl.changer.KeyValueTuple;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

/*import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;*/

/***
 * Provides convenience methods and abstractions to some tasks in Android
 * 
 * <br/>
 * <br/>
 * 
 * @author Jay
 ****/
public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

	@Deprecated
	protected static Context mContext;

	static ProgressDialog mProgressDialog;

	/***
	 * @deprecated
	 * @param ctx
	 *            Activity Context. Any other context will break the app.
	 * ***/
	public Utils(Context ctx) {
		mContext = ctx;
	}

	/***
	 * @deprecated This method has been deprecated. Use its static counterpart instead.
	 * 
	 * <br/>
	 *             Use {@link nl.changer.android.opensource.Utils#showToast(android.content.Context, CharSequence)} <br/>
	 * 
	 *             Shows the message passed in the parameter in the Toast.
	 * 
	 * 
	 * @param msg
	 *            Message to be show in the toast.
	 * ***/
	public void showToast(String msg) {
		Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	/***
	 * Shows a long time duration toast message.
	 * 
	 * @param msg
	 *            Message to be show in the toast.
	 * @return Toast object just shown
	 * ***/
	public static Toast showToast(Context ctx, CharSequence msg) {
		return showToast(ctx, msg, Toast.LENGTH_LONG);
	}

	/***
	 * Shows the message passed in the parameter in the Toast.
	 * 
	 * @param msg
	 *            Message to be show in the toast.
	 * @param duration
	 *            Duration in milliseconds for which the toast should be shown
	 * @return Toast object just shown
	 * ***/
	public static Toast showToast(Context ctx, CharSequence msg, int duration) {
		Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
		toast.setDuration(duration);
		toast.show();
		return toast;
	}

	/***
	 * Checks if the Internet connection is available.
	 * 
	 * @return Returns true if the Internet connection is available. False otherwise.
	 * **/
	public static boolean isInternetAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		// if network is NOT available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		return false;
	}

	/***
	 * 
	 * Checks if the SD Card is mounted on the device.
	 * 
	 * @deprecated use {@link #isSdCardMounted()}
	 * ***/
	public static boolean isSDCARDMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;

		return false;
	}

	public static boolean isSdCardMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;

		return false;
	}

	/***
	 * Shows an alert dialog with the OK button. When the user presses OK button, the dialog
	 * dismisses.
	 * ***/
	public static void showAlertDialog(Context ctx, String title, String body) {
		showAlertDialog(ctx, title, body, null);
	}

	/***
	 * Shows an alert dialog with OK button
	 ***/
	public static void showAlertDialog(Context ctx, String title, String body, DialogInterface.OnClickListener okListener) {

		if (okListener == null) {
			okListener = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			};
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(body).setPositiveButton("OK", okListener);

		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}

		builder.show();
	}

	/***
	 * Serializes the Bitmap to Base64
	 * 
	 * @return Base64 string value of a {@linkplain android.graphics.Bitmap} passed in as a parameter
	 * @throws NullPointerException
	 *             If the parameter bitmap is null.
	 * ***/
	public static String toBase64(Bitmap bitmap) {

		if (bitmap == null) {
			throw new NullPointerException("Bitmap cannot be null");
		}

		String base64Bitmap = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] imageBitmap = stream.toByteArray();
		base64Bitmap = Base64.encodeToString(imageBitmap, Base64.DEFAULT);

		return base64Bitmap;
	}

	/***
	 * Converts the passed in drawable to Bitmap representation
	 * 
	 * @throws NullPointerException
	 *             If the parameter drawable is null.
	 * ***/
	public static Bitmap drawableToBitmap(Drawable drawable) {

		if (drawable == null) {
			throw new NullPointerException("Drawable to convert should NOT be null");
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		if (drawable.getIntrinsicWidth() <= 0 && drawable.getIntrinsicHeight() <= 0) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/***
	 * Converts the given bitmap to {@linkplain java.io.InputStream}.
	 * 
	 * @throws NullPointerException
	 *             If the parameter bitmap is null.
	 * ***/
	public static InputStream bitmapToInputStream(Bitmap bitmap) throws NullPointerException {

		if (bitmap == null) {
			throw new NullPointerException("Bitmap cannot be null");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		InputStream inputstream = new ByteArrayInputStream(baos.toByteArray());

		return inputstream;
	}

	/***
	 * Shows a progress dialog with a spinning animation in it. This method must preferably called
	 * from a UI thread.
	 * 
	 * @param ctx
	 *            Activity context
	 * @param title
	 *            Title of the progress dialog
	 * @param body
	 *            Body/Message to be shown in the progress dialog
	 * @param isCancellable
	 *            True if the dialog can be cancelled on back button press, false otherwise
	 ***/
	public static void showProgressDialog(Context ctx, String title, String body, boolean isCancellable) {
		showProgressDialog(ctx, title, body, null, isCancellable);
	}

	/***
	 * Shows a progress dialog with a spinning animation in it. This method must preferably called
	 * from a UI thread.
	 * 
	 * @param ctx
	 *            Activity context
	 * @param title
	 *            Title of the progress dialog
	 * @param body
	 *            Body/Message to be shown in the progress dialog
	 * @param icon
	 *            Icon to show in the progress dialog. It can be null.
	 * @param isCancellable
	 *            True if the dialog can be cancelled on back button press, false otherwise
	 ***/
	public static void showProgressDialog(Context ctx, String title, String body, Drawable icon, boolean isCancellable) {

		if (ctx instanceof Activity) {
			if (!((Activity) ctx).isFinishing()) {
				mProgressDialog = ProgressDialog.show(ctx, title, body, true);
				mProgressDialog.setIcon(icon);
				mProgressDialog.setCancelable(isCancellable);
			}
		}
	}

	/***
	 * Check if the {@link android.app.ProgressDialog} is visible in the UI.
	 ****/
	public static boolean isProgressDialogVisible() {
		return (mProgressDialog != null);
	}

	/***
	 * Dismiss the progress dialog if it is visible.
	 * **/
	public static void dismissProgressDialog() {

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		mProgressDialog = null;
	}

	/***
	 * Read the {@link java.io.InputStream} and convert the data received into the {@link String}
	 * 
	 * @deprecated This method is deprecated. Use {@linkplain nl.changer.android.opensource.NetworkManager} instead. TODO: This
	 *             method should goto {@linkplain nl.changer.android.opensource.NetworkManager}
	 * ***/
	public static String readStream(InputStream in) {
		StringBuffer data = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			data = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				data.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (reader != null) {
				try {
					reader.close();

					if (in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} // finally

		if (data == null)
			return null;
		else
			return data.toString();
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.ImageUtils#scaleDownBitmap(android.content.Context, android.graphics.Bitmap, int)} instead.
	 * 
	 * <br/>
	 * <br/>
	 *             Scales the image depending upon the display density of the device.
	 * 
	 *             When dealing with the bitmaps of bigger size, this method must be called from a
	 *             non-UI thread.
	 * ***/
	public static Bitmap scaleDownBitmap(Context ctx, Bitmap source, int newHeight) {
		final float densityMultiplier = getDensityMultiplier(ctx);

		Log.v(TAG, "#scaleDownBitmap Original w: " + source.getWidth() + " h: " + source.getHeight());

		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * source.getWidth() / ((double) source.getHeight()));

		Log.v(TAG, "#scaleDownBitmap Computed w: " + w + " h: " + h);

		Bitmap photo = Bitmap.createScaledBitmap(source, w, h, true);

		Log.v(TAG, "#scaleDownBitmap Final w: " + w + " h: " + h);

		return photo;
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.ImageUtils#scaleBitmap(android.content.Context, android.graphics.Bitmap, int)} instead.
	 * 
	 * <br/>
	 * <br/>
	 *             Scales the image independently of the screen density of the device.
	 * 
	 *             When dealing with the bitmaps of bigger size, this method must be called from a
	 *             non-UI thread.
	 * ***/
	public static Bitmap scaleBitmap(Context ctx, Bitmap source, int newHeight) {

		Log.v(TAG, "#scaleDownBitmap Original w: " + source.getWidth() + " h: " + source.getHeight());

		int w = (int) (newHeight * source.getWidth() / ((double) source.getHeight()));

		Log.v(TAG, "#scaleDownBitmap Computed w: " + w + " h: " + newHeight);

		Bitmap photo = Bitmap.createScaledBitmap(source, w, newHeight, true);

		Log.v(TAG, "#scaleDownBitmap Final w: " + w + " h: " + newHeight);

		return photo;
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.ImageUtils#scaleDownBitmap(android.content.Context, android.net.Uri, int)} instead.
	 * 
	 * <br/>
	 * <br/>
	 *             Scales the image independently of the screen density of the device.
	 * @param uri
	 *            Uri of the source bitmap
	 ****/
	public static Bitmap scaleDownBitmap(Context ctx, Uri uri, int newHeight) throws FileNotFoundException, IOException {
		Bitmap original = Media.getBitmap(ctx.getContentResolver(), uri);
		return scaleBitmap(ctx, original, newHeight);
	}

	/***
	 * Gives the device independent constant which can be used for scaling images, manipulating view
	 * sizes and changing dimension and display pixels etc.
	 * ***/
	public static float getDensityMultiplier(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A int value to represent dp equivalent to px value
	 */
	public static int getDip(int px, Context context) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px * scale + 0.5f);
	}

	/***
	 * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
	 * dialog.
	 * 
	 * @param ctx
	 * @param message
	 *            Message to be shown in the dialog.
	 * @param yesListener
	 *            Yes click handler
	 * @param noListener
	 * ***/
	public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
		showConfirmDialog(ctx, message, yesListener, noListener, "Yes", "No");
	}

	/***
	 * Creates a confirmation dialog with Yes-No Button. By default the buttons just dismiss the
	 * dialog.
	 * 
	 * @param ctx
	 * @param message
	 *            Message to be shown in the dialog.
	 * @param yesListener
	 *            Yes click handler
	 * @param noListener
	 * @param yesLabel
	 *            Label for yes button
	 * @param noLabel
	 *            Label for no button
	 * ***/
	public static void showConfirmDialog(Context ctx, String message, DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener, String yesLabel, String noLabel) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		if (yesListener == null) {
			yesListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
		}

		if (noListener == null) {
			noListener = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			};
		}

		builder.setMessage(message).setPositiveButton(yesLabel, yesListener).setNegativeButton(noLabel, noListener).show();
	}

	/***
	 * Creates a confirmation dialog that show a pop-up with button labeled as parameters labels.
	 * 
	 * @param ctx
	 *            {@link android.app.Activity} {@link android.content.Context}
	 * @param message
	 *            Message to be shown in the dialog.
	 * @param dialogClickListener
	 *            For e.g.
	 * 
	 *            <pre>
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	 * 
	 * 	public void onClick(DialogInterface dialog, int which) {
	 * 		switch (which) {
	 * 		case DialogInterface.BUTTON_POSITIVE:
	 * 			// Yes button clicked
	 * 			break;
	 * 
	 * 		case DialogInterface.BUTTON_NEGATIVE:
	 * 			// No button clicked
	 * 			break;
	 * 		}
	 * 	}
	 * };
	 * </pre>
	 * 
	 * @param positiveBtnLabel
	 *            For e.g. "Yes"
	 * @param negativeBtnLabel
	 *            For e.g. "No"
	 * ***/
	public static void showDialog(Context ctx, String message, String positiveBtnLabel, String negativeBtnLabel, DialogInterface.OnClickListener dialogClickListener) {

		if (dialogClickListener == null) {
			throw new NullPointerException("Action listener cannot be null");
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(message).setPositiveButton(positiveBtnLabel, dialogClickListener).setNegativeButton(negativeBtnLabel, dialogClickListener).show();
	}

	/***
	 * Gets the version name of the application. For e.g. 1.9.3
	 * ***/
	public static String getApplicationVersionNumber(Context ctx) {

		String versionName = null;

		try {
			versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionName;
	}

	/***
	 * Gets the version code of the application. For e.g. Maverick Meerkat or 2013050301
	 * ***/
	public static int getApplicationVersionCode(Context ctx) {

		int versionCode = 0;

		try {
			versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}

	/***
	 * @deprecated Use {@link #getOsVersion()} instead
	 * 
	 *             Gets the version number of the Android OS For e.g. 2.3.4 or 4.1.2
	 ***/
	public static String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	/***
	 * Gets the version number of the Android OS For e.g. 2.3.4 or 4.1.2
	 ***/
	public static String getOsVersion() {
		return Build.VERSION.RELEASE;
	}

	/***
	 * Checks if the service with the given name is currently running on the device.
	 * 
	 * @param serviceName
	 *            Fully qualified name of the server. <br/>
	 *            For e.g. nl.changer.myservice.name
	 ***/
	public static boolean isServiceRunning(Context ctx, String serviceName) {

		if (serviceName == null)
			throw new NullPointerException("Service name cannot be null");

		ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(serviceName)) {
				return true;
			}
		}

		return false;
	}

	/***
	 * Gets the device unique id called IMEI. Sometimes, this returns 00000000000000000 for the
	 * rooted devices.
	 * ***/
	public static String getDeviceImei(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/***
	 * Shares an application over the social network like Facebook, Twitter etc.
	 * 
	 * @param sharingMsg
	 *            Message to be pre-populated when the 3rd party app dialog opens up.
	 * @param emailSubject
	 *            Message that shows up as a subject while sharing through email.
	 * @param title
	 *            Title of the sharing options prompt. For e.g. "Share via" or "Share using"
	 * ***/
	public static void share(Context ctx, String sharingMsg, String emailSubject, String title) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);

		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingMsg);
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);

		ctx.startActivity(Intent.createChooser(sharingIntent, title));
	}

	/***
	 * Checks the type of data connection that is currently available on the device.
	 * 
	 * @return <code>ConnectivityManager.TYPE_*</code> as a type of internet connection on the
	 *         device. Returns -1 in case of error or none of
	 *         <code>ConnectivityManager.TYPE_*</code> is found.
	 * ***/
	public static int getDataConnectionType(Context ctx) {

		ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connMgr != null && connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
			if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
				return ConnectivityManager.TYPE_MOBILE;
			} else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
				return ConnectivityManager.TYPE_WIFI;
			} else
				return -1;
		} else
			return -1;
	}

	/***
	 * Checks if the input parameter is a valid email.
	 * ***/
	public static boolean isValidEmail(String email) {
		final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Matcher matcher;
		Pattern pattern = Pattern.compile(emailPattern);

		matcher = pattern.matcher(email);

		if (matcher != null)
			return matcher.matches();
		else
			return false;
	}

	/***
	 * Capitalizes each word in the string.
	 * ***/
	public static String capitalizeString(String string) {

		if (string == null) {
			throw new NullPointerException("String to capitalize cannot be null");
		}

		char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You
																									// can
																									// add
																									// other
																									// chars
																									// here
				found = false;
			}
		} // end for

		return String.valueOf(chars);
	}

	/***
	 * Tiles the background of the for a view with viewId as a parameter.
	 * 
	 * @deprecated Use {@link ViewUtils#tileBackground(android.content.Context, int, android.view.View, int)} instead.
	 * ***/
	public static void tileBackground(Context ctx, int viewId, int resIdOfTile) {

		try {
			// Tiling the background.
			Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resIdOfTile);
			// deprecated constructor call
			// BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(ctx.getResources(), bmp);
			bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			View view = ((Activity) ctx).findViewById(viewId);

			if (view == null)
				throw new NullPointerException("View to which the tile has to be applied should not be null");
			else
				setBackground(view, bitmapDrawable);

		} catch (Exception e) {
			Log.w(TAG, "#tileBackground Exception while tiling the background of the view");
		}
	}

	/***
	 * Sets the passed-in drawable parameter as a background to the passed in target parameter in an
	 * SDK independent way. This is the recommended way of setting background rather than using
	 * native background setters provided by {@link android.view.View} class. This method should NOT be used for
	 * setting background of an {@link android.widget.ImageView}
	 * 
	 * @param target
	 *            View to set background to.
	 * @param drawable
	 *            background image
	 * 
	 * @deprecated Use {@link ViewUtils#setBackground(android.view.View, android.graphics.drawable.Drawable)} instead.
	 * ***/
	@SuppressLint("NewApi")
	public static void setBackground(View target, Drawable drawable) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			target.setBackgroundDrawable(drawable);
		} else {
			target.setBackground(drawable);
		}
	}

	/***
	 * Tiles the background for a view with viewId as a parameter.
	 * 
	 * @deprecated Use {@link ViewUtils#setBackground(android.view.View, android.graphics.drawable.Drawable)} instead.
	 ***/
	public static void tileBackground(Context ctx, int layoutId, View viewToTileBg, int resIdOfTile) {

		try {
			// Tiling the background.
			Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resIdOfTile);
			// deprecated constructor
			// BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
			BitmapDrawable bitmapDrawable = new BitmapDrawable(ctx.getResources(), bmp);
			bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			View view = viewToTileBg.findViewById(layoutId);

			if (view != null) {
				setBackground(view, bitmapDrawable);
			}

		} catch (Exception e) {
			Log.e(TAG, "#tileBackground Exception while tiling the background of the view");
		}
	}

	/***
	 * Checks if the DB with the given name is present on the device.
	 * ***/
	public static boolean isDatabasePresent(String packageName, String dbName) {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase("/data/data/" + packageName + "/databases/" + dbName, null, SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
			// database doesn't exist yet.
			e.printStackTrace();
			Log.e(TAG, "The database does not exist." + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Exception " + e.getMessage());
		}

		boolean isDbPresent = checkDB != null ? true : false;

		return isDbPresent;
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.Utils#getImagePathForUri(android.content.Context, android.net.Uri)}
	 * 
	 * <br/>
	 * <br/>
	 * <br/>
	 * <br/>
	 *             Get the file path from the MediaStore.Images.Media Content URI
	 * 
	 * @param mediaContentUri
	 *            Content URI pointing to a row of {@link android.provider.MediaStore.Images.Media}
	 * ***/
	public static String getRealPathFromURI(Context ctx, Uri mediaContentUri) {

		Cursor cur = null;
		String path = null;

		try {
			String[] proj = { Media.DATA };
			cur = ctx.getContentResolver().query(mediaContentUri, proj, null, null, null);

			if (cur != null && cur.getCount() != 0) {
				cur.moveToFirst();
				path = cur.getString(cur.getColumnIndexOrThrow(MediaColumns.DATA));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null && !cur.isClosed())
				cur.close();
		}

		return path;
	}

	/***
	 * 
	 * @deprecated Use {@link nl.changer.android.opensource.Utils#getPathForMediaUri(android.content.Context, android.net.Uri)}
	 * 
	 * <br/>
	 * <br/>
	 *             Get the file path from the Media Content Uri for video, audio or images.
	 * 
	 * @param mediaContentUri
	 *            Media content Uri.
	 * ***/
	public static String getImagePathForUri(Context context, Uri mediaContentUri) {

		Cursor cur = null;
		String path = null;

		try {
			String[] projection = { MediaColumns.DATA };
			cur = context.getContentResolver().query(mediaContentUri, projection, null, null, null);

			if (cur != null && cur.getCount() != 0) {
				cur.moveToFirst();
				path = cur.getString(cur.getColumnIndexOrThrow(MediaColumns.DATA));
			}

			// Log.v( TAG, "#getRealPathFromURI Path: " + path );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null && !cur.isClosed())
				cur.close();
		}

		return path;
	}

	/***
	 * Get the file path from the Media Content Uri for video, audio or images.
	 * 
	 * @param mediaContentUri
	 *            Media content Uri.
	 * ***/
	public static String getPathForMediaUri(Context context, Uri mediaContentUri) {

		Cursor cur = null;
		String path = null;

		try {
			String[] projection = { MediaColumns.DATA };
			cur = context.getContentResolver().query(mediaContentUri, projection, null, null, null);

			if (cur != null && cur.getCount() != 0) {
				cur.moveToFirst();
				path = cur.getString(cur.getColumnIndexOrThrow(MediaColumns.DATA));
			}

			// Log.v( TAG, "#getRealPathFromURI Path: " + path );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null && !cur.isClosed())
				cur.close();
		}

		return path;
	}

	public static ArrayList<String> toStringArray(JSONArray jsonArr) {

		if (jsonArr == null || jsonArr.length() == 0) {
			return null;
		}

		ArrayList<String> stringArray = new ArrayList<String>();

		for (int i = 0, count = jsonArr.length(); i < count; i++) {
			try {
				String str = jsonArr.getString(i);
				stringArray.add(str);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return stringArray;
	}

	/***
	 * Convert a given list of {@link String} into a {@link org.json.JSONArray}
	 ****/
	public static JSONArray toJSONArray(ArrayList<String> stringArr) {
		JSONArray jsonArr = new JSONArray();

		for (int i = 0; i < stringArr.size(); i++) {
			String value = stringArr.get(i);
			jsonArr.put(value);
		}

		return jsonArr;
	}

	/***
	 * Gets the data storage directory(pictures dir) for the device. If the external storage is not
	 * available, this returns the reserved application data storage directory. SD Card storage will
	 * be preferred over internal storage.
	 * 
	 * 
	 * @param dirName
	 *            if the directory name is specified, it is created inside the DIRECTORY_PICTURES
	 *            directory.
	 * @return Data storage directory on the device. Maybe be a directory on SD Card or internal
	 *         storage of the device.
	 ****/
	public static File getStorageDirectory(Context ctx, String dirName) {

		if (TextUtils.isEmpty(dirName)) {
			dirName = "atemp";
		}

		File f = null;

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + dirName);
		} else {
			// media is removed, unmounted etc
			// Store image in
			// /data/data/<package-name>/cache/atemp/photograph.jpeg
			f = new File(ctx.getCacheDir() + "/" + dirName);
		}

		if (!f.exists()) {
			f.mkdirs();
		}

		return f;
	}

	/***
	 * Given a file name, this method creates a {@link java.io.File} on best chosen device storage and
	 * returns the file object. You can get the file path using {@link java.io.File#getAbsolutePath()}
	 * ***/
	public static File getFile(Context ctx, String fileName) {
		File dir = getStorageDirectory(ctx, null);
		File f = new File(dir, fileName);
		return f;
	}

	/***
	 * Writes the given image to the external storage of the device. If external storage is not
	 * available, the image is written to the application private directory
	 * 
	 * @return Path of the image file that has been written.
	 ****/
	public static String writeImage(Context ctx, byte[] imageData) {

		// TODO: move to MediaUtils
		final String FILE_NAME = "photograph.jpeg";
		File dir = null;
		String filePath = null;
		OutputStream imageFileOS;

		dir = getStorageDirectory(ctx, null);

		// dir.mkdirs();
		File f = new File(dir, FILE_NAME);

		// File f = getFile( FILE_NAME );

		try {
			imageFileOS = new FileOutputStream(f);
			imageFileOS.write(imageData);
			imageFileOS.flush();
			imageFileOS.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		filePath = f.getAbsolutePath();

		return filePath;
	}

	/****
	 * Inserts an image into {@link android.provider.MediaStore.Images.Media} content provider of the device.
	 * 
	 * @return The media content Uri to the newly created image, or null if the image failed to be
	 *         stored for any reason.
	 * ***/
	public static String writeImageToMedia(Context ctx, Bitmap image, String title, String description) {
		// TODO: move to MediaUtils
		if (ctx == null) {
			throw new NullPointerException("Context cannot be null");
		}

		return Media.insertImage(ctx.getContentResolver(), image, title, description);
	}

	/****
	 * @deprecated Use {@link nl.changer.android.opensource.AudioUtils#writeAudioToMedia(android.content.Context, java.io.File)} instead.
	 * 
	 * <br/>
	 *             Inserts an audio into {@link android.provider.MediaStore.Images.Media} content provider of the device.
	 * @return The media content Uri to the newly created audio, or null if failed for any reason.
	 * 
	 * @see {@link nl.changer.android.opensource.AudioUtils#writeAudioToMedia(android.content.Context, java.io.File)}
	 * ***/
	public static Uri writeAudioToMedia(Context ctx, File audioFile) {
		ContentValues values = new ContentValues();
		values.put(MediaColumns.DATA, audioFile.getAbsolutePath());
		values.put(MediaColumns.TITLE, "Name Of Your File");
		values.put(MediaColumns.MIME_TYPE, "audio/mpeg");
		values.put(MediaColumns.SIZE, audioFile.length());
		values.put(MediaStore.Audio.Media.ARTIST, "Artist Name");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		// Now set some extra features it depend on you
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
		Log.v(TAG, "#writeAudioToMedia uri: " + uri + " absPath: " + audioFile.getAbsolutePath());
		Uri uri2 = ctx.getContentResolver().insert(uri, values);
		Log.v(TAG, "#writeAudioToMedia uri2: " + uri2);

		return uri2;
	}

	/***
	 * Gets the name of the application that has been defined in AndroidManifest.xml
	 * 
	 * @throws android.content.pm.PackageManager.NameNotFoundException
	 ****/
	public static String getApplicationName(Context ctx) throws NameNotFoundException {

		if (ctx == null) {
			throw new NullPointerException("Context cannot be null");
		}

		final PackageManager packageMgr = ctx.getPackageManager();
		ApplicationInfo appInfo = null;

		try {
			appInfo = packageMgr.getApplicationInfo(ctx.getPackageName(), PackageManager.SIGNATURE_MATCH);
		} catch (final NameNotFoundException e) {
			throw new NameNotFoundException(e.getMessage());
		}

		final String applicationName = (String) (appInfo != null ? packageMgr.getApplicationLabel(appInfo) : "UNKNOWN");

		return applicationName;
	}

	/****
	 * Returns the URL without the query string
	 ****/
	public static URL getPathFromUrl(URL url) {

		if (url != null) {
			String urlStr = url.toString();
			String urlWithoutQueryString = urlStr.split("\\?")[0];
			try {
				return new URL(urlWithoutQueryString);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/***
	 * Transforms Calendar to ISO 8601 string.
	 ****/
	public static String fromCalendar(final Calendar calendar) {
		// TODO: move this method to DateUtils
		Date date = calendar.getTime();
		String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
		return formatted.substring(0, 22) + ":" + formatted.substring(22);
	}

	/***
	 * Gets current date and time formatted as ISO 8601 string.
	 ****/
	public static String now() {
		// TODO: move this method to DateUtils
		return fromCalendar(GregorianCalendar.getInstance());
	}

	/***
	 * Transforms ISO 8601 string to Calendar.
	 ****/
	public static Calendar toCalendar(final String iso8601string) throws ParseException {
		// TODO: move this method to DateUtils
		Calendar calendar = GregorianCalendar.getInstance();
		String s = iso8601string.replace("Z", "+00:00");
		try {
			s = s.substring(0, 22) + s.substring(23);
		} catch (IndexOutOfBoundsException e) {
			throw new org.apache.http.ParseException();
		}

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		return calendar;
	}

	/***
	 * Calculates the elapsed time after the given parameter date.
	 * 
	 * @param time
	 *            ISO formatted time when the event occurred in local time zone.
	 * ***/
	public static String getElapsedTime(String time) {
		TimeZone defaultTimeZone = TimeZone.getDefault();

		// TODO: its advisable not to use this method as it changes the
		// timezone.
		// Change it at some time in future.
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Date eventTime = parseDate(time);

		Date currentDate = new Date();

		long diffInSeconds = (currentDate.getTime() - eventTime.getTime()) / 1000;
		String elapsed = "";
		long seconds = diffInSeconds;
		long mins = diffInSeconds / 60;
		long hours = diffInSeconds / (60 * 60);
		long days = diffInSeconds / 86400;
		long weeks = diffInSeconds / 604800;
		long months = diffInSeconds / 2592000;

		// Log.v( TAG, "#getElapsedTime seconds: " + seconds + " mins: " + mins
		// + " hours: " + hours + " days: " + days );

		if (seconds < 120) {
			elapsed = "a min ago";
		} else if (mins < 60) {
			elapsed = mins + " mins ago";
		} else if (hours < 24) {
			elapsed = hours + " " + (hours > 1 ? "hrs" : "hr") + " ago";
		} else if (hours < 48) {
			elapsed = "a day ago";
		} else if (days < 7) {
			elapsed = days + " days ago";
		} else if (weeks < 5) {
			elapsed = weeks + " " + (weeks > 1 ? "weeks" : "week") + " ago";
		} else if (months < 12) {
			elapsed = months + " " + (months > 1 ? "months" : "months") + " ago";
		} else {
			elapsed = "more than a year ago";
		}

		TimeZone.setDefault(defaultTimeZone);

		return elapsed;
	}

	/***
	 * Set Mock Location for test device. DDMS cannot be used to mock location on an actual device.
	 * So this method should be used which forces the GPS Provider to mock the location on an actual
	 * device.
	 ****/
	public static void setMockLocation(Context ctx, double longitude, double latitude) {
		LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

		locationManager.addTestProvider(LocationManager.GPS_PROVIDER, "requiresNetwork" == "", "requiresSatellite" == "", "requiresCell" == "", "hasMonetaryCost" == "", "supportsAltitude" == "", "supportsSpeed" == "", "supportsBearing" == "",

		android.location.Criteria.POWER_LOW, android.location.Criteria.ACCURACY_FINE);

		Location newLocation = new Location(LocationManager.GPS_PROVIDER);

		newLocation.setLongitude(longitude);
		newLocation.setLatitude(latitude);
		newLocation.setTime(new Date().getTime());

		newLocation.setAccuracy(500);

		locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

		locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

		// http://jgrasstechtips.blogspot.it/2012/12/android-incomplete-location-object.html
		makeLocationObjectComplete(newLocation);

		locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
	}

	private static void makeLocationObjectComplete(Location newLocation) {
		Method locationJellyBeanFixMethod = null;
		try {
			locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		if (locationJellyBeanFixMethod != null) {
			try {
				locationJellyBeanFixMethod.invoke(newLocation);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * Gets the name of the day of the week.
	 * 
	 * @param date
	 *            ISO format date
	 * @return The name of the day of the week
	 * ***/
	public static String getDayOfWeek(String date) {
		// TODO: move to DateUtils
		Date dateDT = Utils.parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.DAY_OF_WEEK);

		String dayStr = null;

		switch (day) {

		case Calendar.SUNDAY:
			dayStr = "Sunday";
			break;

		case Calendar.MONDAY:
			dayStr = "Monday";
			break;

		case Calendar.TUESDAY:
			dayStr = "Tuesday";
			break;

		case Calendar.WEDNESDAY:
			dayStr = "Wednesday";
			break;

		case Calendar.THURSDAY:
			dayStr = "Thursday";
			break;

		case Calendar.FRIDAY:
			dayStr = "Friday";
			break;

		case Calendar.SATURDAY:
			dayStr = "Saturday";
			break;
		}

		return dayStr;
	}

	/**
	 * 
	 * @deprecated Use {@link nl.changer.android.opensource.DateUtils#getDayOfWeekAbbreviated(String)} instead.
	 * 
	 * <br/>
	 * <br/>
	 * <br/>
	 *             Returns abbreviated (3 letters) day of the week.
	 * 
	 * @param date
	 *            ISO format date
	 * @return The name of the day of the week
	 */
	public static String getDayOfWeekAbbreviated(String date) {
		Date dateDT = DateUtils.parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.DAY_OF_WEEK);

		String dayStr = null;

		switch (day) {

		case Calendar.SUNDAY:
			dayStr = "Sun";
			break;

		case Calendar.MONDAY:
			dayStr = "Mon";
			break;

		case Calendar.TUESDAY:
			dayStr = "Tue";
			break;

		case Calendar.WEDNESDAY:
			dayStr = "Wed";
			break;

		case Calendar.THURSDAY:
			dayStr = "Thu";
			break;

		case Calendar.FRIDAY:
			dayStr = "Fri";
			break;

		case Calendar.SATURDAY:
			dayStr = "Sat";
			break;
		}

		return dayStr;
	}

	/***
	 * 
	 * @deprecated Use {@link nl.changer.android.opensource.DateUtils#getMonth(String)} instead.
	 * 
	 * <br/>
	 * <br/>
	 * <br/>
	 *             Gets the name of the month from the given date.
	 * 
	 * @param date
	 *            ISO format date
	 * @return Returns the name of the month
	 * ***/
	public static String getMonth(String date) {
		// TODO: move to DateUtils
		Date dateDT = DateUtils.parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.MONTH);

		String dayStr = null;

		switch (day) {

		case Calendar.JANUARY:
			dayStr = "January";
			break;

		case Calendar.FEBRUARY:
			dayStr = "February";
			break;

		case Calendar.MARCH:
			dayStr = "March";
			break;

		case Calendar.APRIL:
			dayStr = "April";
			break;

		case Calendar.MAY:
			dayStr = "May";
			break;

		case Calendar.JUNE:
			dayStr = "June";
			break;

		case Calendar.JULY:
			dayStr = "July";
			break;

		case Calendar.AUGUST:
			dayStr = "August";
			break;

		case Calendar.SEPTEMBER:
			dayStr = "September";
			break;

		case Calendar.OCTOBER:
			dayStr = "October";
			break;

		case Calendar.NOVEMBER:
			dayStr = "November";
			break;

		case Calendar.DECEMBER:
			dayStr = "December";
			break;
		}

		return dayStr;
	}

	/**
	 * 
	 * 
	 * @deprecated Use {@link nl.changer.android.opensource.DateUtils#getMonthAbbreviated(String)} instead.
	 * 
	 * <br/>
	 * <br/>
	 * <br/>
	 * 
	 *             Gets 3-character abbreviated name of the month from the given date.
	 * 
	 * @param date
	 *            ISO format date
	 * @return Returns the name of the month
	 */
	public static String getMonthAbbreviated(String date) {
		Date dateDT = DateUtils.parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.MONTH);

		String dayStr = null;

		switch (day) {

		case Calendar.JANUARY:
			dayStr = "Jan";
			break;

		case Calendar.FEBRUARY:
			dayStr = "Feb";
			break;

		case Calendar.MARCH:
			dayStr = "Mar";
			break;

		case Calendar.APRIL:
			dayStr = "Apr";
			break;

		case Calendar.MAY:
			dayStr = "May";
			break;

		case Calendar.JUNE:
			dayStr = "Jun";
			break;

		case Calendar.JULY:
			dayStr = "Jul";
			break;

		case Calendar.AUGUST:
			dayStr = "Aug";
			break;

		case Calendar.SEPTEMBER:
			dayStr = "Sep";
			break;

		case Calendar.OCTOBER:
			dayStr = "Oct";
			break;

		case Calendar.NOVEMBER:
			dayStr = "Nov";
			break;

		case Calendar.DECEMBER:
			dayStr = "Dec";
			break;
		}

		return dayStr;
	}

	/***
	 * Gets random color integer
	 ****/
	public static int getRandomColor() {
		Random random = new Random();
		int red = random.nextInt(255);
		int green = random.nextInt(255);
		int blue = random.nextInt(255);

		return Color.argb(255, red, green, blue);
	}

	/****
	 * Converts a given bitmap to byte array
	 * ***/
	public static byte[] toBytes(Bitmap bmp) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	/***
	 * Resizes an image to the given width and height parameters Prefer using
	 * {@link nl.changer.android.opensource.Utils#decodeSampledBitmapFromResource(android.content.Context, android.net.Uri, int, int)} over this method.
	 * 
	 * @param sourceBitmap
	 *            Bitmap to be resized
	 * @param newWidth
	 *            Width of resized bitmap
	 * @param newHeight
	 *            Height of the resized bitmap
	 ****/
	public static Bitmap resizeImage(Bitmap sourceBitmap, int newWidth, int newHeight, boolean filter) {
		// TODO: move this method to ImageUtils
		if (sourceBitmap == null) {
			throw new NullPointerException("Bitmap to be resized cannot be null");
		}

		Bitmap resized = null;

		if (sourceBitmap.getWidth() < sourceBitmap.getHeight()) {
			// image is portrait
			resized = Bitmap.createScaledBitmap(sourceBitmap, newHeight, newWidth, true);
		} else
			// image is landscape
			resized = Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, true);

		resized = Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, true);

		return resized;
	}

	/****
	 * 
	 * <br/>
	 * <br/>
	 * 
	 * @param compressionFactor
	 *            Powers of 2 are often faster/easier for the decoder to honor
	 * **/
	public static Bitmap compressImage(Bitmap sourceBitmap, int compressionFactor) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.ARGB_8888;
		opts.inSampleSize = compressionFactor;

		if (Build.VERSION.SDK_INT >= 10) {
			opts.inPreferQualityOverSpeed = true;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, opts);

		return image;
	}

	/****
	 * @deprecated Use {@link ViewUtils#showPhotoWithRoundedCorners(android.widget.ImageView, String, int)}
	 * 
	 * <br/>
	 * <br/>
	 *             Show a photo with a rounded corners.
	 * @param cornerRadius
	 *            Should NOT be too large, ideally the value should be 8 or 10. Pass -1 if you dont
	 *            want the rounded corners
	 ****/
	public static void showPhotoWithRoundedCorners(ImageView photo, String url, int cornerRadius) {
		/*DisplayImageOptions options = null;

		if (cornerRadius != -1) {
			options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(cornerRadius)) // rounded
																																					// corner
																																					// bitmap
					.build();
		} else {
			// no rounded corners
			options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
		}

		if (!TextUtils.isEmpty(url) && !url.equalsIgnoreCase("null")) {
			photo.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(url, photo, options);
		} else {
			// hide the photos in a converted view so that
			// older photos are not visible
			// and user does not get a perception of wrong photos
			// photo.setVisibility( View.INVISIBLE );
		}*/
	}

	/****
	 * @deprecated Use {@link nl.changer.android.opensource.DateUtils#parseDate(String)} instead.
	 * 
	 * <br/>
	 * <br/>
	 *             Parses date string and returns a {@link java.util.Date} object
	 * 
	 * @return The ISO formatted date object
	 *****/
	public static Date parseDate(String date) {
		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date);
		String newDate = null;
		Date dateDT = null;

		try {
			newDate = sbDate.substring(0, 19).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String rDate = newDate.replace("T", " ");
		String nDate = rDate.replaceAll("-", "/");

		try {
			dateDT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(nDate);
			// Log.v( TAG, "#parseDate dateDT: " + dateDT );
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dateDT;
	}

	/****
	 * @deprecated Too many image decoding methods.
	 ****/
	public static Bitmap decodeSampledBitmapFromResource(Context ctx, Uri uri, int reqWidth, int reqHeight) throws FileNotFoundException {
		// TODO: move this method to ImageUtils
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		try {
			BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), new Rect(), options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		try {
			return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), new Rect(), options);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(e.getMessage());
		}
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/***
	 * Provide the height to which the sourceImage is to be resized. This method will calculate the
	 * resultant height. Use scaleDownBitmap from {@link nl.changer.android.opensource.Utils} wherever possible
	 ***/
	public Bitmap resizeImageByHeight(int height, Bitmap sourceImage) {
		// TODO: move this method to ImageUtils
		int widthO = 0; // original width
		int heightO = 0; // original height
		int widthNew = 0;
		int heightNew = 0;

		widthO = sourceImage.getWidth();
		heightO = sourceImage.getHeight();
		heightNew = height;

		// Maintain the aspect ratio
		// of the original banner image.
		widthNew = (heightNew * widthO) / heightO;

		return Bitmap.createScaledBitmap(sourceImage, widthNew, heightNew, true);
	}

	/***
	 * Checks if the url is valid
	 ****/
	public static boolean isValidURL(String url) {

		URL u = null;

		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		try {
			u.toURI();
		} catch (URISyntaxException e) {
			return false;
		}

		return true;
	}

	/***
	 * Get the type of the media. Audio, Video or Image.
	 * 
	 * @return Lower case string for one of above listed media type
	 * ***/
	public static String getMediaType(String contentType) {
		// TODO: move to MediaUtils
		if (isMedia(contentType)) {
			if (isVideo(contentType))
				return "video";
			else if (isAudio(contentType))
				return "audio";
			else if (isImage(contentType))
				return "image";
			else
				return null;
		} else {
			return null;
		}
	}

	/****
	 * Identifies if the content represented by the parameter mimeType is media. Image, Audio and
	 * Video is treated as media by this method. You can refer to standard MIME type here. <a
	 * href="http://www.iana.org/assignments/media-types/media-types.xhtml" >Standard MIME
	 * types.</a>
	 * 
	 * @param mimeType
	 *            standard MIME type of the data.
	 ****/
	public static boolean isMedia(String mimeType) {
		// TODO: move to MediaUtils
		boolean isMedia = false;

		if (mimeType != null) {
			if (mimeType.startsWith("image/") || mimeType.startsWith("video/") || mimeType.startsWith("audio/"))
				isMedia = true;
		} else {
			isMedia = false;
		}

		return isMedia;
	}

	/***
	 * Gets the Uri without the fragment. For e.g if the uri is
	 * content://com.android.storage/data/images/48829#is_png the part after '#' is called as
	 * fragment. This method strips the fragment and returns the url.
	 * ***/
	public static String removeUriFragment(String url) {

		if (url == null || url.length() == 0) {
			return null;
		}

		String[] arr = url.split("#");

		if (arr.length == 2)
			return arr[0];
		else
			return url;
	}

	/****
	 * Removes the parameters from the query from the uri
	 ****/
	public static String removeQueryParameters(Uri uri) {
		assert (uri.getAuthority() != null);
		assert (uri.getPath() != null);
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(uri.getScheme());
		builder.encodedAuthority(uri.getAuthority());
		builder.encodedPath(uri.getPath());
		return builder.build().toString();
	}

	/***
	 * Returns true if the mime type is a standard image mime type
	 ****/
	public static boolean isImage(String mimeType) {
		// TODO: move to MediaUtils
		// TODO: apply regex patter for checking the MIME type
		if (mimeType != null) {
			if (mimeType.startsWith("image/"))
				return true;
			else
				return false;
		} else
			return false;
	}

	/***
	 * Returns true if the mime type is a standard audio mime type
	 ****/
	public static boolean isAudio(String mimeType) {
		// TODO: move to MediaUtils
		// TODO: apply regex patter for checking the MIME type
		if (mimeType != null) {
			if (mimeType.startsWith("audio/"))
				return true;
			else
				return false;
		} else
			return false;
	}

	/***
	 * Returns true if the mime type is a standard video mime type
	 ****/
	public static boolean isVideo(String mimeType) {
		// TODO: move to MediaUtils
		// TODO: apply regex patter for checking the MIME type
		if (mimeType != null) {
			if (mimeType.startsWith("video/"))
				return true;
			else
				return false;
		} else
			return false;
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.Utils#formatSize(long)}
	 * **/
	public static int toMegaBytes(long byteCount) {
		long kiloBytes = byteCount / 1000;
		int megaBytes = (int) (kiloBytes / 1000);
		return megaBytes;
	}

	/***
	 * @deprecated Use {@link nl.changer.android.opensource.Utils#formatSize(long)}
	 * **/
	public static long toKiloBytes(long byteCount) {
		return (byteCount / 1000);
	}

	/****
	 * Get the media data from the one of the following media {@link android.content.ContentProvider} This method
	 * should not be called from the main thread of the application.
	 * <ul>
	 * <li>{@link android.provider.MediaStore.Images.Media}</li>
	 * <li>{@link android.provider.MediaStore.Audio.Media}</li>
	 * <li>{@link android.provider.MediaStore.Video.Media}</li>
	 * </ul>
	 * 
	 * @param ctx
	 *            Context object
	 * @param uri
	 *            Media content uri of the image, audio or video resource
	 ****/
	public static byte[] getMediaData(Context ctx, Uri uri) {
		// TODO: move to MediaUtils
		if (uri == null) {
			throw new NullPointerException("Uri cannot be null");
		}

		Cursor cur = ctx.getContentResolver().query(uri, new String[] { Media.DATA }, null, null, null);
		byte[] data = null;

		try {
			if (cur != null && cur.getCount() > 0) {
				if (cur.moveToNext()) {
					String path = cur.getString(cur.getColumnIndex(Media.DATA));

					try {
						File f = new File(path);
						FileInputStream fis = new FileInputStream(f);
						data = NetworkManager.readStreamToBytes(fis);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Log.v( TAG, "#getVideoData byte.size: " + data.length );
				} // end while
			} else
				Log.e(TAG, "#getMediaData cur is null or blank");
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}

		return data;
	}

	/***
	 * Gets the size of the media resource pointed to by the paramter mediaUri.
	 * 
	 * Known bug: for unknown reason, the image size for some images was found to be 0
	 * 
	 * @param mediaUri
	 *            uri to the media resource. For e.g. content://media/external/images/media/45490 or
	 *            content://media/external/video/media/45490
	 * 
	 * @return Size in bytes
	 ****/
	public static long getMediaSize(Context ctx, Uri mediaUri) {
		// TODO: move to MediaUtils
		Cursor cur = ctx.getContentResolver().query(mediaUri, new String[] { Media.SIZE }, null, null, null);
		long size = -1;

		try {
			if (cur != null && cur.getCount() > 0) {
				while (cur.moveToNext()) {
					size = cur.getLong(cur.getColumnIndex(Media.SIZE));

					// for unknown reason, the image size for image was found to
					// be 0
					// Log.v( TAG, "#getSize byte.size: " + size );

					if (size == 0)
						Log.w(TAG, "#getSize The media size was found to be 0. Reason: UNKNOWN");

				} // end while
			} else if (cur.getCount() == 0) {
				Log.e(TAG, "#getMediaSize cur size is 0. File may not exist");
			} else
				Log.e(TAG, "#getMediaSize cur is null");
		} finally {
			if (cur != null && !cur.isClosed())
				cur.close();
		}

		return size;
	}

	/****
	 * @deprecated Use {@link nl.changer.android.opensource.MediaUtils#getDuration(android.content.Context, android.net.Uri)} instead. <br/>
	 *             Get runtime duration of media such as audio or video in milliseconds
	 ****/
	public static long getMediaDuration(Context ctx, Uri mediaUri) {
		// TODO: move to MediaUtils
		Cursor cur = ctx.getContentResolver().query(mediaUri, new String[] { Video.Media.DURATION }, null, null, null);
		long duration = -1;

		try {
			if (cur != null && cur.getCount() > 0) {
				while (cur.moveToNext()) {
					duration = cur.getLong(cur.getColumnIndex(Video.Media.DURATION));

					// for unknown reason, the image size for image was found to
					// be 0
					// Log.v( TAG, "#getSize byte.size: " + size );

					if (duration == 0)
						Log.w(TAG, "#getMediaDuration The image size was found to be 0. Reason: UNKNOWN");

				} // end while
			} else if (cur.getCount() == 0) {
				Log.e(TAG, "#getMediaDuration cur size is 0. File may not exist");
			} else
				Log.e(TAG, "#getMediaDuration cur is null");
		} finally {
			if (cur != null && !cur.isClosed())
				cur.close();
		}

		return duration;
	}

	/****
	 * Gets media file name.
	 ****/
	public static String getMediaFileName(Context ctx, Uri mediaUri) {
		// TODO: move to MediaUtils
		String colName = MediaColumns.DISPLAY_NAME;
		Cursor cur = ctx.getContentResolver().query(mediaUri, new String[] { colName }, null, null, null);
		String dispName = null;

		try {
			if (cur != null && cur.getCount() > 0) {
				while (cur.moveToNext()) {
					dispName = cur.getString(cur.getColumnIndex(colName));

					// for unknown reason, the image size for image was found to
					// be 0
					// Log.v( TAG, "#getMediaFileName byte.size: " + size );

					if (TextUtils.isEmpty(colName)) {
						Log.w(TAG, "#getMediaFileName The file name is blank or null. Reason: UNKNOWN");
					}

				} // end while
			} else if (cur != null && cur.getCount() == 0) {
				Log.e(TAG, "#getMediaFileName File may not exist");
			} else {
				Log.e(TAG, "#getMediaFileName cur is null");
			}
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}

		return dispName;
	}

	/****
	 * Gets media type from the Uri.
	 ****/
	public static String getMediaType(Context ctx, Uri uri) {
		// TODO: move to MediaUtils
		if (uri == null) {
			throw new NullPointerException("Uri cannot be null");
		}

		String uriStr = uri.toString();

		if (uriStr.contains("video")) {
			return "video";
		} else if (uriStr.contains("audio")) {
			return "audio";
		} else if (uriStr.contains("image")) {
			return "image";
		} else {
			return null;
		}
	}

	/****
	 * Converts to list of {@link KeyValueTuple}
	 * 
	 * @throws NullPointerException
	 *             if parameter is null
	 ****/
	public static ArrayList<KeyValueTuple> toKeyValueList(JSONArray keyValueArray) {
		// TODO: remove KeyValueTyple and use
		// http://developer.android.com/reference/android/util/Pair.html
		ArrayList<KeyValueTuple> list = null;

		if (keyValueArray == null) {
			throw new NullPointerException("key-values array cannot be null");
		}

		if (keyValueArray.length() > 0) {
			list = new ArrayList<KeyValueTuple>();
		}

		for (int i = 0; i < keyValueArray.length(); i++) {
			JSONObject obj = null;
			String key = null;
			String value = null;

			try {
				obj = keyValueArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (obj == null) {
				continue;
			}

			try {
				key = obj.getString(GlobalConstants.KEY_KEY);
				value = obj.getString(GlobalConstants.KEY_VALUE);
				list.add(new KeyValueTuple(key, value));
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/****
	 * Returns {@link android.text.SpannableString} in Bold typeface
	 * 
	 * @param sourceText
	 *            String to be converted to bold.
	 * ***/
	public static SpannableStringBuilder toBold(String sourceText) {

		if (sourceText == null) {
			throw new NullPointerException("String to convert cannot be bold");
		}

		final SpannableStringBuilder sb = new SpannableStringBuilder(sourceText);

		// Span to set text color to some RGB value
		final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

		// set text bold
		sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return sb;
	}

	/****
	 * Makes the dialog fill 90% of screen width and minHeight 20% of screen height Ideally this
	 * method should not be used. Use appropriate theme for the activity to give a dialog like UI.
	 * Alternatively you can also use AlertDialog.Builder to create the same effect.
	 * ***/
	public static View dialogify(Activity ctx, int dialogLayoutId) {
		return dialogify(ctx, dialogLayoutId, 0.9f, 0.2f);
	}

	/****
	 * Helps given customized look to the dialog UI. Ideally this method should not be used. Use
	 * appropriate theme for the activity to give a dialog like UI. Alternatively you can also use
	 * AlertDialog.Builder to create the same effect.
	 * ***/
	public static View dialogify(Activity ctx, int dialogLayoutId, float minWidth, float minHeight) {
		// retrieve display dimensions
		Rect displayRectangle = new Rect();
		Window window = ctx.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

		// inflate and adjust layout
		LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(dialogLayoutId, null);

		if (minWidth != -1) {
			layout.setMinimumWidth((int) (displayRectangle.width() * minWidth));
		}

		if (minHeight != -1) {
			layout.setMinimumHeight((int) (displayRectangle.height() * minHeight));
		}

		return layout;
	}

	/****
	 * Makes the dialog fill 90% width.
	 * ***/
	public static View dialogifyWidth(Activity ctx, int dialogLayoutId) {
		return dialogify(ctx, dialogLayoutId, 0.9f, -1);
	}

	/***
	 * Formats given size in bytes to KB, MB, GB or whatever. This will work up to 1000 TB
	 * ***/
	public static String formatSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/***
	 * Formats given size in bytes to KB, MB, GB or whatever. Preferably use this method for
	 * performance efficiency.
	 * 
	 * @param si
	 *            Controls byte value precision. If true, formatting is done using approx. 1000 Uses
	 *            1024 if false.
	 * ***/
	public static String formatSize(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	/*
	 * public static String getHash(String value, String key) throws
	 * UnsupportedEncodingException, NoSuchAlgorithmException,
	 * InvalidKeyException { String type = "HmacSHA1"; SecretKeySpec secret =
	 * new SecretKeySpec(key.getBytes(), type); Mac mac = Mac.getInstance(type);
	 * mac.init(secret); byte[] bytes = mac.doFinal(value.getBytes()); return
	 * bytesToHex(bytes); }
	 * 
	 * private final static char[] hexArray = "0123456789abcdef".toCharArray();
	 * 
	 * private static String bytesToHex(byte[] bytes) { char[] hexChars = new
	 * char[bytes.length * 2]; int v; for (int j = 0; j < bytes.length; j++) { v
	 * = bytes[j] & 0xFF; hexChars[j * 2] = hexArray[v >>> 4]; hexChars[j * 2 +
	 * 1] = hexArray[v & 0x0F]; } return new String(hexChars); }
	 */

	/***
	 * Creates the uri to a file located on external storage or application internal storage.
	 ****/
	public static Uri createUri(Context ctx) {
		File root = getStorageDirectory(ctx, null);
		root.mkdirs();
		File file = new File(root, Long.toString(new Date().getTime()));
		Uri uri = Uri.fromFile(file);

		return uri;
	}

	/***
	 * Creates an intent to take a video from camera or gallery or any other application that can
	 * handle the intent.
	 ****/
	public static Intent createTakeVideoIntent(Activity ctx, Uri savingUri, int durationInSeconds) {
		// TODO: move to MediaUtils

		if (savingUri == null) {
			throw new NullPointerException("Uri cannot be null");
		}

		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		final PackageManager packageManager = ctx.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, savingUri);
			intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationInSeconds);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("video/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[] {}));

		return chooserIntent;
	}

	/***
	 * Creates a ACTION_IMAGE_CAPTURE photo & ACTION_GET_CONTENT intent. This intent will be
	 * aggregation of intents required to take picture from Gallery and Camera at the minimum. The
	 * intent will also be directed towards the apps that are capable of sourcing the image data.
	 * For e.g. Dropbox, Astro file manager.
	 * 
	 * @param savingUri
	 *            Uri to store a high resolution image at. If the user takes the picture using the
	 *            camera the image will be stored at this uri.
	 ****/
	public static Intent createTakePictureIntent(Activity ctx, Uri savingUri) {
		// TODO: move to MediaUtils

		if (savingUri == null) {
			throw new NullPointerException("Uri cannot be null");
		}

		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = ctx.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, savingUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[] {}));

		return chooserIntent;
	}

	/****
	 * Creates external content:// scheme uri to save the images at. The image saved at this
	 * {@link android.net.Uri} will be visible via the gallery application on the device.
	 * ***/
	public static Uri createImageUri(Context ctx) throws IOException {
		// TODO: move to MediaUtils

		if (ctx == null) {
			throw new NullPointerException("Context cannot be null");
		}

		Uri imageUri = null;

		ContentValues values = new ContentValues();
		values.put(MediaColumns.TITLE, "");
		values.put(ImageColumns.DESCRIPTION, "");
		imageUri = ctx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);

		return imageUri;
	}

	/****
	 * Creates external content:// scheme uri to save the videos at.
	 * ***/
	public static Uri createVideoUri(Context ctx) throws IOException {
		// TODO: move to MediaUtils

		if (ctx == null) {
			throw new NullPointerException("Context cannot be null");
		}

		Uri imageUri = null;

		ContentValues values = new ContentValues();
		values.put(MediaColumns.TITLE, "");
		values.put(ImageColumns.DESCRIPTION, "");
		imageUri = ctx.getContentResolver().insert(Video.Media.EXTERNAL_CONTENT_URI, values);

		return imageUri;
	}

	/***
	 * Get the correctly appended name from the given name parameters
	 * 
	 * @param firstName
	 *            First name
	 * @param lastName
	 *            Last name
	 * 
	 * @return Returns correctly formatted full name. Returns null if both the values are null.
	 * ***/
	public static String getName(String firstName, String lastName) {
		if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) {
			return firstName + " " + lastName;
		} else if (!TextUtils.isEmpty(firstName))
			return firstName;
		else if (!TextUtils.isEmpty(lastName))
			return lastName;
		else
			return null;
	}

	public static Bitmap roundBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	public static final boolean isRelativeUrl(String url) {

		if (TextUtils.isEmpty(url)) {
			throw new NullPointerException("Url cannot be null");
		}

		Uri uri = Uri.parse(url);

		return uri.getScheme() == null;
	}

	/***
	 * Checks if the parameter {@link android.net.Uri} is a content uri.
	 ****/
	public static boolean isContentUri(Uri uri) {
		if (!uri.toString().contains("content://")) {
			Log.w(TAG, "#isContentUri The uri is not a media content uri");
			return false;
		} else {
			return true;
		}
	}
}