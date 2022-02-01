package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private int serverPort = 6969;
    private String ip;
    private Socket clientSocket;
    BufferedReader reader;
    PrintWriter writer;

    public Client(int serverPort, String ip){
        this.serverPort = serverPort;
        this.ip = ip;

    }

    public void eshtablishSocketConnection(){
        try
        {
            this.clientSocket = new Socket(ip,serverPort);
            System.out.println("Connection established with server");
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
//            String s = reader.readLine();
//            System.out.println(s);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot open port "+serverPort, e);
        }
    }

    public void endSocketConnection(){
        try
        {
            writer.println("exit");
            this.reader.close();
            this.writer.close();
            this.clientSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot close port", e);
        }
    }

    public boolean login(String userid, String password){
        writer.println("login "+userid+" "+password);
        String s = "";
        try{
            s = reader.readLine();
        }
        catch (IOException e){
            throw new RuntimeException("get not work", e);
        }

        return s.equals("yes");
    }

    public String get(String key){
        writer.println("get "+key);
        String s = "";
        try{
            s = reader.readLine();
        }
        catch (IOException e){
            throw new RuntimeException("get not work", e);
        }
        return s;
    }

    public void put(String key, String val){
        writer.println("put "+key+" "+val);
    }


    public void getAll(){
        writer.println("getall");
        try{
            String s = reader.readLine();
            System.out.println(s);
        }
        catch (IOException e){
            throw new RuntimeException("get not work", e);
        }
    }

}
