import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {
	boolean register(String username, String password) throws RemoteException;

	boolean login(String username, String password) throws RemoteException;

	void logout(String username) throws RemoteException;

	void sendMessage(String sender, String message) throws RemoteException;

	List<String> getChatHistory() throws RemoteException;

	void registerClient(ChatClient client, String username) throws RemoteException;

	void unregisterClient(String username) throws RemoteException;

	void sendPrivateMessage(String sender, String recipient, String message) throws RemoteException;

	void sendFile(String sender, String recipient, String fileName, byte[] fileData) throws RemoteException;

	byte[] downloadFile(String fileName) throws RemoteException;

	void typingNotification(String username, boolean isTyping) throws RemoteException;

	List<String> getOnlineUsers() throws RemoteException;
}