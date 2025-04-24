import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5000;
    private static String username = null;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(in.readLine()); // Read welcome message

            // Thread to listen for server messages
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server");
                }
            }).start();

            // Main input loop
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("exit")) {
                    out.println("LOGOUT");
                    break;
                }
                else if (input.startsWith("login ")) {
                    out.println("LOGIN:" + input.substring(6));
                    username = input.substring(6).split(":")[0]; // Store username
                }
                else if (input.startsWith("book ")) {
                    if (username == null) {
                        System.out.println("Please login first");
                        continue;
                    }
                    // Format: book movie:theater:date:time:tickets
                    out.println("BOOK:" + input.substring(5));
                }
                else {
                    out.println(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}