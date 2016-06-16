import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
/*
 * Kelas yang merepresentasikan AES menggunakan library AES di java
 * @author Ken Nabila Setya-1306413252
 * @author Rahmi Julianasari-1306381710
 */	
public class AES {
	
	private String keyHex;

	//Constructor, masukan key untuk AES
	public AES(byte[] keyHex) {
		this.keyHex = Utils.bytesToHex(keyHex);
	}
	
	/*
	 * method enkripsi dengan AES java library
	 * @return byte[] hasil enkripsi
	 */
	public byte[] encrypt(byte[] textHex)throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKey key = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), "AES");
			
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
			
		byte[] result = cipher.doFinal(DatatypeConverter.parseHexBinary(Utils.bytesToHex(textHex)));
			
		return result;
	}
	
	/*
	 * method dekripsi dengan AES java library
	 * @return byte[] hasil dekripsi
	 */
	public byte[] decrypt(byte[] textHex)throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		SecretKey key = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), "AES");
			
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key);
			
		byte[] result = cipher.doFinal(DatatypeConverter.parseHexBinary(Utils.bytesToHex(textHex)));
			
		return result;
	}	
	
}