package main;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientThread extends Thread {
    BufferedReader in;

    public ClientThread(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String data = in.readLine();
                if (data == null) {
                    System.out.println(MyColor.ANSI_RED + "connection ended");
                    break;
                }
                MyColor.print(MyColor.ANSI_PURPLE, data + "\n");
            } catch (IOException e) {
                System.out.println(MyColor.ANSI_RED + "connection ended");
                break;
            }
        }
    }
}
