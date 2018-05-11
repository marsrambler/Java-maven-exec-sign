/**
 * 
 */
package com.jack.verify;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
/**
 *
 */
public class SigningVerifyTool {
	
	public static final String kDefaultDigestAlgorithm = "SHA";
	
	/**
	 * 
	 * @param loader
	 */
	public SigningVerifyTool() {
	}

	/**
	 * 
	 * @param doc
	 * @return
	 * @throws Exception 
	 */
	public void verify(String content, String savedDigest, String signature) throws Exception {

		try {			
			verifyDigest(savedDigest, content);
			if (!SigningVerifier.verify(signature, savedDigest)) {
				throw new Exception("Invalid signature");
			}
		} 
		catch (Exception e)
		{
			throw e;
		}
	}

	private void verifyDigest(String savedDigest, String content)
			throws Exception {
		
		String computedDigest = digest(content);
		if (!savedDigest.equals(computedDigest))
			throw new Exception("digest invalid");
	}
	
	private String digest(String string) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance(kDefaultDigestAlgorithm);
		byte[] digestBytes = md.digest(string.getBytes());
		return  new String(Base64.encodeBase64(digestBytes));
	}	

	public static void main(String[] argv) {
		String content     = "abc";
		String savedDigest = "qZk+NkcGgWq6PiVxeFDCbJzQ2J0=";
		String signature   = "e8P5fZ2MX+ZX8N/cQFAmqbIubOJU5oiYwYfhz+YuHMzL8jQAihnV4bkrnrIimRxVTdKiH2O45aEIyCI9OeByysLi4L9+kqhykwRzC/it7OZUhbU21CZr/QGzN2v2SyE9aYaSn1IgIEKzx3pYtx9GAAFZ76ItjxClWrXsQ3FyLeOMzjYXBCdo5F08mEDvU9lVoMQc42u6nk3TJXAs8g8zqEJCBNV7QvPokJkVGPAbhsyb3dlKc2RxIm+4+vuhIWjfm0mxFox9Yfhm6Clmh6k4B8r8cjfkfy6GGTG4BvL0mCRXOgsq5pXfEBK3hLhelfn1lFCzRMjaAZVg/2mzBfx8wXghxgrg6oFqYEfxefKV/HxLH37o4+4HSVG7Ptg2hiA0+f9po7F08tlwo0rhi1C301r7UrTRggqZ8dzGp/dqLzoRc2bdXUwyvn6uF5o4JLaNyJ63PVWxdpV8VdDrOvtSOf0GPSsxR0eHSC9cgZOWW55k8hN6htQ3JKPkyP3Nj05ASbYsaih19z1BctpDGingMuWpTswqjeK7dbQahcvjEXhIicQnt15M0/bolKkSt44g/8/CseYjQpfK9rrYSz0V/AmKZ22PUl4bVfhhSoyBLAerIRDtHJjV8and5IiGmwuUnKDgLzYXa1bO4EQFYyPSN8O/fSg//4mE5qicgog6kB8=";

		try {
			
			SigningVerifyTool loader = new SigningVerifyTool();
			loader.verify(content, savedDigest, signature);
			System.out.println("Verified!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
