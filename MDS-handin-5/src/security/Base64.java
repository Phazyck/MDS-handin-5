package security;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Base64 {

    public static String bytesToString(byte[] b) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStream b64os = MimeUtility.encode(baos, "base64")) {
            b64os.write(b);
        }
        return new String(baos.toByteArray());
    }

    public static byte[] stringToBytes(String s) throws Exception {
        byte[] b = s.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        InputStream b64is = MimeUtility.decode(bais, "base64");
        byte[] tmp = new byte[b.length];
        int n = b64is.read(tmp);
        byte[] res = new byte[n];
        System.arraycopy(tmp, 0, res, 0, n);
        return res;
    }
}
