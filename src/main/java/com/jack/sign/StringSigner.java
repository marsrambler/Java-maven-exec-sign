/**
 * 
 */
package com.jack.sign;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import org.apache.commons.codec.binary.Base64;

/**
 *
 */
public class StringSigner {
	
	public static final String kDefaultDigestAlgorithm = "SHA";
	private PrivateKey mPrivateKey = null;
	private String mSignatureSigAlg = null;
	
	public StringSigner(PrivateKey key, String algorithm) {
		mPrivateKey = key;
		mSignatureSigAlg = algorithm;
	}

	public void signAll(String content)
		throws Exception
	{
		try
		{			
			String digest = digest(content);			
			System.out.println("digest: " + digest);
			
			String signature = sign(digest);			
			System.out.println("signature: " + signature);
			
			System.out.println("digestedData: " + content);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public String digest(String string) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(kDefaultDigestAlgorithm);
		byte[] digestBytes = md.digest(string.getBytes());
		return  new String(Base64.encodeBase64(digestBytes));
	}	
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public String sign(String data) throws Exception
	{
		try
		{
			Signature signature = Signature.getInstance(mSignatureSigAlg);
			signature.initSign(mPrivateKey);
			
			signature.update(data.getBytes());
			byte[] bytes = signature.sign();
			
			bytes = Base64.encodeBase64(bytes);
			
			return new String(bytes);
		}
		catch (Exception e)
		{
			throw e;
		}		
	}	
}
