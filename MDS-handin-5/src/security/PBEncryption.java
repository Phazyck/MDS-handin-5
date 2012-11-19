package security;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * A class for encrypting/decrypting messages with the use of passwords.
 */
public class PBEncryption {

    public static void main(String[] args) throws Exception {
        // Set up the secret.
        byte[] secret = "You will never know! :-O".getBytes();
        
        // Set up the password.
        char[] password = "TheUltimatePassword".toCharArray();
        
        // Print the secret.
        System.out.println(new String(secret));
        
        // Encrypt the secret.
        byte[] encrypted = encrypt(secret, password);
        
        // Print the encrypted secret.
        System.out.println(new String(encrypted));
        
        // Decrypt the secret.
        byte[] decrypted = decrypt(encrypted, password);
        
        // Print the decrypted secret.
        System.out.println(new String(decrypted));
    }

    /**
     * Encrypt a secret with a given password, using Password Based Encryption.
     * @param secret The secret.
     * @param password The password.
     * @return The encrypted secret.
     */
    public static byte[] encrypt(byte[] secret, char[] password) throws Exception {
        return encryption(Cipher.ENCRYPT_MODE, secret, password);
    }

    
    /**
     * Decrypt an encrypted secret with a given password, using Password Based Encryption.
     * @param secret The encrypted secret.
     * @param password The password.
     * @return The decrypted secret.
     */
    public static byte[] decrypt(byte[] secret, char[] password) throws Exception {
        return encryption(Cipher.DECRYPT_MODE, secret, password);
    }

    private static byte[] encryption(int i, byte[] secret, char[] password) throws Exception {
        byte[] salt = {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};

        // Create PBE parameter set
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);

        SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password));

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(i, pbeKey, pbeParamSpec);

        // Encrypt the cleartext
        return pbeCipher.doFinal(secret);
    }
}
