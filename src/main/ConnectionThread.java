package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConnectionThread extends Thread  {
    private ArrayList<ConnectionThread> connectionThreads;
    private BufferedReader in;
    private PrintWriter out;
    private String name = null ;
    private Socket socket ;

    public ConnectionThread(ArrayList<ConnectionThread> connectionThreads, Socket socket) throws Exception {
        this.connectionThreads = connectionThreads;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream() , true);
        connectionThreads.add(this);
        this.start();
    }

    @Override
    public void run(){
        try {
            // setting user name
            out.println("saisir votre Id : ");
            name = in.readLine();
            System.out.println("Connection established with : " + name);


            // getting other users name

            String names = connectionThreads.stream()
                    .filter(e -> (e != null && e.name != null && !this.name.equals(e.name)))
                    .map(e -> "\"" + e.name + "\"")
                    .collect(Collectors.joining(" et "));


            // printing the welcome message
            if (names.equals(""))
                out.println("Server >> Bienvenue " + name + " vous êtes bien connectés");
            else if (!names.contains("et"))
                out.println("Server >> Bienvenue " + name + " vous êtes bien connectés : " + names + " est aussi connectés");
            else
                out.println("Server >> Bienvenue " + name + " vous êtes bien connectés : " + names + " sont aussi connectés");

            // setting chat logic
            while (true) {
                String data = in.readLine();
                if (data == null || data.equals("Quit")) {
                    this.disconnect();
                    break;
                }

                // internal log
                System.out.println("## spreading message from " + this.name + " ::  " + data);

                // sending msg to others
                for (ConnectionThread e : connectionThreads) {
                    if (e != this)
                        e.out.println(this.name + " >> " + data);
                }
            }
            System.out.println("Connection ended with : " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws Exception {
        out.close();
        in.close();
        socket.close();
        this.connectionThreads.remove(this);
    }
}
