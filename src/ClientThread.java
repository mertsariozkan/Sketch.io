import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientThread(Socket socket, BufferedReader input, PrintWriter output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message;
                while ((message = input.readLine()) != null) {

                    for (PrintWriter o : Server.outputs) {
                        o.println(message);
                        o.flush();
                    }

                }
            } catch (IOException e) {
                System.out.println("A client disconnected.");
                Server.outputs.remove(output);
                break;
            }

        }



    }
}
