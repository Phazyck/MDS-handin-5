package security;

import com.Receiver;
import com.Transmitter;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;
import static security.Base64.*;

/**
 * A class used by two remote entities to agree on a secret key, using
 * Diffie-Hellman key exchange, with which they can then encrypt/decrypt their
 * messages.
 *
 * The intended use of the class is as follows. One entity will take on the
 * 'active' role of suggesting a secret key, using suggestKey(). The other
 * entity then takes on the 'passive' role of accepting this key, using
 * acceptKey(). Without the two entities working together, either action is
 * useless. After agreeing to a secret key, the entities can then use the key
 * together with the encrypt()/decrypt() methods to encrypt/decrypt their
 * messages.
 */
public class Encryption {

    /**
     * This methods tests the agreement of a secret key between two entities,
     * and the use a that key to encrypt/decrypt messages.
     */
    public static void main(String[] args) {
        // Start 'Alice'.
        new Thread() {
            @Override
            public void run() {
                try {
                    Transmitter<String> out = new com.udp.StringTransmitter("localhost", 4320);
                    Key key = Encryption.suggestKey(out);
                    String message = "Eve should mind her own business!";
                    System.out.println("A : B has accepted the suggested key: \n\t" + bytesToString(key.getEncoded()));
                    Thread.sleep(1000);
                    System.out.println("A : Preparing message: \n\t" + message);
                    String encrypted = Encryption.encrypt(message, key);
                    System.out.println("A : Encrypted message sent to B: \n\t" + encrypted);
                    out.transmit(encrypted);
                } catch (Exception e) {
                    System.out.println("A : An error occurred.");
                    e.printStackTrace();
                }
            }
        }.start();

        // Start 'Bob'.
        new Thread() {
            @Override
            public void run() {
                try {
                    Receiver<String> in = new com.udp.StringReceiver(new java.net.DatagramSocket(4320));
                    Key key = Encryption.acceptKey(in);
                    System.out.println("B : Accepted secret key suggested by A : \n\t" + bytesToString(key.getEncoded()));
                    String encrypted = in.receive();
                    System.out.println("B : Encrypted message received from A : \n\t" + encrypted);
                    String message = Encryption.decrypt(encrypted, key);
                    System.out.println("B : Decrypted message: \n\t" + message);
                } catch (Exception e) {
                    System.out.println("Bob : An error occurred.");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * Accept a suggest shared secret key from the entity at which the given
     * Receiver is pointed at.
     */
    public static Key acceptKey(Receiver<String> in) throws Exception {
        // Receive encoded public key from partner.
        byte[] pubKeyEnc = stringToBytes(in.receive());
        // Prepare a key factory.
        KeyFactory keyFac = KeyFactory.getInstance("DH");
        // Decode the public key.
        DHPublicKey partnerKey = (DHPublicKey) keyFac.generatePublic(new X509EncodedKeySpec(pubKeyEnc));
        // Get the parameter specification from partners public key.
        DHParameterSpec paramSpec = (partnerKey).getParams();
        // Prepare a key pair generator.
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        // Initialize the generator, using the parameters specified above.
        keyPairGen.initialize(paramSpec);
        // Generate a [public,private] key pair.
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // Prepare a key agreement.
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        // Initialize the agreement, using the private key from the key pair specified above.
        keyAgreement.init(keyPair.getPrivate());
        // Prepare transmitter.
        Transmitter<String> out = in.getTransmitter();
        // Encode and send public key to partner.
        out.transmit(bytesToString(keyPair.getPublic().getEncoded()));
        // Execute the last phase of the key agreement, using partner's public key.
        keyAgreement.doPhase(partnerKey, true);
        // Generate the shared secret.
        return keyAgreement.generateSecret("DES");
    }

    /**
     * Suggest a shared secret key with the entity at which the given
     * Transmitter is pointed at.
     */
    public static Key suggestKey(Transmitter<String> out) throws Exception {
        // Prepare a parameter generator.
        AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
        // Initialize the parameter generator.
        paramGen.init(512);
        // Generate the parameter specification.
        DHParameterSpec paramSpec = paramGen.generateParameters().getParameterSpec(DHParameterSpec.class);
        // Prepare a key pair generator.
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
        // Initialize the generator, using the parameters specified above.
        keyPairGen.initialize(paramSpec);
        // Generate a [public,private] key pair.
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // Prepare a key agreement.
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        // Initialize the agreement, using the private key from the key pair specified above.
        keyAgreement.init(keyPair.getPrivate());
        // Send public key to partner.
        out.transmit(bytesToString(keyPair.getPublic().getEncoded()));
        // Prepare receiver.
        Receiver<String> in = out.getReceiver();
        // Receive encoded public key from partner.
        byte[] pubKeyEnc = stringToBytes(in.receive());
        // Prepare a key factory.
        KeyFactory keyFac = KeyFactory.getInstance("DH");
        // Decode the public key.
        PublicKey partnerKey = keyFac.generatePublic(new X509EncodedKeySpec(pubKeyEnc));
        // Execute the last phase of the key agreement, using partner's public key.
        keyAgreement.doPhase(partnerKey, true);
        // Return the shared secret.
        return keyAgreement.generateSecret("DES");
    }

    /**
     * Encrypt a given string, using the given key.
     *
     * @param cleartext The string.
     * @param key The secret key.
     * @return The encrypted string.
     */
    public static String encrypt(String cleartext, Key key) throws Exception {
        // Prepare the cipher.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        // Initialize the cipher in encryption mode.
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // Return the encrypted string.
        return bytesToString(cipher.doFinal(cleartext.getBytes()));
    }

    /**
     * Decrypt a given string, using the given key.
     *
     * @param ciphertext The encrypted string.
     * @param key The secret key.
     * @return The decrypted string.
     */
    public static String decrypt(String ciphertext, Key key) throws Exception {
        // Prepare the cipher.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        // Initialize the cipher in decryption mode.
        cipher.init(Cipher.DECRYPT_MODE, key);
        // Return the decrypted string.
        return new String(cipher.doFinal(stringToBytes(ciphertext)));
    }
}