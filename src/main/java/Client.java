import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket(Main.HOST, Main.PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Введите слово для поиска");
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            out.println(word);
            String searchResults;
            while ((searchResults = in.readLine()) != null) {
                System.out.println(searchResults);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}