import java.io.*;
import java.net.*;

public class EchoServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Hello! You are connected to the server.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                if (message.equalsIgnoreCase("QUIT")) {
                    break;
                }
                out.println("Echo: " + message);
            }
            System.out.println("Client disconnected");
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
