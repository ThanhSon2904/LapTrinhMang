import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Chat Server...");
            System.out.println("Server port: " + Constants.SERVER_PORT);
            System.out.println("Server name: " + Constants.SERVER_NAME);
            
            // Tạo RMI registry
            Registry registry = LocateRegistry.createRegistry(Constants.SERVER_PORT);
            System.out.println("RMI Registry created on port " + Constants.SERVER_PORT);
            
            // Tạo service và đăng ký
            ChatService chatService = new ChatServiceImpl();
            registry.rebind(Constants.SERVER_NAME, chatService);
            
            System.out.println("Chat server is running successfully!");
            System.out.println("Service bound as: " + Constants.SERVER_NAME);
            System.out.println("Press Ctrl+C to stop the server...");
            
            // Giữ server chạy
            while (true) {
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}