import java.io.*;
import java.net.*;
import java.util.*;

public class ChatRoomServer {
    private static final int PORT = 1234;
    private static Set<PrintWriter> clientOutputs = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat room server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client joined the chat");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientOutputs.add(out);
                out.println("Hello! Welcome to the chatroom.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

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
                    broadcast("Client " + clientSocket.getPort() + ": " + message);
                }
                System.out.println("Client disconnected");
                clientOutputs.remove(out);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String message) {
            synchronized (clientOutputs) {
                for (PrintWriter writer : clientOutputs) {
                    writer.println(message);
                }
            }
        }
    }
}
