package com.jack.verify;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import org.apache.commons.codec.binary.Base64;


public class SigningVerifier
{
	private static String signatureSigAlg = "SHA1withRSA";
	private static KeyStore wwPublicKeystore  = null;
	private static String keyAlias = "server";
	
	static
	{
		try
		{
			ClassLoader classLoader = SigningVerifier.class.getClassLoader(); 			
			String wwPublicKeystoreFilePath = "server.trust";
			//String wwPublicKeystoreFilePath = "server.p12";
			
			String wwPublicKeyStorePass = "password";
			wwPublicKeystore = KeyStore.getInstance("jks");
			InputStream resourceAsStream1 = classLoader.getResourceAsStream(wwPublicKeystoreFilePath);
			wwPublicKeystore.load(resourceAsStream1, wwPublicKeyStorePass.toCharArray());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sz
	 * @param data
	 * @throws Exception 
	 */
	public static boolean verify(String sz, String data, PublicKey key) throws Exception
	{
		try
		{
			byte[] bytes = Base64.decodeBase64(sz.getBytes());
			
			Signature signature = Signature.getInstance(signatureSigAlg);
			signature.initVerify(key);
			signature.update(data.getBytes());
			
			return signature.verify(bytes);
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	/**
	 * 
	 * @param signature
	 * @param savedDigest
	 * @param alias
	 * @return
	 * @throws Exception 
	 */
	public static boolean verify(String signature, String savedDigest) throws Exception
	{
		try
		{
			Certificate cert = wwPublicKeystore.getCertificate(keyAlias);
			
			if(cert == null)
				throw new Exception("public key invalid");
			
			PublicKey pubkey = cert.getPublicKey();
			return verify(signature, savedDigest, pubkey);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}
