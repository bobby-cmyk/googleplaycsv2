package googleplaycsvv2.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import googleplaycsvv2.csvfilehandler.CsvHandler;

public class ServerMain {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // Validate command line args to ensure user provide 2 args: port and csv path, else exit program
        if (args.length != 2) {
            System.err.println(">>> Proper usage: <Port> <CSV Path>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        String csvPath = args[1];

        CsvHandler csvHandler = new CsvHandler();

        // Read file provided by user
        csvHandler.read(csvPath);

        // Initilise server socket
        ServerSocket server = new ServerSocket(port);

        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        while (true) {
            // Wait for connection
            System.out.printf(">>> Waiting for connection at port: %d\n", port);
            Socket sock = server.accept();

            // Connected to client n
            System.out.printf(">>> Connected to client\n");

            ConnectionHandler worker = new ConnectionHandler(sock, csvHandler);
            
            threadPool.submit(worker);
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


 // File reading and processing
 // Client and server basic networking
 // Multi-threading