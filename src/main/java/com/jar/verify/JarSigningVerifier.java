package com.jar.verify;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSigningVerifier {

	public static InputStream getResourceStream(String filename) {
		ClassLoader classLoader = JarSigningVerifier.class.getClassLoader();
		InputStream resourceAsStream = classLoader.getResourceAsStream(filename);
		return resourceAsStream;
	}
	
	public static String getResourceStreamPath(String filename) {
		ClassLoader classLoader = JarSigningVerifier.class.getClassLoader();
		return classLoader.getResource(filename).getPath();
	}
	
	public final static String keystorePath = "truststore.jks";
	public final static String keystorePass = "password";
	
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		
		final HashMap<String, Certificate> certMap = new HashMap<String, Certificate>();

		InputStream in = getResourceStream(keystorePath);
		KeyStore ks = KeyStore.getInstance("jks"); //KeyStore.getDefaultType());
		ks.load(in, keystorePass.toCharArray());
		
		Enumeration<String> aliases = ks.aliases();
		while (aliases.hasMoreElements()) {
		    String alias = aliases.nextElement();
		    Certificate cert = ks.getCertificate(alias); 
		    certMap.put(alias, cert);
		}// while
		
		String jarFileName = "PrintMessage-signed.jar";
		String jarFilePath = getResourceStreamPath(jarFileName);
		
		File readFile = new File(jarFilePath);
		
		JarFile jar = new JarFile(readFile, true);
		Enumeration<JarEntry> entries = jar.entries();
		
		while (entries.hasMoreElements()) {
			
		    JarEntry entry = entries.nextElement();

		    // Verify the entry
		    InputStream inJar = jar.getInputStream(entry);
		    try {
		        drain(inJar);
		    } finally {
		        try {
		            in.close();
		        } catch (Exception e) {
		        }
		    }

		    Certificate[] certs = entry.getCertificates();
		    
		    if (null != certs && certs.length > 0) {		    
		    	for (Certificate cert : certs) {
		               String alias = verify(cert, certMap);
		               if (null == alias) {
		                   System.out.println("Verification failed.");
		               } else {
		            	   System.out.println("verification successful.");
		               }
		           }// for
		      }
		} // while	
	}
	
    private static void drain(InputStream in) throws IOException {
        byte[] buf = new byte[512];
        while (-1 != in.read(buf))
            ;
    }		

    private static String verify(Certificate cert, HashMap<String, Certificate> map) {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String alias = it.next();
            try {
            	
            	Certificate certStandard = map.get(alias);
            	PublicKey pubKey = certStandard.getPublicKey();
                cert.verify(pubKey);
                return alias;
            } catch (Exception e) {
            	e.printStackTrace();
                continue;
            }
        }// while

        return null;
    }    
}

