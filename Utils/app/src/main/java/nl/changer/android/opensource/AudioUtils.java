
package nl.changer.android.opensource;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class AudioUtils {

	private static final String TAG = AudioUtils.class.getSimpleName();

	private static String mFileName;
	private static MediaRecorder mRecorder;

	public static void startRecordingAudio(Context ctx) {

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		// Winamp does not play the
		// audio recorded MP3 audio.
		mFileName = Utils.getStorageDirectory(ctx, "application-utils/audio").getAbsolutePath() + "/" + new Date().getTime() + ".mp3";
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch (IllegalStateException e) {
			Log.e(TAG, " failed" + e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, " failed" + e.getMessage());
		} catch (Exception e) {
			Log.e(TAG, " failed" + e.getMessage());
		}
	}

	public static Uri stopRecordingAudio(Context ctx) {

		try {
			mRecorder.stop();
			mRecorder.reset();    // set state to idle
			mRecorder.release();
			mRecorder = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return saveAudio(ctx);
	}

	/****
	 * 
	 * @deprecated
	 * <br/>
	 * Use {@link nl.changer.android.opensource.AudioUtils#saveAudio(android.content.Context)}
	 * Insert an audio into {@link android.provider.MediaStore.Images.Media} content provider of the device.
	 * 
	 * @return The media content Uri to the newly created audio, or null if failed for any reason.
	 * ***/
	public static Uri writeAudioToMedia(Context ctx, File audioFile) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, "");
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
		values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
		values.put(MediaStore.Audio.Media.ARTIST, "");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		// Now set some extra features it depend on you
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
		Uri uri2 = ctx.getContentResolver().insert(uri, values);

		if (uri2 == null || TextUtils.isEmpty(uri2.toString())) {
			Log.w(TAG, "Something went wrong while inserting data to content resolver");
		}

		return uri2;
	}

	/**
	 * 
	 * @param ctx
	 * @return The media content Uri to the newly created audio, or null if failed for any reason.
	 */
	private static Uri saveAudio(Context ctx) {
		File audioFile = new File(mFileName);
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, "");
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg");
		values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
		values.put(MediaStore.Audio.Media.ARTIST, "");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		// Now set some extra features it depend on you
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile.getAbsolutePath());
		Uri uri2 = ctx.getContentResolver().insert(uri, values);

		if (uri2 == null || TextUtils.isEmpty(uri2.toString())) {
			Log.w(TAG, "Something went wrong while inserting data to content resolver");
		}

		return uri2;
	}

	/****
	 * Remove audio file that is existing on the device.
	 * 
	 * @param ctx
	 *            {@link android.content.Context}
	 * @param uri
	 *            {@link android.net.Uri} of the audio file
	 * 
	 * @throws NullPointerException
	 *             if the Uri parameter is null
	 * */
	public static boolean removeAudio(Context ctx, Uri uri) {

		if (uri == null) {
			throw new NullPointerException("Uri cannot be null");
		}

		return (ctx.getContentResolver().delete(uri, null, null) != 0);
	}
}
