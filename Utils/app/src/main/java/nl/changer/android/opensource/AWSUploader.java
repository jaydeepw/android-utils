package nl.changer.android.opensource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.github.kevinsawicki.http.HttpRequest;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;

import nl.changer.android.http.HttpHeader;

public class AWSUploader {
	
	public static final String INTENT_UPLOAD_PROGRESS = "nl.changer.intent.awsuploader.progress";
	// public static final String INTENT_EXTRA_PROGRESS_CURRENT_PERCENT = "nl.changer.intent.awsuploader.progress.current.precent";
	public static final String INTENT_EXTRA_PROGRESS_CURRENT = "nl.changer.intent.awsuploader.progress.current";
	public static final String INTENT_EXTRA_PROGRESS_MAX = "nl.changer.intent.awsuploader.progress.max";
	
	private static final int MAX_BUFFER_SIZE = 1024 * 2;

	private static final String TAG = AWSUploader.class.getSimpleName();
	
	private String mAccessKeyId;
	private String mSecretKey;
	private String mBucketName;
	
	public AWSUploader( String accessKeyId, String secretKey, String bucketName ) {
		mAccessKeyId = accessKeyId;
		mSecretKey = secretKey;
		mBucketName = bucketName;
	}

	/**** 
	 * Uploads the image to AWS S3 and 
	 * returns the URL of the uploaded image.
	 ****/
	public URL uploadPhotograph(final String filePath) {

		Log.v( TAG, "#uploadPhotograph filePath: " + filePath );

		if( filePath == null ) {
			Log.v( TAG, "#uploadPhotograph Fatal error. The filePath of the image to be uploaded on S3 is null" );
			return null;
		}

		URL url = null;

		// if( ! Constants.IS_EMULATOR ) {

		AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(mAccessKeyId, mSecretKey));

		// This is same as used by the iOS application
		String bucketName = mBucketName;

		// prefix the name with the platform name,
		// in this case 'android'
		// to make them distinguishable on AWS
		String fileName = "image_file" + new Date().getTime() + ".jpeg";
		
		if( !s3Client.doesBucketExist( bucketName ) ) {
			s3Client.createBucket( bucketName );
		}	// end if

		PutObjectRequest por = new PutObjectRequest(bucketName, fileName, new File( filePath));
		s3Client.putObject(por);
		// Log.v( TAG, "#uploadPhotograph probably image uploading has finished " );

		// getting the URL
		ResponseHeaderOverrides override = new ResponseHeaderOverrides();
		override.setContentType( MimeType.IMAGE_JPEG );

		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( bucketName, fileName );
		urlRequest.setExpiration( new Date( System.currentTimeMillis() + 3600 ) );  // Added an hour's worth of milliseconds to the current time.
		urlRequest.setResponseHeaders( override );

		url = s3Client.generatePresignedUrl( urlRequest );

		// strip off the query string from the URL
		url = Utils.getPathFromUrl( url );

