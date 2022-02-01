package Server;

public class Main {
    public static void main(String[] args) {
        MultiThreadedSever server = new MultiThreadedSever(6969);
        server.run();
    }
}
