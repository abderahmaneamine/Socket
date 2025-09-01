import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Le serveur est à l'écoute sur le port 8888.");
            Random random = new Random();
            
            int nombre = random.nextInt(100) + 1;
            System.out.println("Le nombre aléatoire est : " + nombre);
            int j=1;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client "+j+" a connecté.");
                Thread thread = new Thread(new Service(clientSocket, nombre));
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}

class Service implements Runnable {
    private Socket clientSocket;
    private int nombre;
    
    public Service(Socket clientSocket, int nombre) {
        this.clientSocket = clientSocket;
        this.nombre = nombre;
    }
    
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Entrez un nombre entre 1 et 100 :");
            while (true) {
                String ligne = in.readLine();
                if (ligne == null) break;
                int nombreClient = Integer.parseInt(ligne);
                if (nombreClient == nombre) {
                    out.println("Bravo !");
                    break;
                } else if (nombreClient < nombre) {
                    out.println("Le nombre est plus grand.");
                } else {
                    out.println("Le nombre est plus petit.");
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}