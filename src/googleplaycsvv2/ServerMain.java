package googleplaycsvv2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        
        // Validate command line args to ensure user provide 2 args: port and csv path, else exit program
        if (args.length != 2) {
            System.err.println(">>> Proper usage: <Port> <CSV Path>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        String csvPath = args[1];

        // Initilise server socket
        try (ServerSocket server = new ServerSocket(port)) {
            
            // Wait for connection
            System.out.printf(">>> Waiting for connection at port: %d\n", port);
            Socket sock = server.accept();

            // Connected to client n
            System.out.printf(">>> Connected to client\n");
            
            // Initialise input and output streams to wait for command and send back response
            InputStream is = sock.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            OutputStream os = sock.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            // Wait to read command from client
            String command = dis.readUTF();

            //TODO: pass command to csvHandler

            // Send response to client
            dos.writeUTF("This is a response from the server");
            dos.flush();

            // Close from the reverse order of opening
            dis.close();
            bis.close();
            is.close();

            dos.close();
            bos.close();
            os.close();
        }

        catch (IOException ie) {
            System.out.printf(">>> Unable to initialise server socket: %s\n", ie.getMessage());
        }
    }
}












// client should be able to choose from a list of categories

// command: <category> <min/max/avg> by rating

// server side: 
// read a csv file from the folder
// wait for commands from client

// single request
// continuous request
// then multi-threaded to handle multiple clients

// client side
// able to send command  command: <category> <min/max/avg> by rating
// need to include an identifier name in args command


/*/
1. work on the server side -> get port and file name from args
2. work on client side -> get port and self-id
3. work on csvreader
5. multithread the program
 */
