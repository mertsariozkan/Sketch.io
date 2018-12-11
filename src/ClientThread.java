import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    boolean listening = true;
    public ClientThread(Socket socket, BufferedReader input, PrintWriter output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public synchronized void run() {
        while (listening) {
            try {
                String message;
                while ((message = input.readLine()) != null) {
                    //System.out.println(message);
                    for (PrintWriter o : Server.outputs) {
                        o.println(message);
                        o.flush();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Closing socket/streams
            finally {

                output.close();
                try {
                    Server.outputs.remove(output);
                    input.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }



    }
}