		return url;
	}
	
	/****
	 * @deprecated
	 * <br/>
	 * Use {@link nl.changer.android.opensource.AWSUploader#uploadObjectByUri(android.content.Context, String, String, android.net.Uri, java.util.HashMap)}
	 * <br/>
	 * 
	 * Uploads the object pointed to by the Uri parameter to AWS S3.
	 * Compresses the image if it is larger than 1MB in size.
	 * 
	 * @param ctx
	 * @param uri Media Uri of the resource on the device
	 * @param contentType
	 * @param url Signed PUT URL to upload to AWS
	 * ****/
	public static boolean uploadMediaObject(Context ctx, Uri uri, String contentType, String url) {
		
		if( uri == null ) {
			throw new NullPointerException(" uri cannot be null or blank");	
		}
		
		if( TextUtils.isEmpty(contentType) ) {
			throw new NullPointerException(" contentType cannot be null or blank");	
		}
		
		if( TextUtils.isEmpty(url) ) {
			throw new NullPointerException(" url cannot be null or blank");	
		}
	
		// Log.v( TAG, "#uploadMediaObject uri: " + uri + " contentType: " + contentType + " url: " + url );
		Bitmap bmp = null;
		byte[] data = null;
		boolean isSucccessful = false;
		
		if(Utils.isImage(contentType)) {
			// content://media/external/images/media/45490
			
			data = Utils.getMediaData( ctx, uri );
		} else if( Utils.isAudio( contentType ) ) {
			// android.provider.MediaStore.Audio.
			data = Utils.getMediaData( ctx, uri );
		} else if( Utils.isVideo(contentType) ) {
			data = Utils.getMediaData( ctx, uri );
		}
			
		Log.i( TAG, "#uploadMediaObject media size: " + Utils.formatSize(Utils.getMediaSize(ctx, uri), true) );
		
		// Log.v(TAG, "#uploadObject uploading...");
		HashMap<String, Object> outputData = new HashMap<String, Object>();
		isSucccessful = uploadObject( ctx, url, contentType, data, outputData );
		
		return isSucccessful;
	}
	

	
	/****
	 * @deprecated Potentially dangerous method as it takes all the data in memory.
	 * <br/> 
	 * Use {@link nl.changer.android.opensource.AWSUploader#uploadObjectByUri(android.content.Context, String, String, android.net.Uri, java.util.HashMap)}
	 * of this same method.
	 * <br/>
	 * @param urlStr Signed HTTP PUT Url to upload the object to
	 * @param contentType Standard HTTP Content type of the object to be uploaded to the server
	 * @param inputData {@link org.json.JSONObject} or byte[] to be uploaded to the server
	 * @param outputData Map to return the result back
	 ****/
	public static boolean uploadObjectToAWS( Context ctx, String urlStr, String contentType, Object inputData, HashMap<String, Object> outputData ) {
		
		URL url = null;
		boolean isSucccessful = false;
		
		try {
			url = new URL(urlStr);
			// Log.v(TAG, "#uploadObjectToAWS url: " + url );
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		connection.setDoOutput(true);
		connection.setRequestProperty( HTTP.CONTENT_TYPE, contentType );
		connection.setRequestProperty( "Accept", "*/*" );
		try {
			connection.setRequestMethod( HttpPut.METHOD_NAME );
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		
		// OutputStreamWriter out = null;
		DataOutputStream out = null; 
		try {
			// out = new OutputStreamWriter( connection.getOutputStream() );
			out = new DataOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Log.v(TAG, "#uploadObjectToAWS uploading using " + out.getClass().getSimpleName() );
		
		if( out != null ) {
			if( inputData != null && inputData.toString().length() > 0 ) {
				
				byte[] buffer = null;
				try {
					if( inputData instanceof JSONObject || inputData instanceof JSONArray || inputData instanceof String )
						buffer = inputData.toString().getBytes();
						// out.write( inputData.toString().getBytes() );
					else if ( inputData instanceof byte[] ) {
						buffer = (byte[]) inputData;
						
						// comment this. Send each byte individually to update progress.
						// out.write(buffer);
					}
					
					
					// byte[] bucket;// = new byte[256];
					int bucketSize = 256;
					int packetSent = 0;
					//int offset = 0;
					// newly added code, not tested yet.
					
					for (int i = 0; i < buffer.length; i++) {
						out.write( buffer[i] );
						Intent intent = new Intent(INTENT_UPLOAD_PROGRESS);
						intent.putExtra(INTENT_EXTRA_PROGRESS_CURRENT, i);
						intent.putExtra(INTENT_EXTRA_PROGRESS_MAX, buffer.length);
						ctx.sendBroadcast(intent);
					}	// end for
					
					out.flush();
			  		out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
		} else {
			Log.w(TAG, "#uploadObject out stream is null");	
		}
		
		try {
			int responseCode = connection.getResponseCode();
			Log.v( TAG, "#uploadObject resCode: " + responseCode );
			
			if( responseCode == HttpStatus.SC_OK ) {
				isSucccessful = true;	
			} else {
				isSucccessful = false;	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return isSucccessful;
	}
	
	
	/****
	 * @deprecated Potentially dangerous method as it takes all the data in memory.
	 * <br/> 
	 * Use {@link nl.changer.android.opensource.AWSUploader#uploadObjectByUri(android.content.Context, String, String, android.net.Uri, java.util.HashMap)}
	 * of this same method.
	 * <br/>
	 * @param inputData Can be a String object or byte[]
	 ****/
	public static boolean uploadObject(Context ctx, String urlStr, String contentType, Object inputData, HashMap<String, Object> outputData) {
		
		if(inputData == null) {
			throw new NullPointerException("Data to upload cannot be null");	
		}
		
		boolean isSucccessful = false;
		
		byte[] buffer = null;
		
		if( inputData instanceof JSONObject || inputData instanceof JSONArray || inputData instanceof String )
			buffer = inputData.toString().getBytes();
		else if ( inputData instanceof byte[] ) {
			buffer = (byte[]) inputData;
		} else {
			throw new InvalidParameterException("Invalid data to be uploaded");	
		}
		
		
		int responseCode = HttpRequest.put(urlStr)
							.contentType(contentType)
							.accept("*/*")
							.send(buffer)
							.code();
		
		Log.i(TAG, "#uploadObject resCode: " + responseCode );
		
		// we dont know exactly what status is sent by
		// S3 upon successful upload. So lets keep a range.
		if( responseCode >= HttpStatus.SC_OK && responseCode <= 299 ) {
			isSucccessful = true;	
		} else {
			isSucccessful = false;	
		}
		
		return isSucccessful;
	}
	
	/****
	 * This is the most efficient method to upload the data to server.
	 * Prefer using this method over using the other ones in future.
	 * 
	 * @param ctx
	 * @param urlStr Url to upload the data to
	 * @param contentType
	 * @param uri {@link android.net.Uri} from where to get the data
	 * @param outputData
	 ****/
	public static boolean uploadObjectByUri(Context ctx, String urlStr, String contentType, Uri uri, HashMap<String, Object> outputData) {
		
		if(uri == null) {
			throw new NullPointerException("uri to upload cannot be null");	
		}
		
		boolean isSucccessful = false;
		
		String path = Utils.getPathForMediaUri(ctx, uri);
		
		URL url = null;
		File f = new File(path);
		try {
			url = new URL(urlStr);
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.setRequestProperty(HTTP.CONTENT_TYPE, contentType);
		connection.setRequestProperty(HttpHeader.ACCEPT, "*/*");
		connection.setFixedLengthStreamingMode((int) f.length());
		
		Log.i(TAG, "#uploadObjectByUri <====Uploading... File length " + Utils.formatSize(f.length()) + " ====>");
		
		try {
			connection.setRequestMethod(HttpPut.METHOD_NAME);
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		
		BufferedOutputStream out = null;
		int bytesRead = 0, bytesAvailable = 0, bufferSize;
	    byte[] buffer;
	    
		try {
			out = new BufferedOutputStream(connection.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(out != null) {
			FileInputStream fileInputStream = null;
			BufferedInputStream buffInputStream = null;
			try {
				fileInputStream = new FileInputStream(f);
				buffInputStream = new BufferedInputStream(fileInputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				bytesAvailable = buffInputStream.available();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
	        buffer = new byte[bufferSize];
	        
	        try {
				bytesRead = buffInputStream.read(buffer, 0, bufferSize);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	    	
	        try {
	            while(bytesRead > 0) {
	                try {
	                	out.write(buffer, 0, bufferSize);
	                	out.flush();
	                	
	                	// Log.i(TAG, "bytes written: " + bytesRead);
	                } catch (OutOfMemoryError e) {
	                    e.printStackTrace();
	                    isSucccessful = false;
	                }
	                bytesAvailable = buffInputStream.available();
	                bufferSize = Math.min(bytesAvailable, MAX_BUFFER_SIZE);
	                bytesRead = buffInputStream.read(buffer, 0, bufferSize);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            isSucccessful = false;
	        }
		} else {
			Log.w(TAG, "#uploadObjectByUri out stream is null");	
		}
		
		try {
			int responseCode = connection.getResponseCode();
			Log.v( TAG, "#uploadObjectByUri resCode: " + responseCode );
			
			if(responseCode == HttpStatus.SC_OK) {
				isSucccessful = true;	
			} else {
				isSucccessful = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.i( TAG, "#uploadObjectByUri <====Uploading finished====>" );
		
		return isSucccessful;
	}

}
