package pt.uc.fct.s3.benchmark.amazon;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.StringInputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.out;




public class Worker {
    private Map<String, Set<String>> bucketsAndKeys = new TreeMap<String, Set<String>>();
    static int i = 0;
    static FileWriter fw = null;
    /// public static String IDvalue = null;
    public static String content = null;


    public static String createBucketAndPost() throws Exception { //Write
        /**
         * Consistency Verification -->> Read
         * Onother count2:
         * EU (Ireland)= replica-ireland
         */

        String IDvalue = null;
        final String BUCKET = "replica-ireland";//Copia em Ireland count 1: s3fctuc
        final String KEY = "logWritter.txt";

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(tz);
        out.println("Writing...");

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);



        try {

            File logWritter = new File("logWritter.txt");
            if (!logWritter.exists()) {
                logWritter.createNewFile();
            }



            i = i + 1;
            System.out.println("----------New WRITE, request:" + i);


            calendar.setTimeInMillis(System.currentTimeMillis());
            Files.write(Paths.get("logWritter.txt"), ("W " + i + " " + sdf.format(calendar.getTime()) + " " + "\n").getBytes(), StandardOpenOption.APPEND);



            String content = new String(Files.readAllBytes(Paths.get("logWritter.txt")));


            ObjectMetadata metadata = new ObjectMetadata();
            // Worker.IDvalue = Integer.toString(i);//
            IDvalue = Integer.toString(i);//
            // metadata.addUserMetadata("id", Worker.IDvalue);//metadata.addUserMetadata("key1", "value1")
            metadata.addUserMetadata("id", IDvalue);//metadata.addUserMetadata("key1", "value1")
            metadata.setContentLength(content.length());
            System.out.println("++++++++++metadata" + metadata.getContentLength());
            s3Client.putObject(BUCKET, KEY, new StringInputStream(content), metadata);
///SEMPRE QUE ESCREVER NOTIFICAR TODOS QUE FIZ A ESCRITA E ENVIAR "key1", "value1" PARA TODOS


        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
            e.printStackTrace();
        }


        return IDvalue;
        //  return Worker.IDvalue;
    }


    private synchronized Set<String> createdKeys(String bucket) {
        Set<String> keys = bucketsAndKeys.get(bucket);
        if (keys == null) {
            keys = new TreeSet<String>();
            bucketsAndKeys.put(bucket, keys);
        }
        return keys;
    }

}
