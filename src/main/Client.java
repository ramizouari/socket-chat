package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String [] arg)  {
        // init
        Socket socket;
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
        BufferedReader finalSocketIn = socketIn;
        new Thread( () -> {
            while (true) {
                try {
                    String data = finalSocketIn.readLine();
                    if (data == null){
                        System.out.println( MyColor.ANSI_RED + "connection broken");
                        break;
                    }
                    MyColor.print(MyColor.ANSI_PURPLE, data + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        while (true){
            String c = keyboard.nextLine();
            if (c.equals("Quit"))
                break;
            socketOut.println(c);
        }
    }
}
