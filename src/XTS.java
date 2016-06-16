import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/*
 * Kelas yang merepresentasikan XTS
 * @author Ken Nabila Setya-1306413252
 * @author Rahmi Julianasari-1306381710
 */	
public class XTS {
	private String file;
	private String out;
	private int block_size;
	private int key_length_hex;
    private byte[][] multiplyAlpha; //menyimpan nilai alpha dan alpha pangkat j dlm matriks
	private byte[] nonce = Utils.hexToBytes("12345678901234567890123456789012");
	private int m; //jumlah block - 1
	private int b; //byte tersisa di block terakhir
	private byte[] key1;
	private byte[] key2;
	
	public XTS(String file, String key, String out) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		//inisialisasi
		this.file = file;
		this.block_size = 16; 		
		this.key_length_hex= 64; 
		this.out = out;
		
		//membaca key
		BufferedReader brKey = new BufferedReader(new FileReader(key));
		String read = brKey.readLine();
		this.key1 = Utils.hexToBytes(read.substring(0,key_length_hex/2));
		this.key2 = Utils.hexToBytes(read.substring(key_length_hex/2,read.length()));
        brKey.close();
        
        //membaca file input
		RandomAccessFile brFile = new RandomAccessFile(file, "r");
		long fileSize = brFile.length();
		brFile.close();
        this.m = (int) (fileSize / block_size);
        this.b = (int) (fileSize % block_size);
        
