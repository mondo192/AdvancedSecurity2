package ssl.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TCPClient {

    private static String HOST;
    private static int PORT;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java TCPClient <Host name> <Port number>");
            System.exit(1);
        }

        HOST = args[0];
        PORT = Integer.parseInt(args[1]);

        

        try {
            System.setProperty("javax.net.ssl.trustStore", "../../../keystore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "password");
            
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}