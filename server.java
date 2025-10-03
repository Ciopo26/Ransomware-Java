package serverPazzo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class server {

	public static void main(String[] args) {
		String percorso = "C:\\Users\\Federico\\chiave.txt";
		//inizializzazione server
		try(ServerSocket socket = new ServerSocket(5000)){
			System.out.println("Server Ora in ascolto sulla porta 5000");
		
			//accetta connessioni
			while(true) {
				Socket client = socket.accept();
				System.out.println("Connessione accettata da " + client.getInetAddress());
				
				//ricezione dati
				DataInputStream inputStream = new DataInputStream(client.getInputStream());
	            String receivedMessage = inputStream.readUTF();
	            System.out.println("Messaggio ricevuto: " + receivedMessage);
	            
	            //salvataggio in file di testo
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(percorso, true))) {
	                writer.write(receivedMessage+" IP: "+client.getInetAddress());
	                writer.newLine(); // Aggiunge una nuova riga dopo il messaggio
	                System.out.println("Messaggio salvato in: " + percorso);
	            } catch (Exception e) {
	                System.err.println("Errore durante la scrittura nel file: " + e.getMessage());
	            }
				
			}			
			
		}catch(Exception e) {
			System.out.println("Errore server");
		}
	}	
}

