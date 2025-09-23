import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    private Map<String, User> users;
    private List<String> chatHistory;
    private Map<String, ChatClient> clients;
    private Map<String, byte[]> fileStorage;

    public ChatServiceImpl() throws RemoteException {
        super();
        users = new HashMap<>();
        chatHistory = new ArrayList<>();
        clients = new HashMap<>();
        fileStorage = new HashMap<>();
        loadUsers();
        loadChatHistory();
        System.out.println("Chat service initialized. Users: " + users.size() + ", Messages: " + chatHistory.size());
    }

    private void loadUsers() {
        File file = new File(Constants.USERS_FILE);
        if (!file.exists()) {
            System.out.println("Users file not found. Creating new one.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.USERS_FILE))) {
            users = (HashMap<String, User>) ois.readObject();
            System.out.println("Loaded " + users.size() + " users from file.");
        } catch (FileNotFoundException e) {
            System.out.println("Users file not found. Starting with empty user list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Saved " + users.size() + " users to file.");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadChatHistory() {
        chatHistory = FileUtils.loadChatHistory();
        System.out.println("Loaded " + chatHistory.size() + " messages from chat history.");
    }

    @Override
    public synchronized boolean register(String username, String password) throws RemoteException {
        if (users.containsKey(username)) {
            System.out.println("Registration failed: Username '" + username + "' already exists.");
            return false;
        }
        
        String hashedPassword = Integer.toString(password.hashCode());
        users.put(username, new User(username, hashedPassword));
        saveUsers();
        
        System.out.println("User registered: " + username);
        return true;
    }

    @Override
    public synchronized boolean login(String username, String password) throws RemoteException {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(Integer.toString(password.hashCode()))) {
            user.setOnline(true);
            System.out.println("User logged in: " + username);
            return true;
        }
        System.out.println("Login failed for user: " + username);
        return false;
    }

    @Override
    public synchronized void logout(String username) throws RemoteException {
        User user = users.get(username);
        if (user != null) {
            user.setOnline(false);
            System.out.println("User logged out: " + username);
        }
    }

    @Override
    public synchronized void registerClient(ChatClient client, String username) throws RemoteException {
        clients.put(username, client);
        System.out.println("Client '" + username + "' registered for callback.");
        broadcastOnlineUsers();
        broadcastMessage("Server", username + " đã tham gia cuộc trò chuyện.");
    }

    @Override
    public synchronized void unregisterClient(String username) throws RemoteException {
        clients.remove(username);
        System.out.println("Client '" + username + "' unregistered from callback.");
        broadcastOnlineUsers();
        broadcastMessage("Server", username + " đã rời khỏi cuộc trò chuyện.");
    }

    @Override
    public synchronized void sendMessage(String sender, String message) throws RemoteException {
        // Định dạng tin nhắn vẫn giữ nguyên
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	String timestamp = sdf.format(new Date());
    	String formattedMessage = "[" + timestamp + "] " + sender + ": " + message;
        chatHistory.add(formattedMessage);
        FileUtils.saveMessageToHistory(formattedMessage);
        System.out.println("Message from " + sender + ": " + message);

        // Vòng lặp mới: Gửi cho tất cả client NGOẠI TRỪ người gửi
        for (Map.Entry<String, ChatClient> entry : clients.entrySet()) {
            String username = entry.getKey();
            ChatClient client = entry.getValue();

            // Chỉ gửi nếu username của client không phải là người gửi
            if (!username.equals(sender)) {
                try {
                    client.appendMessage(formattedMessage);
                } catch (RemoteException e) {
                    System.err.println("Failed to send message to " + username + ". Removing client.");
                    // Xử lý client bị ngắt kết nối nếu cần
                    clients.remove(username);
                    broadcastOnlineUsers(); // Cập nhật lại danh sách online
                }
            }
        }
    }

    @Override
    public synchronized void sendPrivateMessage(String sender, String recipient, String message) throws RemoteException {
        ChatClient senderClient = clients.get(sender);
        ChatClient recipientClient = clients.get(recipient);

        if (recipientClient != null) {
            try {
                recipientClient.receivePrivateMessage(sender, message);
                System.out.println("Private message from " + sender + " to " + recipient + ": " + message);
            } catch (RemoteException e) {
                System.err.println("Private message failed: recipient '" + recipient + "' is disconnected. Removing.");
                clients.remove(recipient);
                broadcastOnlineUsers();
                if (senderClient != null) {
                    senderClient.appendMessage("Hệ thống: Tin nhắn của bạn đến " + recipient + " không thể gửi được. Người dùng này có thể đã ngoại tuyến.");
                }
            }
        } else {
            System.err.println("Private message failed: recipient '" + recipient + "' not found.");
            if (senderClient != null) {
                 senderClient.appendMessage("Hệ thống: Người dùng '" + recipient + "' không online. Tin nhắn không thể gửi được.");
            }
        }
    }

    @Override
    public synchronized void sendFile(String sender, String recipient, String fileName, byte[] fileData) throws RemoteException {
        String serverFileName = sender + "_" + fileName;
        fileStorage.put(serverFileName, fileData);
        ChatClient recipientClient = clients.get(recipient);
        if (recipientClient != null) {
            recipientClient.receiveFileNotification(sender, fileName);
            System.out.println("File '" + fileName + "' sent from " + sender + " to " + recipient);
        } else {
            System.err.println("File transfer failed: recipient '" + recipient + "' not found.");
        }
    }

    @Override
    public synchronized byte[] downloadFile(String fileName) throws RemoteException {
        return fileStorage.get(fileName);
    }
    
    @Override
    public synchronized void typingNotification(String username, boolean isTyping) throws RemoteException {
        for (Map.Entry<String, ChatClient> entry : clients.entrySet()) {
            if (!entry.getKey().equals(username)) {
                try {
                    entry.getValue().receiveTypingNotification(username, isTyping);
                } catch (RemoteException e) {
                    System.err.println("Lỗi gửi thông báo gõ đến client: " + entry.getKey());
                    clients.remove(entry.getKey());
                    broadcastOnlineUsers();
                }
            }
        }
    }

    @Override
    public synchronized List<String> getChatHistory() throws RemoteException {
        return new ArrayList<>(chatHistory);
    }

    @Override
    public synchronized List<String> getOnlineUsers() throws RemoteException {
        List<String> onlineUsers = new ArrayList<>();
        // Only return users who have a live callback connection
        for (String user : clients.keySet()) {
            onlineUsers.add(user);
        }
        return onlineUsers;
    }
    
    private synchronized void broadcastMessage(String sender, String message) throws RemoteException {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	String timestamp = sdf.format(new Date());
    	String formattedMessage = "[" + timestamp + "] " + sender + ": " + message;
        chatHistory.add(formattedMessage);
        FileUtils.saveMessageToHistory(formattedMessage);
        for (ChatClient client : clients.values()) {
            try {
                client.appendMessage(formattedMessage);
            } catch (RemoteException e) {
                System.err.println("Client " + client + " is disconnected. Removing.");
                clients.remove(client);
                broadcastOnlineUsers();
            }
        }
    }
    
    private synchronized void broadcastOnlineUsers() {
        try {
            List<String> onlineUsers = getOnlineUsers();
            for (ChatClient client : clients.values()) {
                try {
                    client.updateOnlineUsers(onlineUsers);
                } catch (RemoteException e) {
                    System.err.println("Client " + client + " is disconnected. Removing.");
                    clients.remove(client);
                }
            }
        } catch (RemoteException e) {
            System.err.println("Lỗi khi lấy danh sách người dùng online: " + e.getMessage());
        }
    }
}