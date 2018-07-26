package pt.uc.fct.s3.benchmark.amazon;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ConsistencyVerification {

    private static Map<String, Set<String>> bucketsAndKeys = new TreeMap<>();

    public static void readBucket(final String idDoDado) throws InterruptedException { //Read


        /**
         * Consistency Verification --> Read
         * Onother count2:
         * South America (São Paulo)= replica-sao-paulo
         */

        final String BUCKET = "replica-sao-paulo";//Copia em Frankfurt count 1: s3fctuc-3
        final String KEY = "logWritter.txt";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);
        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);
        System.out.println("ConsistencyVerification class idDoDado:" + idDoDado);

        boolean repeat;
        do {
            // verify metadata
            repeat = false;
            S3Object object = s3Client.getObject(BUCKET, KEY);
            ObjectMetadata metadata = object.getObjectMetadata();

            Map<String, String> metadataList = metadata.getUserMetadata();
            for (Map.Entry<String, String> entry : metadataList.entrySet()) {
                if (entry.getKey().equals("id")) {
                    System.out.println(String.format("metadata getKey = getValue |OR|-->" + "  %s = %s", entry.getKey(), entry.getValue()));

                    if (idDoDado.equals(entry.getValue())) {

                        System.out.println("---Causal Consistent Data!!!");
                        System.out.println("* This is last client write");
                    } else {
                        System.out.println("################### WAIT....----1ms-Inconsistent data ID:  " + idDoDado + "Value:  " + entry.getValue());
                        Thread.sleep(1000);
                        repeat = true;
                    }
                }
            }
        }
        while (repeat);
        ConsistencyVerification.createdKeys(BUCKET).add(KEY);
    }


    private synchronized static Set<String> createdKeys(String bucket) {
        Set<String> keys = bucketsAndKeys.get(bucket);
        if (keys == null) {
            keys = new TreeSet<String>();
            bucketsAndKeys.put(bucket, keys);
        }
        return keys;
    }


}
