import java.io.*;
import java.net.*;

public class EchoClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server");
            System.out.println("Server says: " + in.readLine());

            String userMessage;
            do {
                System.out.print("Enter message (QUIT to exit): ");
                userMessage = userInput.readLine();
                out.println(userMessage);
                System.out.println("Server response: " + in.readLine());
            } while (!userMessage.equalsIgnoreCase("QUIT"));

            System.out.println("Closing connection...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


