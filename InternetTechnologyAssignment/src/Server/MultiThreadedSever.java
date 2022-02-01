package Server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultiThreadedSever implements Runnable {
    protected int          serverPort   = 6969;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(5);
    Hashtable<String, Hashtable<String, String>> keystore = new Hashtable<>();
    Hashtable<String, String> authStore = new Hashtable<>();

    public MultiThreadedSever(int port){

        this.serverPort = port;
        authStore.put("an12", "1234");
        authStore.put("am99", "4567");
        authStore.put("aa66", "2233");
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private synchronized boolean isStopped(){
        return this.isStopped;
    }

    private void openSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open reqd. port", e);
        }
    }

    @Override
    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println(clientSocket.getPort());
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            this.threadPool.execute(
                    new WorkerRunnable(clientSocket,
                            "Thread Pooled Server", keystore, authStore));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }
}
