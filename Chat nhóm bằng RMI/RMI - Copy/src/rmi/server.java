package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Gói tin
class MessagePacket implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public MessagePacket(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender + ": " + content;
    }
}

// Interface cho Client
interface ChatClient extends Remote {
    void receiveMessage(MessagePacket packet) throws RemoteException;
}

// Interface cho Server
interface ChatServer extends Remote {
    void registerClient(ChatClient client, String name) throws RemoteException;
    void broadcastMessage(MessagePacket packet) throws RemoteException;
}

// Triển khai Server
public class Server extends UnicastRemoteObject implements ChatServer {
    private static final long serialVersionUID = 1L;
    private Map<String, ChatClient> clients = new ConcurrentHashMap<>();

    protected Server() throws RemoteException { super(); }

    @Override
    public synchronized void registerClient(ChatClient client, String name) throws RemoteException {
        clients.put(name, client);
        System.out.println(name + " đã tham gia.");
        broadcastMessage(new MessagePacket("Server", name + " đã tham gia!"));
    }

    @Override
    public synchronized void broadcastMessage(MessagePacket packet) throws RemoteException {
        System.out.println("Broadcast: " + packet);
        for (ChatClient client : clients.values()) {
            try { client.receiveMessage(packet); }
            catch (RemoteException e) { e.printStackTrace(); }
        }
    }

    // Main để chạy server
    public static void main(String[] args) {
        try {
            // Nếu chạy LAN: thay "localhost" bằng IP server hoặc set VM arg -Djava.rmi.server.hostname=...
            System.setProperty("java.rmi.server.hostname", "localhost");
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ChatServer", server);
            System.out.println("Server ready on port 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
