/**
 * 
 */
package com.jack.sign;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

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

	public static void main(String[] argv) {
		
		String keyStorePass = "password";
		String pkName = "1";
		String pkPass = "password";
		String keyStoreFileName = "server.p12";
		//String keyStoreFileName = "client.p12";
		
		String inputMsg = "abc";

		try {

			String signatureSigAlg = "SHA1withRSA";

			KeyStore ks = KeyStore.getInstance("PKCS12");
			InputStream fis = getResourceStream(keyStoreFileName);
			ks.load(fis, keyStorePass.toCharArray());

			PrivateKey key = (PrivateKey) ks.getKey(pkName, pkPass.toCharArray());
			StringSigner paytableSigner = new StringSigner(key, signatureSigAlg);
			paytableSigner.signAll(inputMsg);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
