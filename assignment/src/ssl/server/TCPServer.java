package ssl.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class TCPServer {

    private static int PORT;

    private final static String algorithm = "SSL";
    private final static String implementation = "SunX509";
    private final static String kind = "JKS";
    private final static String file = "../../../keystore.jks";

    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java TCPServer <Port number>");
            System.exit(1);
        }

        PORT = Integer.parseInt(args[0]);
    
        try {
            SSLContext context = SSLContext.getInstance(algorithm);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(implementation);
            KeyStore keyStore = KeyStore.getInstance(kind);

            System.out.print("Enter keystore password: ");
            char[] password = System.console().readPassword();
            keyStore.load(new FileInputStream(file), password);
            keyManagerFactory.init(keyStore, password);
            context.init(keyManagerFactory.getKeyManagers(), null, null);

            // wipe the password 
            Arrays.fill(password, '0');

            SSLServerSocketFactory factory = context.getServerSocketFactory();

            SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);

            String[] supported = server.getSupportedCipherSuites();
            String[] anonCipherSuitesSupported = new String[supported.length];
            int numAnonCipherSuitesSupported = 0;
            for (int i = 0; i < supported.length; i++) {
                if (supported[i].indexOf("_anon_") > 0) {
                    anonCipherSuitesSupported[numAnonCipherSuitesSupported++] = supported[i];
                }
            }

            String[] oldEnabled = server.getEnabledCipherSuites();
            String[] newEnabled = new String[oldEnabled.length + numAnonCipherSuitesSupported];
            System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
            System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length, numAnonCipherSuitesSupported);

            server.setEnabledCipherSuites(newEnabled);

            System.out.println("Server ready and awaiting clients...");

            while (true) {
                try {
                    Socket client = server.accept();
                    new Thread(new TCPServerRunnable(client)).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}