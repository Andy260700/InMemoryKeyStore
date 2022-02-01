package Server;

import java.io.*;
import java.net.Socket;
import java.util.Hashtable;


public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    protected Hashtable<String,Hashtable<String,String>> keyStore;
    protected Hashtable<String, String > authStore;

    public WorkerRunnable(Socket clientSocket, String serverText, Hashtable<String,Hashtable<String,String>> keyStore, Hashtable<String,String> authStore) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.keyStore = keyStore;
        this.authStore = authStore;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            String client_in = "";
            String userid = Thread.currentThread().getName();
            do
            {
                client_in = reader.readLine();
                System.out.println("received from client "+ userid+": "+client_in);
                String tokens[] = client_in.split(" ");


                if(tokens[0].equals("exit"))
                {
                    System.out.println("exiting... "+userid);
                    if(userid.equals(Thread.currentThread().getName())){
                        keyStore.remove(userid);
                    }
                }
                else if(tokens[0].equals("login")){
                    if(authStore.get(tokens[1]).equals(tokens[2])){
                        System.out.println("client "+userid+" logged in as "+tokens[1]);
                        userid = tokens[1];
                        writer.println("yes");
                    }
                    else{
                        writer.println("no");
                    }
                }
                else if (tokens[0].equals("get")) {
                    String key = tokens[1];
                    String res = "";
                    if (keyStore.containsKey(userid))
                        res = keyStore.get(userid).getOrDefault(key, "");
                    else {
                        keyStore.put(userid, new Hashtable<>());
                    }

                    writer.println(res);
                } else if (tokens[0].equals("put")){
                    if (keyStore.containsKey(userid))
                        keyStore.get(userid).put(tokens[1], tokens[2]);
                    else {
                        keyStore.put(userid, new Hashtable<>());
                        keyStore.get(userid).put(tokens[1], tokens[2]);
                    }
                }
                else{
                    if(!userid.equals(Thread.currentThread().getName())){
                        writer.println(keyStore);
                    }
                }
            }
            while (!client_in.equals("exit"));


                reader.close();
                writer.close();
//            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
