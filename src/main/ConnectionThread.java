package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
        try{
            // setting user name
            out.println("saisir votre Id : ");
            name =  in.readLine();
            System.out.println("Connection established with : "+ name );


            // getting other users name
            String names = "" ;
            for (ConnectionThread e : connectionThreads) {
                if (e != null && e.name != null && !this.name.equals(e.name))
                    names += " \" " + e.name + " \" et";
            }
            names = names.substring(0 , Math.max(names.length() -2 , 0));

            // printing the welcome message
            if (names.equals(""))
                out.println("Server >> Bienvenue " +name+" vous êtes bien connectés");
            else
                out.println("Server >> Bienvenue " +name+" vous êtes bien connectés : "+ names + " sont aussi connectés");

            // setting chat logic
            while (true){
                String data  =  in.readLine();
                if (data == null){
                    System.out.println("Connection ended with : " + name);
                    out.close();
                    in.close();
                    socket.close();
                    this.connectionThreads.remove(this);
                    break;
                }
                MyColor.printBlue("spreading message from " +  this.name + " ::  " + data + "\n");

                for (ConnectionThread e : connectionThreads) {
                    if (e != this)
                        e.out.println( this.name + " >> " + data);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
