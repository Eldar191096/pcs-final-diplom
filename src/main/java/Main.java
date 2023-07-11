import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.io.IOException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final String PDF = "pdfs";
    public static final int PORT = 8989;
    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(PDF));
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Запуск сервера");
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                ) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String request = gson.toJson(engine.search(in.readLine()));
                    out.println(request);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
