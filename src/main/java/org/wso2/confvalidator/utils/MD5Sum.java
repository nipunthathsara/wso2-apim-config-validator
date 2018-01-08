package org.wso2.confvalidator.utils;

import java.security.MessageDigest;

import java.io.File;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nipun on Jan, 2018
 */
public class MD5Sum {
    public static String MD5HashDirectory(String dir) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            if(new File(dir).isDirectory()) {
                File[] fileListing = new File(dir).listFiles();
                for (File file : fileListing) {
                    if(file.isFile()){
//                        String hash;
                    }
                }
            }else{

            }

        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }



//        new File(dir).eachFileRecurse{ file ->
//            if (file.isFile()) {
//                String hashFile = MD5.asHex(MD5.getHash(new File(file.path)));
//                md5.Update(hashFile, null);
//            }
//
//        }
//        String hashFolder = md5.asHex();
//        return hashFolder;
        return null;
    }
}
