package pt.uc.fct.s3.benchmark.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class BucketAndPost {
    private Map<String, Set<String>> bucketsAndKeys = new TreeMap<String, Set<String>>();
    static FileWriter fw = null;
    public String versionId = "1";
    public static int request = 0;

    public static String createBucketAndPost() { //Read
        /**
         * Read
         * Onother count2:
         * South America (São Paulo)= replica-sao-paulo
         */

        final String BUCKET = "replica-sao-paulo"; //Copia em Frankfurt count 1: s3fctuc-3
        final String KEY = "logWritter.txt";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);



        String result = null;
        try {

            request = request + 1;
            System.out.println("New Read, request:" + request);
            String object = s3Client.getObjectAsString(BUCKET, KEY);//REQUEST S3 DATA
            result = BucketAndPost.appendLine(object);//REQUEST TEXT FILE PARSING




        } catch (IOException e) {
            System.err.println("Problem writing to the file logFile.txt");
            e.printStackTrace();
        }
        return result;
    }

    public static void write(PrintWriter output, String message) {
        System.out.println("Sending: " + message);
        output.println(message);


    }

    public static String appendLine(String thestring) throws IOException {

        File logReader = new File("logReader.txt");//C:/Users/Administrator/Desktop/S3Benchmark/logReader.txt
        if (!logReader.exists()) {
            logReader.createNewFile();
        }

        fw = new FileWriter(logReader, true);
        //fw = new FileWriter(logReader, true);
        String[] alllines = thestring.split("\n");
        String lastLine = alllines[alllines.length - 1];
        fw.write(lastLine + "\n");
        fw.close();

        return lastLine;
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


