package com.file.sign;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import org.apache.commons.codec.binary.Base64;
import sun.security.x509.X509CertImpl;

/**
 *
 */
public class StringSignatureTool {

	public StringSignatureTool() 
	{
	}

	public static InputStream getResourceStream(String filename) {
		ClassLoader classLoader = StringSignatureTool.class.getClassLoader();
		InputStream resourceAsStream = classLoader.getResourceAsStream(filename);
		return resourceAsStream;
	}

	public static String digest(String string) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance(kDefaultDigestAlgorithm);
		byte[] digestBytes = md.digest(string.getBytes());
		return  new String(Base64.encodeBase64(digestBytes));
	}		
	
	public static String sign(String data, PrivateKey privKey) throws Exception {
		try {
			Signature signature = Signature.getInstance(signatureSigAlg);
			signature.initSign(privKey);

			signature.update(data.getBytes());
			byte[] bytes = signature.sign();

			bytes = Base64.encodeBase64(bytes);

			return new String(bytes);
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public static void signAll(String content, PrivateKey privKey) throws Exception {
		try {
			String digest = digest(content);
			System.out.println("digest: " + digest);

			String signature = sign(digest, privKey);
			
			System.out.println("signature: " + signature);
			System.out.println("digestedData: " + content);
			
		} catch (Exception e) {
			throw e;
		}
	}	

	public static final String keyStoreFileName = "server.p12";
	public static final String keyStorePass = "password";
	public static final String pkName = "1";
	public static final String pkPass = "password";

	public static final String kDefaultDigestAlgorithm = "SHA";	
	public static final String signatureSigAlg = "MD5withRSA";//"SHA1withRSA";	
	
	public static void main(String[] argv) {

		String inputMsg = "abc";

		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			InputStream fis = getResourceStream(keyStoreFileName);
			ks.load(fis, keyStorePass.toCharArray());

			PrivateKey key = (PrivateKey) ks.getKey(pkName, pkPass.toCharArray());
			
			X509CertImpl cert = (X509CertImpl) ks.getCertificate("1");
			System.out.println("certification alg: " + cert.getSigAlgName());
			
			signAll(inputMsg, key);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
