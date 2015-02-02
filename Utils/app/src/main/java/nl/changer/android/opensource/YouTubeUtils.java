package nl.changer.android.opensource;

import android.net.Uri;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class YouTubeUtils {

/*default.jpg -   default
https://i1.ytimg.com/vi/<insert-youtube-video-id-here>/mqdefault.jpg - medium 
https://i1.ytimg.com/vi/<insert-youtube-video-id-here>/hqdefault.jpg - high
https://i1.ytimg.com/vi/<insert-youtube-video-id-here>/sddefault.jpg
*/	
	public static final String THUMBNAIL_QUALITY_DEFAULT = "default";
	public static final String THUMBNAIL_QUALITY_MQ = "mqdefault";
	public static final String THUMBNAIL_QUALITY_HQ = "hqdefault";
	public static final String THUMBNAIL_QUALITY_SD = "sddefault";
	
	public static String createVideoUrl(String videoId) {
		
		if(TextUtils.isEmpty(videoId)) {
			throw new IllegalArgumentException("Video Id cannot be null or blank");	
		}
		
		return "http://youtube.com/watch?v=" + videoId;
	}
	
	/***
	 * Creates thubmnail url for a given video ID.
	 * 
	 * @param videoId
	 * @param quality 
	 ****/
	public static String createThumbnailUrl(String videoId, String quality) {
		
		if(quality == null) {
			quality = THUMBNAIL_QUALITY_DEFAULT;
		}
		
		if(!quality.equalsIgnoreCase(THUMBNAIL_QUALITY_DEFAULT) &&
				!quality.equalsIgnoreCase(THUMBNAIL_QUALITY_MQ) &&
				!quality.equalsIgnoreCase(THUMBNAIL_QUALITY_HQ) && 
				!quality.equalsIgnoreCase(THUMBNAIL_QUALITY_SD)) {
			throw new IllegalArgumentException("Invalid quality thumbnail requested");
		}
		
		return "http://img.youtube.com/vi/" + videoId + "/" + quality + ".jpg";
	}
	
	/****
	 * Checks to see if the your contains the authority "youtube.com"
	 ****/
	public static boolean isYouTubeUrl(String url) {
		
		if(TextUtils.isEmpty(url)) {
			return false;
		}
		
		Uri uri = Uri.parse(url);
		String authority = uri.getAuthority();
		if( !TextUtils.isEmpty(authority) && authority.contains("youtube.com")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Fetches video ID from given you tube video URL.
	 * 
	 * @param videoUrl
	 * @return video ID
	 */
	public static String getVideoId(String videoUrl) {
		String videoId = null;

		// Sample YouTube URLs.
		// "http://www.youtube.com/watch?v=8mKTiD02v3M";
		// "http://www.youtube.com/v/8mKTiD02v3M?version=3&autohide=1";
		// "http://youtu.be/8mKTiD02v3M";

		URL url;
		try {

			url = new URL(videoUrl);

			if (!TextUtils.isEmpty(videoUrl)) {
				if (videoUrl.contains("?v=")) {
					videoId = videoUrl.split("\\?v=")[1];
				} else if (videoUrl.contains("?version")) {
					videoId = url.getPath().split("\\/")[2];
				} else {
					videoId = url.getPath().split("\\/")[1];
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return videoId;
	}
}
