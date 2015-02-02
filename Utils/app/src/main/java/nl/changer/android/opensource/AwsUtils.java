package nl.changer.android.opensource;

import android.util.Base64;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AwsUtils {

	/***
	 * Computes RFC 2104-compliant HMAC signature. This can be used to sign the Amazon S3
	 * request urls
	 * 
	 * @param data The data to be signed.
	 * @param key The signing key.
	 * @return The Base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException when signature generation fails
	 */
	public static String getHMac(String data, String key) throws SignatureException {
		
		if(data == null) {
			throw new NullPointerException("Data to be signed cannot be null");
		}
		
		String result = null;
		try {

			final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance &
			// initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] digest = mac.doFinal(data.getBytes());

			if(digest != null) {
				// Base 64 Encode the results
				result = Base64.encodeToString(digest, Base64.NO_WRAP);				
			}

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}

		return result;
	}
}
