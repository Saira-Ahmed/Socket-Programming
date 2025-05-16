import java.io.*;
import java.net.*;

public class ChatRoomClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chatroom");
            System.out.println("Server says: " + in.readLine());

            Thread listener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);  // This ensures each message is printed on a new line
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            listener.start();

            String userMessage;
            do {
                System.out.print("Enter message (QUIT to exit): ");
                userMessage = userInput.readLine();
                out.println(userMessage);  // No need for extra "\n" here
            } while (!userMessage.equalsIgnoreCase("QUIT"));

            System.out.println("Exiting chatroom...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
