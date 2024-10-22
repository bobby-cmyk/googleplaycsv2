package googleplaycsvv2.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        // Ensure that client provides both the port and its name in command line, else exit
        if (args.length != 2) {
            System.err.println(">>> Proper usage: <Port> <Client Name>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);
        String clientName = args[1];

        
        Socket sock = new Socket("localhost", port);

        System.out.printf(">>> %s is connected to server at port %d\n", clientName, port);

        // Initialise output and input stream to send and receive to/from server
        OutputStream os = sock.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);
        
        InputStream is = sock.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        // Send a command to the server
        Console cons = System.console();
        
        System.out.print("<-- All Commands -->\n * <list> - show all categories \n * <quit> - quit program \n * <action> <category> - e.g <max> <lifestyle>, <min> <utility>, <avg> <education>\n");

        while (true) {

            System.out.print(">>> Your Command: ");
            // trim for whitespaces, and transform to lowercase
            String input = cons.readLine().trim().toLowerCase();

            dos.writeUTF(input);
            dos.flush();

            String response = dis.readUTF();

            if (response.equals("quit")) {
                System.out.println("Connection has ended");
                break;
            }

            System.out.println(response);
            
        }

        dos.close();
        bos.close();
        os.close();

        dis.close();
        bis.close();
        is.close();

        sock.close();
    }
}
