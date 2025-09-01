import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur {
    private static boolean gameOver = false; // Variable to track game status

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Le serveur écoute sur le port 8888.");
            Random random = new Random();

            int nombre = random.nextInt(100) + 1;
            System.out.println("Le nombre aléatoire est : " + nombre);
            int j = 1;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client " + j + " a connecté.");
                Thread thread = new Thread(new Service(clientSocket, nombre));
                thread.start();

                j++;
            }
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public static synchronized void setGameOver() {
        gameOver = true;
    }

    public static synchronized boolean isGameOver() {
        return gameOver;
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
                

                // Check if game is over
                synchronized (Serveur.class) {
                    if (Serveur.isGameOver()) {
                        out.println("!!! Game Over !!!"); // Send "Game Over" message to client
                        break;
                    }
                }

                int nombreClient;
                try {
                    nombreClient = Integer.parseInt(ligne);
                } catch (NumberFormatException e) {
                    out.println("Ce n'est pas un nombre valide. Veuillez réessayer :");
                    continue;  // Prompt the client again to enter a valid number
                }

                if (nombreClient == nombre) {
                    out.println("Bravooo");
                    synchronized (Serveur.class) {
                        Serveur.setGameOver(); // Set game over status
                    }
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