        //panggil method untuk membuat matriks alpha
        //menyimpan nilai alpha dan alpha pangkat j dlm matriks
        AES aes = new AES(this.key2);
		multiplyAlpha(aes.encrypt(nonce));
	}
	
	/*
	 * method enkripsi file input dengan key
	 * lalu mengembalikan hasil langsung ke dalam file output 
	 */
	public void encrypt() throws Exception{
		//baca input berdasarkan path file yang disimpan 
		RandomAccessFile brFile = new RandomAccessFile(file, "r");
        byte[][] input = new byte[m+1][block_size];
        input[m] = new byte[b];
        byte[][] output = new byte[m+1][block_size];
        output[m] = new byte[b];
        for (int i = 0; i<input.length; i++) {
            brFile.read(input[i]);
        }

        //proses XTS-Aes encryption
        for (int q=0; q<=m-2; q++){
        	output[q] = blockEnc(key1, key2, input[q], q);
        }
        
        //jika tidak ada byte tersisa di block terakhir
        if (b==0){
        	output[m-1] = blockEnc(key1, key2, input[m-1], m-1);
        	output[m] = new byte[0];
        }
        //jika ada sisa byte di block terakhir
        //lakukan padding pada sisa byte
        else{
        	byte[] cc= blockEnc(key1, key2, input[m-1], m-1);
        	System.arraycopy(cc, 0, output[m], 0, b);
        	byte[] cp = new byte[block_size-b];
        	for(int i=b; i<block_size; i++)
        		cp[i-b] = cc[i];

        	byte[] pp = new byte[input[m].length + cp.length];
        	System.arraycopy(input[m], 0, pp, 0, input[m].length);
        	System.arraycopy(cp, 0, pp, input[m].length, cp.length);
        	
        	output[m-1] =  blockEnc(key1, key2, pp, m);
        	
        }
        
        brFile.close();
        
        //Memasukan output kedalam file
        RandomAccessFile brOut = new RandomAccessFile(out, "rw");
        for (int i = 0; i<output.length; i++) {
        	for(int j =0; j<output[i].length; j++)
    			brOut.write(output[i][j]);
        }
        brOut.close();
    }
	
	/*
	 * method yang merepresentasikan proses enkripsi per block 
	 * @param key1
	 * @param key2
	 * @param p merupakan block array yang akan di enkripsi
	 * @param j untuk menunjukan pangkat dari alpha
	 * @return block hasil enkripsi
	 */
	public byte[] blockEnc(byte[] key1, byte[] key2, byte[] p, int j) throws Exception{
		AES aes= new AES(key2);
		byte[] t = multiplyAlpha[j];
		byte[] pp = xortweaktext(t, p); 
		aes= new AES(key1);
		byte[] cc = aes.encrypt(pp);
		byte[] c = xortweaktext(t, cc);  
		
		return c;
	}

	/*
	 * method dekripsi file input dengan key
	 * lalu mengembalikan hasil langsung ke dalam file output 
	 */
	public void decrypt() throws Exception{		
		//baca input berdasarkan path file yang disimpan 
		RandomAccessFile brFile = new RandomAccessFile(file, "r");
        byte[][] input = new byte[m+1][block_size];
        input[m] = new byte[b];
        byte[][] output = new byte[m+1][block_size];
        output[m] = new byte[b];
        for (int i = 0; i<input.length; i++) {
            brFile.read(input[i]);
        }
        //proses XTS-Aes decryption
        for (int q=0; q<=m-2; q++){
        	output[q] = blockDec(key1, key2, input[q], q);
        }
        //jika tidak ada byte tersisa di block terakhir
        if (b==0){
        	output[m-1] = blockDec(key1, key2, input[m-1], m-1);
        	output[m] = new byte[0];
        }
        //jika ada sisa byte di block terakhir
        else{
        	byte[] pp= blockDec(key1, key2, input[m-1], m);
        	System.arraycopy(pp, 0, output[m], 0, b);
        	byte[] cp = new byte[block_size-b];
        	for(int i=b; i<block_size; i++)
        		cp[i-b] = pp[i];

        	byte[] cc = new byte[input[m].length + cp.length];
        	System.arraycopy(input[m], 0, cc, 0, input[m].length);
        	System.arraycopy(cp, 0, cc, input[m].length, cp.length);
        	
        	output[m-1] =  blockDec(key1, key2, cc, m-1);
        	
        }
        
        brFile.close();
        
        //Memasukan output kedalam file
        RandomAccessFile brOut = new RandomAccessFile(out, "rw");
        for (int i = 0; i<output.length; i++) {
        	for(int j=0; j<output[i].length; j++)
        		brOut.write(output[i][j]);
        }
        brOut.close();
    }

	/*
	 * method yang merepresentasikan proses dekripsi per block 
	 * @param key1
	 * @param key2
	 * @param c merupakan block array yang akan di dekripsi
	 * @param j untuk menunjukan pangkat dari alpha
	 * @return block hasil dekripsi
	 */
	public byte[] blockDec(byte[] key1, byte[] key2, byte[] c, int j) throws Exception{	
		AES aes= new AES(key2);
		byte[] t = multiplyAlpha[j];
		byte[] cc = xortweaktext(t, c);
		aes= new AES(key1);
		byte[] pp = aes.decrypt(cc);
		byte[] p = xortweaktext(t, pp);  
		
		return p;
		
	}
	
	/*
	 * method unutk menghitung alpha pangkat 0 hingga j, kemudian hasilnya disimpan dalam matriks
	 * sehingga nilai alpha pangkat j nanti tinggal dipanggil saja dengan multyply[j]
	 * @param tweakEncrypt/nonce
	 */
	public void multiplyAlpha(byte[] tweakEncrypt) {
        byte[][] multiplyDP = new byte[m+1][block_size];
        multiplyDP[0] = tweakEncrypt;
        for (int i = 1; i < m+1; i++) {
            multiplyDP[i][0] = (byte) ((2 * (multiplyDP[i-1][0] % 128)) ^ (135 * (multiplyDP[i-1][15] / 128)));
            for (int k = 1; k < 16; k++) {
                multiplyDP[i][k] = (byte) ((2 * (multiplyDP[i-1][k] % 128)) ^ (multiplyDP[i-1][k - 1] / 128));
            }
        }
        this.multiplyAlpha =  multiplyDP;
    }
    
	/*
	 * method unutk menghitung melakukan xor per byte antara tweak dan text
	 * @param tweakEncypt
	 * @paa textBlock merupakan block yang akan di xor dengan tweak
	 * @return array of byte hasil xor
	 */
    public byte[] xortweaktext (byte[] tweakEncrypt, byte[] textBlock){
    	byte[] result = new byte[16];
    	for(int i=0; i<tweakEncrypt.length; i++){
    		result[i] = (byte)(tweakEncrypt[i]^textBlock[i]);
    	}
    	return result;
    }
   
}
