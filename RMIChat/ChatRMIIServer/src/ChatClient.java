import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
	void appendMessage(String message) throws RemoteException;

	void receivePrivateMessage(String sender, String message) throws RemoteException;

	void receiveFileNotification(String sender, String fileName) throws RemoteException;

	void receiveTypingNotification(String username, boolean isTyping) throws RemoteException;

	void updateOnlineUsers(java.util.List<String> onlineUsers) throws RemoteException;
}