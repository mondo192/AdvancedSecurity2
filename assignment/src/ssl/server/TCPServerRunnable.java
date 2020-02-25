package ssl.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class TCPServerRunnable implements Runnable {

    private Socket client;

    public TCPServerRunnable(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            out.println("SERVER> Secure connection established");

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}