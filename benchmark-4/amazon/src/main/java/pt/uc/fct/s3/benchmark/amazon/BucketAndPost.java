package pt.uc.fct.s3.benchmark.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


public class BucketAndPost {
    private Map<String, Set<String>> bucketsAndKeys = new TreeMap<String, Set<String>>();
    static FileWriter fw = null;
    public String versionId = "1";
    public static int request = 0;


    public static String createBucketAndPost() throws InterruptedException { //last Read
        /**
         * Read
         * Onother count2:
         * South America (São Paulo)= replica-sao-paulo
         */
        // TODO Stanley: 6/11/2018  ler na replica SP

        final String BUCKET = "replica-sao-paulo";////"replica-ireland";//replica em Ireland count 1: s3fctuc
        System.out.println("The replica I'm reading is:  " + BUCKET);
        final String KEY = "logWritter.txt";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

        long time2  = 0;
        String result = null;

        FileTime time = null;
        try {

            request = request + 1;
            System.out.println("New Read, request:" + request);
            String object = s3Client.getObjectAsString(BUCKET, KEY);//REQUEST S3 DATA
            result = BucketAndPost.appendLine(object);//REQUEST TEXT FILE PARSING

           // System.out.println("*********** This is last client (C1) write in: " + s3Client.getRegionName());

// TODO Stanley: 6/27/2018  Get a last data modified
            ///ObjectMetadata metadataLastModified = s3Client.getObjectMetadata(BUCKET, KEY);
          //  time = FileTime.fromMillis(metadataLastModified.getLastModified().getTime());
           /// time = FileTime.from(metadataLastModified.getLastModified().getTime(), MILLISECONDS);
          ///  System.out.println("metadataLastModified:  " + time);

// TODO Stanley: 6/27/2018   Sample-1
/*
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
                    .withBucketName("replica-sao-paulo")
                    .withMaxKeys(1000);

            ListObjectsV2Result results = s3Client.listObjectsV2(listObjectsV2Request);

            for (S3ObjectSummary objectSummary : results.getObjectSummaries()) {
                Date Test1 = objectSummary.getLastModified(); //as java.util.Date
                System.out.println("*******************Sample1" + Test1);
                System.out.println("*******************Sample1-2" + objectSummary.getLastModified());

            }

// TODO Stanley: 6/27/2018   Sample-2

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(BUCKET);
            ObjectListing objectListing;

                objectListing = s3Client.listObjects(listObjectsRequest);

                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    System.out.println(" *******Sample-2- " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")"
                            + " (getLastModified = " + objectSummary.getLastModified() + ")");
                }
*/
            // TODO Stanley: 6/27/2018  Get a last data modified


        } catch (IOException e) {
            System.err.println("Problem writing to the file logFile.txt");
            e.printStackTrace();
        }
    ///    return result +" --> getLastModified:  " + String.valueOf(time);
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


