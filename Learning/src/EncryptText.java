import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EncryptText {
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) throws Exception {
        SecretKey secretKey = generateKey(128);
        Files.write(Paths.get("secretKey.txt"), Base64.getEncoder().encode(secretKey.getEncoded()));
        Path filePath = Paths.get("wordleAccountData.txt");
        List<String> lines = Files.readAllLines(filePath);

        for(int k = 0; k < lines.size(); k++){
            lines.set(k, encrypt(lines.get(k), secretKey));
        }
        System.out.println(lines);
       
        Files.write(filePath,lines);
       

        System.out.println("Encryption done. Check worldeAccountdata.txt and secretKey.txt files.");
    }

    private static SecretKey generateKey(int n) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    public static String encrypt(String strToEncrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
        }
        return "";
        
    }
}
