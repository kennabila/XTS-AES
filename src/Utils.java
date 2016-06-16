/*
 * Kelas yang merepresentasikan utility utk mengolah format data
 * @author Ken Nabila Setya-1306413252
 * @author Rahmi Julianasari-1306381710
 */	
public class Utils {
	public static final char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	/*
	 * Method untuk mengubah suatu string menjadi byte[]
	 * @param : str adalah string yang akan diubah menjadi tipe data array of byte
	 * @return array of byte
	 */
	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		} else if (str.length() < 2) {
			return null;
		} else {
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++) {
				buffer[i] = (byte) Integer.parseInt(
						str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}

	}
	
	/*
	 * Method untuk mengubah bentuk array of byte menjadi String
	 * @param : ba adalah array of byte yang akan diubah menjadi tipe data String
	 * @return String hasil convert
	 */
	 public static String bytesToHex (byte[] ba) {
		 int length = ba.length;
	     char[] buf = new char[length * 2];
	     for (int i = 0, j = 0, k; i < length; ) {
	         k = ba[i++];
	         buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
	         buf[j++] = HEX_DIGITS[ k        & 0x0F];
	     }
	     return new String(buf);
	}	
}
