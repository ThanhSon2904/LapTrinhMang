import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Client extends UnicastRemoteObject implements ChatClient {
    private ChatService chatService;
    private String username;
    private boolean connected = false;
    private ClientGUI gui;

    public Client(ClientGUI gui) throws java.rmi.RemoteException {
        super();
        this.gui = gui;
    }

    @Override
    public void appendMessage(String message) throws java.rmi.RemoteException {
        gui.appendMessage(message);
    }

    @Override
    public void receivePrivateMessage(String sender, String message) throws RemoteException {
        gui.receivePrivateMessage(sender, message);
    }

    @Override
    public void receiveFileNotification(String sender, String fileName) throws RemoteException {
        System.out.println("DEBUG: Notifikasi tệp tin đã được nhận từ server.");
        gui.receiveFileNotification(sender, fileName);
    }
    
    @Override
    public void receiveTypingNotification(String username, boolean isTyping) throws RemoteException {
        gui.receiveTypingNotification(username, isTyping);
    }
    
    @Override
    public void updateOnlineUsers(List<String> onlineUsers) throws RemoteException {
        gui.updateOnlineUsersList(onlineUsers);
    }

    public boolean connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Constants.SERVER_PORT);
            chatService = (ChatService) registry.lookup(Constants.SERVER_NAME);
            connected = true;
            return true;
        } catch (Exception e) {
            System.err.println("Cannot connect to server: " + e.getMessage());
            connected = false;
            return false;
        }
    }

    public boolean register(String username, String password) throws Exception {
        if (!connected && !connectToServer()) {
            throw new Exception("Cannot connect to server");
        }
        return chatService.register(username, password);
    }

    public boolean login(String username, String password) throws Exception {
        if (!connected && !connectToServer()) {
            throw new Exception("Cannot connect to server");
        }
        boolean success = chatService.login(username, password);
        if (success) {
            this.username = username;
            chatService.registerClient(this, this.username);
        }
        return success;
    }

    public void logout() throws Exception {
        if (username != null && connected) {
            chatService.unregisterClient(username);
            chatService.logout(username);
            username = null;
        }
    }

    public void sendMessage(String message) throws Exception {
        if (username != null && connected) {
            chatService.sendMessage(username, message);
        }
    }

    public void sendPrivateMessage(String recipient, String message) throws Exception {
        if (username != null && connected) {
            chatService.sendPrivateMessage(username, recipient, message);
        }
    }

    public void sendFile(String recipient, String filePath) throws Exception {
        if (username != null && connected) {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + filePath);
            }

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] fileData = new byte[(int) file.length()];
                fis.read(fileData);
                chatService.sendFile(username, recipient, file.getName(), fileData);
            }
        }
    }

    public byte[] downloadFile(String fileName) throws Exception {
        if (!connected) {
            throw new Exception("Not connected to server");
        }
        return chatService.downloadFile(fileName);
    }

    public List<String> getChatHistory() throws Exception {
        if (!connected) {
            throw new Exception("Not connected to server");
        }
        return chatService.getChatHistory();
    }
    
    public List<String> getOnlineUsers() throws Exception {
        if (!connected) {
            throw new Exception("Not connected to server");
        }
        return chatService.getOnlineUsers();
    }

    public boolean isConnected() {
        return connected;
    }

    public String getUsername() {
        return username;
    }

    public ChatService getChatService() {
        return chatService;
    }
}