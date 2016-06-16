import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;

import java.awt.Label;  
/*
 * Kelas GUI dari XTS-AES
 * Kelas ini merupakan kelas utama dari program
 * 
 * @author Ken Nabila Setya-1306413252
 * @author Rahmi Julianasari-1306381710
 */	
public class AESFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AESFrame frame = new AESFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AESFrame() {
		setTitle("XTS-AES");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 482, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 36, 446, 213);
		contentPane.add(tabbedPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("Encryption", null, layeredPane, null);
		
		JLabel lblFilePlainText = new JLabel("Plain Text File");
		lblFilePlainText.setBounds(20, 22, 83, 20);
		layeredPane.add(lblFilePlainText);
		
		JLabel lblFileKeykey = new JLabel("Key File");
		lblFileKeykey.setBounds(20, 60, 83, 20);
		layeredPane.add(lblFileKeykey);
		
		final JLabel pathPlainText = new JLabel("(no file selected)");
		pathPlainText.setBounds(122, 22, 110, 20);
		layeredPane.add(pathPlainText);
		
		final JLabel pathKeyEncrypt = new JLabel("(no file selected)");
		pathKeyEncrypt.setBounds(122, 60, 98, 20);
		layeredPane.add(pathKeyEncrypt);
		
		JButton btnPlainText = new JButton("browse");
		btnPlainText.setBounds(317, 21, 83, 23);
		layeredPane.add(btnPlainText);
		
		JButton btnKeyEncrypt = new JButton("browse");
		btnKeyEncrypt.setBounds(317, 59, 83, 23);
		layeredPane.add(btnKeyEncrypt);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Decryption", null, layeredPane_1, null);
		
		final JLabel pathCiphertext = new JLabel("(no file selected)");
		pathCiphertext.setBounds(122, 24, 172, 22);
		layeredPane_1.add(pathCiphertext);
		
		final JLabel pathKeyDecrypt = new JLabel("(no file selected)");
		pathKeyDecrypt.setBounds(122, 60, 172, 22);
		layeredPane_1.add(pathKeyDecrypt);
		
		JButton btnPathCipher = new JButton("Browse");
		btnPathCipher.setBounds(322, 24, 79, 22);
		layeredPane_1.add(btnPathCipher);
		
		JButton btnKeyDecrypt = new JButton("Browse");
		btnKeyDecrypt.setBounds(322, 60, 79, 22);
		layeredPane_1.add(btnKeyDecrypt);
		
		JLabel lblNewLabel_1 = new JLabel("Cipher Text File");
		lblNewLabel_1.setBounds(26, 21, 100, 28);
		layeredPane_1.add(lblNewLabel_1);
		
		JLabel lblFilekeydecrypt = new JLabel("Key File");
		lblFilekeydecrypt.setBounds(26, 60, 100, 22);
		layeredPane_1.add(lblFilekeydecrypt);
		
		Label label = new Label("XTS-AES");
		label.setAlignment(Label.CENTER);
		label.setFont(new Font("Amaranth", Font.PLAIN, 20));
		label.setBounds(25, 10, 414, 25);
		contentPane.add(label);
		btnKeyEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(pathKeyEncrypt);
			}
		});
		btnPlainText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(pathPlainText);
			}
		});
		
		btnKeyDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(pathKeyDecrypt);
			}
		});
		btnPathCipher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseFile(pathCiphertext);
				
			}
		});
		
		JButton btnDecryptAndSave = new JButton("Decrypt and Save to File");
		btnDecryptAndSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					EnDecSave(pathCiphertext, pathKeyDecrypt,true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnDecryptAndSave.setBounds(122, 110, 209, 35);
		layeredPane_1.add(btnDecryptAndSave);
		
		JButton btnEncrypt = new JButton("Encrypt and Save to File");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					EnDecSave(pathPlainText, pathKeyEncrypt, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnEncrypt.setBounds(120, 115, 209, 35);
		layeredPane.add(btnEncrypt);
		
	}
	
	/*
	 * Method untuk memilif file untu menyimpan keluaran program 
	 * @param : result merupakan hasil output yang akan disimpan
	 */
	public String saveMap() {
	    JFileChooser chooser = new JFileChooser();
	    int retrival = chooser.showSaveDialog(null);
	    if (retrival == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getAbsolutePath();
	    }
	    return null;
	}
	
	/*
	 * Method untuk membuka file browser dengan JFile chooser
	 */
	public void chooseFile(JLabel file){
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    file.setText(selectedFile.getAbsolutePath());
		}
	}
	
	
	/*
	 * Method untuk mengecek apakah file sudah benar dan tidak kosong,
	 * untuk mengecak masukan key dan panjang key, jika kosong akan
	 * mengembalikan Exception
	 * jika tidak kosong kemudian akan men-save hasil ke file yg diinginkan
	 */
	public void EnDecSave(JLabel file1, JLabel file2, boolean dec) throws Exception{
		try {
			
			if(file1.getText().equals("(no file selected)") || 
				file2.getText().equals("(no file selected)")){
				JOptionPane.showMessageDialog(null, "Files can not be empty", "Choose Files!", JOptionPane.INFORMATION_MESSAGE);	
			}
			else{
				FileReader fr = new FileReader(file2.getText());
				BufferedReader reader = new BufferedReader(fr);		
				String key = reader.readLine();
				
				//cek length dari key
				if (key.length()==64 && key.matches("-?[0-9a-fA-F]+")){
					//enkripsi
					
					String result = saveMap();
					XTS xts = new XTS(file1.getText(), file2.getText(), result);	
					if(dec)
						xts.decrypt();	
					else
						xts.encrypt();		
					
					JOptionPane.showMessageDialog(null, "File was successfully processed", "Success", JOptionPane.INFORMATION_MESSAGE);

				}
				else{
					JOptionPane.showMessageDialog(null, "Key File must be filled with 64 digits Hex", "Wrong Input!", JOptionPane.INFORMATION_MESSAGE);
				}
				
				reader.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
