import java.io.Serializable;

public class Constants implements Serializable {
	public static final String SERVER_NAME = "ChatService";
	public static final int SERVER_PORT = 1099;
	public static final String SECRET_KEY = "ThisIsA16ByteKey"; // 16, 24 or 32 bytes
	public static final String USERS_FILE = "users.dat";
	public static final String CHAT_HISTORY_FILE = "chat_history.enc";
}