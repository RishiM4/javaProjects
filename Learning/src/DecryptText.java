import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DecryptText {

    public static void main(String[] args) throws Exception {
        

        Path filePath = Paths.get("wordleAccountData.txt");
        List<String> lines = Files.readAllLines(filePath);
        for (int k = 0; k < lines.size(); k++){
            lines.set(k, decryptText(lines.get(k)));
        }
        System.out.println("Decrypted Text: " + lines);
    }

    public static String decryptText(String strToDecrypt) throws Exception {
        byte[] secretKeyBytes = Base64.getDecoder().decode(Files.readAllBytes(Paths.get("secretKey.txt")));
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
        return new String(decryptedBytes);
    }
}
