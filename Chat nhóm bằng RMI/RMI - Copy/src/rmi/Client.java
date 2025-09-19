package rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Scanner;

// Client implement interface
public class Client extends UnicastRemoteObject implements ChatClient {
    private static final long serialVersionUID = 1L;
    private String name;
    private ChatServer server;

    protected Client(String name, ChatServer server) throws RemoteException {
        super();
        this.name = name;
        this.server = server;
        server.registerClient(this, name);
    }

    @Override
    public void receiveMessage(MessagePacket packet) throws RemoteException {
        System.out.println(packet);
    }

    public void startChat() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String msg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(msg)) break;
                MessagePacket packet = new MessagePacket(name, msg);
                server.broadcastMessage(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main để chạy client
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Nhập tên: ");
            String name = scanner.nextLine();

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatServer server = (ChatServer) registry.lookup("ChatServer");

            Client client = new Client(name, server);
            client.startChat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
