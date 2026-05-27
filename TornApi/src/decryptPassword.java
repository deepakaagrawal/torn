
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deeagraw
 */
public class decryptPassword {
    public static byte[] decryptPassword(byte[] result) throws Exception {
    byte constant = result[0];
    if (constant != 5) {
        throw new IllegalArgumentException();
    }

    byte[] secretKey = new byte[8];
    System.arraycopy(result, 1, secretKey, 0, 8);

    byte[] encryptedPassword = new byte[result.length - 9];
    System.arraycopy(result, 9, encryptedPassword, 0, encryptedPassword.length);

    byte[] iv = new byte[8];
    for (int i = 0; i < iv.length; i++) {
        iv[i] = 0;
    }

    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "DES"), new IvParameterSpec(iv));
    return cipher.doFinal(encryptedPassword);
}
    public static void main(String[] args) {
        
    String s="OzMjf0B84eDYTwtqH6wDLw";
//        if (args.length != 1) {
//            System.err.println("Usage:  java Decrypt <password>");
//            System.exit(1);
//        }
//
//        if (args[0].length() % 2 != 0) {
//            System.err.println("Password must consist of hex pairs.  Length is odd (not even).");
//            System.exit(2);
//        }

        byte[] secret = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            String pair = s.substring(i, i + 2);
            System.out.println(pair);
                    
            secret[i / 2] = (byte) (Integer.parseInt(pair, 16));
        }

        try {
            System.out.println(new String(decryptPassword(secret)));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }
}
}
