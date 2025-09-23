import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static synchronized void saveMessageToHistory(String message) {
		String encryptedMessage = CryptoUtils.encrypt(message);
		if (encryptedMessage == null) {
			System.out.println("Failed to encrypt message for saving.");
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CHAT_HISTORY_FILE, true))) {
			writer.write(encryptedMessage);
			writer.newLine();
			System.out.println("Message saved to history: " + message);
		} catch (IOException e) {
			System.out.println("Error saving message to history: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static synchronized List<String> loadChatHistory() {
		List<String> messages = new ArrayList<>();
		File file = new File(Constants.CHAT_HISTORY_FILE);

		if (!file.exists()) {
			System.out.println("Chat history file not found. Creating new one.");
			return messages;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			int count = 0;
			while ((line = reader.readLine()) != null) {
				String decryptedMessage = CryptoUtils.decrypt(line);
				if (decryptedMessage != null) {
					messages.add(decryptedMessage);
					count++;
				}
			}
			System.out.println("Successfully loaded " + count + " messages from chat history.");
		} catch (IOException e) {
			System.out.println("Error loading chat history: " + e.getMessage());
			e.printStackTrace();
		}
		return messages;
	}
}