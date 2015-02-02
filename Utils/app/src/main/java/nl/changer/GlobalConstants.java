/***
 * Collection of key names that can be used anywhere like 
 * JSON parsing of JSON has those keys, native storage in key-values etc.
 * ***/

package nl.changer;

public class GlobalConstants {
	
	/***Use API_OUTPUT_STATUS_CODE and API_OUTPUT_STATUS_LINE***/
	@Deprecated
	public static final String API_OUTPUT_STATUS = "status";
	
	public static final String API_OUTPUT_STATUS_CODE = "status_code";
	public static final String API_OUTPUT_STATUS_LINE = "status_line";
	public static final String API_OUTPUT_STATUS_SUCCESS = "success";
	public static final String API_OUTPUT_STATUS_ERROR = "error";
	public static final String API_OUTPUT_STATUS_MESSAGE = "message";
	
	public static final String KEY_STATUS = "status";
	public static final int ERROR_NONE = 0;
	public static final int ERROR_UNKNOWN = 1;
	public static final int ERROR_NETWORK_TIMEOUT = 2;
	
	public static final String KEY_ACCESS_TOKEN = "access_token";
	
	public static final String KEY_AUTH_TOKEN = "auth_token";
	
	/****ANSWER
	 * Heroku dis-allowed custom headers without X in them.
	 * So we started sending both of them to the server.
	 * */
	public static final String KEY_X_ACCESS_TOKEN = "X-Access-Token";
	
	public static final String KEY_X_AUTH_TOKEN = "X-Auth-Token";
	
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_NAME = "name";
	public static final String KEY_ID = "id";
	public static final String KEY_SMALL = "small";
	public static final String KEY_FIRST_NAME = "first_name";
	public static final String KEY_LAST_NAME = "last_name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_URL = "url";
	public static final String KEY_DATA = "data";
	public static final String KEY_COUNT = "count";
	public static final String KEY_RESULTS = "results";
	public static final String KEY_POST = "post";
	public static final String KEY_BODY = "body";
	public static final String KEY_PHOTO_URL = "photo_url";
	public static final String KEY_THUMBNAILS = "thumbnails";
	public static final String KEY_START_DATE = "start_date";
	public static final String KEY_END_DATE = "end_date";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_ANSWER = "answer";
	public static final String KEY_NEXT = "next";
	public static final String KEY_KEY = "key";
	public static final String KEY_VALUE = "value";
	public static final String KEY_PARENT = "parent";
	public static final String KEY_SUBSCRIPTIONS = "subscriptions";
	public static final String KEY_USER = "user";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_FREE = "free";
	public static final String KEY_IMAGE_URL = "image_url";
	public static final String KEY_PAID = "paid";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_POLICY = "policy";
	public static final String KEY_SIGNATURE = "signature";
	public static final String KEY_FILE = "file";
	public static final String KEY_URI = "uri";
	public static final String KEY_TYPE = "type";
	public static final String KEY_EXTRAS = "extras";
	public static final String KEY_ITEM = "item";
	
	public static final String NEW_LINE = "\n";
	
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	/***
	 * Constant indicating that the resource like comments, photos etc
	 * was FOUND on the server.
	 * ***/
	public static final int SUCCESS_RESOURCE_FOUND = 1;
	/***
	 * Constant indicating that the resource like comments, photos etc
	 * was FOUND on the server.
	 * ***/
	public static final int SUCCESS_RESOURCE_NOT_FOUND = 2;
}