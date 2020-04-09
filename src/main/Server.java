package main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] arg) {
        ArrayList<ConnectionThread> connectionThreads = new ArrayList<>();
        ServerSocket mainSocket;
        try {
            mainSocket = new ServerSocket(8070);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            try {
                Socket s = mainSocket.accept();
                new ConnectionThread(connectionThreads, s);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
