import java.io.Serializable;

public class User implements Serializable {
	private String username;
	private String password; // Hashed password
	private boolean isOnline;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.isOnline = false;
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	@Override
	public String toString() {
		return username + ":" + password + ":" + isOnline;
	}
}