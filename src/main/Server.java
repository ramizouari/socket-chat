package main;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String [] arg) throws Exception {
         ArrayList<ConnectionThread> connectionThreads = new ArrayList<>();
        ServerSocket mainSocket = new ServerSocket(8070);
        while (true){
            try{
                Socket s  = mainSocket.accept() ;
                ConnectionThread c = new ConnectionThread( connectionThreads , s );
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
