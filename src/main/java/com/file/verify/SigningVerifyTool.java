package com.file.verify;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import org.apache.commons.codec.binary.Base64;
import sun.security.x509.X509CertImpl;

/**
 *
 */
public class SigningVerifyTool {

	/**
	 * 
	 * @param loader
	 */
	public SigningVerifyTool() {
	}

	public static InputStream getResourceStream(String filename) {
		ClassLoader classLoader = SigningVerifyTool.class.getClassLoader();
		InputStream resourceAsStream = classLoader.getResourceAsStream(filename);
		return resourceAsStream;
	}
	
	private static String digest(String string)
			throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(kDefaultDigestAlgorithm);
		byte[] digestBytes = md.digest(string.getBytes());
		return new String(Base64.encodeBase64(digestBytes));
	}

	private static void verifyDigest(String savedDigest, String content)
			throws Exception {

		String computedDigest = digest(content);
		if (!savedDigest.equals(computedDigest))
			throw new Exception("digest invalid");
	}
	
	public static boolean verifySignature(String savedSignature, String savedDigest, PublicKey key) throws Exception
	{
		try
		{
			byte[] bytes = Base64.decodeBase64(savedSignature.getBytes());
			
			Signature signature = Signature.getInstance(signatureSigAlg);
			signature.initVerify(key);
			signature.update(savedDigest.getBytes());
			
			return signature.verify(bytes);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}	
	
	
	public static final String wwPublicKeystoreFilePath = "server.trust";			
	public static final String wwPublicKeyStorePass = "password";
	public static final String keyAlias = "server";
	
	public static final String signatureSigAlg = "MD5withRSA";//"SHA1withRSA";
	public static final String kDefaultDigestAlgorithm = "SHA";

	public static void main(String[] argv) {
		
		String content = "abc";
		String savedDigest = "qZk+NkcGgWq6PiVxeFDCbJzQ2J0=";
		String signature = "CYsH0rwSZ3n4wQG6RIAurAsZ/M6/0pXNC0jHZCaoXi1QrsOjHAUf7/4zer1vRnd17EsJaOdOA5uXBHc9S/xAbK7dhCZtQrR84w1qVopdK14U4QDz+HftYQ2MvZoUE3HIouH+WYm8tqq20QlnA/oCNoNQJEAa7wEiUmlHux2AtfUg0nkaCvKyuNufhN21GGV3j5CIUfw8DKMhGlMzEntDWI7MMVc7Jy7Z2oFBlbYr8yqp6vxwxD5u4QxESnq/41TQ8sZTPauJqrSwVB2FFGFaRm/K71NRCcntxb6jGca0xQ6QE84xDSsWvyTpCJ9pSWEs/y4Xkl1AlIJwVppadGjtNfwwRE93W+OY0Sljz/c2iBPF7f+CTkZUCmLebrpAD0xv/Yo5J946FJiDUFJmusy0X8zwefkgo2CMr78adPA1lpvAhOAC+YAFnpSG30/fdT60W769GvRVvUY70f9qfGy3ICT9K705UmwctB3NIJlE/o7orpFeUZhjB7JL6Em1PLlryyxmHm/LI828+FBh9oWZH5k6DTZMlSsMw1XK7MHk4C5qIN8dmH72NMqXxOkkEqmhZELOgO4lU6Jck/Qt91Wj//Ww57B9aYQ4WDba3+5TwsC2A9w2UmdG9Ro16zpxdM+fxGijnp3s8CMY0+kTRjqR2hj6zgDYbawOv0DHpoIZNgs=";
		//"e8P5fZ2MX+ZX8N/cQFAmqbIubOJU5oiYwYfhz+YuHMzL8jQAihnV4bkrnrIimRxVTdKiH2O45aEIyCI9OeByysLi4L9+kqhykwRzC/it7OZUhbU21CZr/QGzN2v2SyE9aYaSn1IgIEKzx3pYtx9GAAFZ76ItjxClWrXsQ3FyLeOMzjYXBCdo5F08mEDvU9lVoMQc42u6nk3TJXAs8g8zqEJCBNV7QvPokJkVGPAbhsyb3dlKc2RxIm+4+vuhIWjfm0mxFox9Yfhm6Clmh6k4B8r8cjfkfy6GGTG4BvL0mCRXOgsq5pXfEBK3hLhelfn1lFCzRMjaAZVg/2mzBfx8wXghxgrg6oFqYEfxefKV/HxLH37o4+4HSVG7Ptg2hiA0+f9po7F08tlwo0rhi1C301r7UrTRggqZ8dzGp/dqLzoRc2bdXUwyvn6uF5o4JLaNyJ63PVWxdpV8VdDrOvtSOf0GPSsxR0eHSC9cgZOWW55k8hN6htQ3JKPkyP3Nj05ASbYsaih19z1BctpDGingMuWpTswqjeK7dbQahcvjEXhIicQnt15M0/bolKkSt44g/8/CseYjQpfK9rrYSz0V/AmKZ22PUl4bVfhhSoyBLAerIRDtHJjV8and5IiGmwuUnKDgLzYXa1bO4EQFYyPSN8O/fSg//4mE5qicgog6kB8=";

		try {

			verifyDigest(savedDigest, content);	
			
			KeyStore wwPublicKeystore = KeyStore.getInstance("jks");			
			InputStream resourceAsStream = getResourceStream(wwPublicKeystoreFilePath);
			wwPublicKeystore.load(resourceAsStream, wwPublicKeyStorePass.toCharArray());						
			
			X509CertImpl x509cert = (X509CertImpl) wwPublicKeystore.getCertificate(keyAlias);
			System.out.println("certification alorithm: " + x509cert.getSigAlgName());
			
			PublicKey pubKey = x509cert.getPublicKey();			
			boolean result = verifySignature(signature, savedDigest, pubKey);
			
			System.out.println("Verified result: " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
