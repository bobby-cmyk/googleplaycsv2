package googleplaycsvv2.server;

import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import googleplaycsvv2.csvfilehandler.CsvHandler;

public class ConnectionHandler implements Runnable{

    private final Socket sock;
    private final CsvHandler csvHandler;

    public ConnectionHandler(Socket sock, CsvHandler csvHandler) {
        this.sock = sock;
        this.csvHandler = csvHandler;
    }

    @Override
    public void run() {

        try {
            // Initialise input and output streams to wait for command and send back response
            InputStream is = sock.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            OutputStream os = sock.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            while (true) {
                // Wait to read command from client
                String input = dis.readUTF();

                // Validate the command provided by user
                String[] parts = input.split(" ");

                String command = "";

                String category = "";

                String response = "";

                if (parts.length == 1) {
                    command = parts[0]; 
                }
                
                else if (parts.length == 2) {
                    command = parts[0];
                    category = parts[1];
                }

                else {
                    // Command does not exist
                    response = "Command does not exist";
                }
            
                // check if command provided is either list, quit, max, min, or avg
                if (command.equals("list")) {
                    response = csvHandler.getCategories();
                }

                else if (command.equals("quit")) {
                    // Close socket when quit is given
                    response = "quit";
                    dos.writeUTF(response);
                    dos.flush();
                    break;
                }

                else if (command.equals("max") || command.equals("min") || command.equals("avg")) {
                    if (csvHandler.isValidCategory(category)) {
                        if (command.equals("max")) response = csvHandler.getMax(category);
                        else if (command.equals("min")) response = csvHandler.getMin(category);
                        else if (command.equals("avg")) response = csvHandler.getAvg(category);
                        else response = "Command does not exist!";
                    }
                    else {
                        response = "Category does not exist!";
                    }
                }
                else {
                    response = "Command does not exist";
                }

                // Send response to client
                dos.writeUTF(response);
                dos.flush();
            }

            dis.close();
            bis.close();
            is.close();

            dos.close();
            bos.close();
            os.close();
                
            sock.close();
        }

        catch (IOException e) {
            e.getStackTrace();
        }
        
    }
    
}
