package egringotts;
/**
 *
 * @author Seanseann
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Base64;


public class PasswordSalting {
    private final String algorithm="SHA-256";
    
     private String generateSalt(String password, byte[]salt)throws NoSuchAlgorithmException {
        if(salt==null){
            throw new IllegalArgumentException("Salt cannot be null");
        }
        
        MessageDigest digest=MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        byte[] hash=digest.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
     
    public byte[]createSalt(){
        byte[]saltByte=new byte[16];
        new Random().nextBytes(saltByte);
        return saltByte;
    }

    public String bytestoStringBase64(byte[]bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] stringSaltToByte(String salt){
        try{
            return Base64.getDecoder().decode(salt);
        }catch(IllegalArgumentException e){
            System.err.println("Error decoding: "+ e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
