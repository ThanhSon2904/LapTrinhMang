import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
	public static String encrypt(String data) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(Constants.SECRET_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			System.err.println("Encryption error: " + e.getMessage());
			return null;
		}
	}

	public static String decrypt(String encryptedData) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(Constants.SECRET_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
			return new String(decryptedBytes, "UTF-8");
		} catch (Exception e) {
			System.err.println("Decryption error: " + e.getMessage());
			return null;
		}
	}
}