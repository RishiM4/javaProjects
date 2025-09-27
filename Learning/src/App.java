import java.security.*;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class App {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] pubBytes = publicKey.getEncoded();
        byte[] privBytes = privateKey.getEncoded();

        String publicKey64 = Base64.getEncoder().encodeToString(pubBytes);
        String privatekey64 = Base64.getEncoder().encodeToString(privBytes);

        System.out.println("Public Key (Base64):");
        System.out.println(publicKey64);

        System.out.println("Private Key (Base64):");
        System.out.println(privatekey64);
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        keyGenerator.init(128);  

        
        SecretKey secretKey = keyGenerator.generateKey();

        
        byte[] keyBytes = secretKey.getEncoded();

        
        System.out.println("AES Key (Base64): " + java.util.Base64.getEncoder().encodeToString(keyBytes));
    }
}
