package dec;

import java.util.*;
import java.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Main {
    private static final String TAG = "DecryptResource";
    private static final String CRYPT_KEY = "";
    private static final String CRYPT_IV = "";

   
    public static void main(String[] args) throws Exception {
		
		String fileName = args[0];
		
		
        BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(fileName), StandardCharsets.UTF_8));
        StringBuilder strb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            strb.append(line);
        }
        br.close();


        byte[] bytes = Base64.getMimeDecoder().decode(strb.toString());
        ByteArrayInputStream byteInputStream = null;
        
		try {
            SecretKey skey = new SecretKeySpec(CRYPT_KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(CRYPT_IV.getBytes("UTF-8")));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.doFinal(bytes));
            byteInputStream = new ByteArrayInputStream(bos.toByteArray());
			
            try {
				byte[] array = new byte[ byteInputStream.available()];
				byteInputStream.read(array);
				Path path = Paths.get(fileName);
				Files.write(path, array);
			} catch (IOException e) {
				e.printStackTrace();
			}
            /*Scanner scanner = new Scanner(byteInputStream);
            scanner.useDelimiter("\\Z");//To read all scanner content in one String
            String data = "";
            if (scanner.hasNext())
                data = scanner.next();
            System.out.println(data);
            */
			
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        
    }
}
