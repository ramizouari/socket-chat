package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String [] arg)  {
        // init
        Socket socket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        String name = "nonName";

        try {
             socket = new Socket("localhost" ,8070);
             socketOut = new PrintWriter(socket.getOutputStream() , true);
             socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Could not connect");
            e.printStackTrace();
        }

        Scanner keyboard = new Scanner(System.in);

        // setting the name
        try {
            System.out.print(socketIn.readLine());
            name = keyboard.nextLine();
            socketOut.println(name);
            System.out.println(socketIn.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // socket output listening thread
        new ClientThread(socketIn).start();

        // send message to socket
        while (true) {
            String c = keyboard.nextLine();
            socketOut.println(c);
            if (c.equals("Quit"))
                break;
        }
    }
}
