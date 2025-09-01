import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String ligne = in.readLine();
                if (ligne == null) break;
                System.out.println(ligne);
                String reponse = clavier.readLine();
                out.println(reponse);
                if (ligne.equals("Bravo, vous avez deviné le nombre !")) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
