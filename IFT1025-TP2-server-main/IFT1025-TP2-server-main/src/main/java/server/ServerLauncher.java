package server;

/**
 * ServerLauncher cree un serveur lorsque sa methode main est lancee.
 */
public class ServerLauncher {
    public final static int PORT = 1337;

    /**
     * Demarre un serveur et imprime que celui-ci est demarre.
     * @param args
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}