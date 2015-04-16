package com.cos.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Joseph on 3/4/2015.
 */
@Service
public class CosConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(CosConnectionService.class);

    public AmazonS3 connect(String host, String accessKey, String secretKey, String protocol) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();

        Protocol proto = Protocol.valueOf(protocol.toUpperCase());
        clientConfig.setProtocol(proto);

        AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
        conn.setEndpoint(host);

        return conn;
    }

    public List<Bucket> listBuckets(AmazonS3 conn) {
        List<Bucket> bucketList = conn.listBuckets();
        for (Bucket bucket : bucketList) {
            System.out.println(bucket.getName() + "\t" +
                    StringUtils.fromDate(bucket.getCreationDate()));
        }
        return bucketList;
    }

    public Bucket createBucket(AmazonS3 conn, String bucketName) {
        return conn.createBucket(bucketName);
    }

    public void deleteBucket(AmazonS3 conn, String bucketName) {
        conn.deleteBucket(bucketName);
    }

    public List<S3ObjectSummary> listObjects(AmazonS3 conn, String bucketName) {
        ObjectListing objectListing = conn.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaryList = new LinkedList<S3ObjectSummary>();
        do {
            objectSummaryList.addAll(objectListing.getObjectSummaries());
            objectListing = conn.listNextBatchOfObjects(objectListing);
        } while (objectListing.isTruncated());
        return objectSummaryList;
    }

    public PutObjectResult putObject(AmazonS3 conn, String bucketName, String objectName, InputStream sourceInputStream) {
        return conn.putObject(bucketName, objectName, sourceInputStream, new ObjectMetadata());
    }

    public PutObjectResult putObject(AmazonS3 conn, String bucketName, String objectName, String tempDir, InputStream inputStream) throws IOException {
        String fileName = objectName.substring(objectName.lastIndexOf("."));
        String realName = UUID.randomUUID().toString() + fileName;

        File file = new File(tempDir, realName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        FileUtils.copyInputStreamToFile(inputStream, file);
        return conn.putObject(bucketName, objectName, file);
    }

    public void getObject(AmazonS3 conn, String bucketName, String objectName, String tempDir, OutputStream os) throws IOException {
        String tempFileName = UUID.randomUUID().toString() + objectName;
        File file = new File(tempDir, tempFileName);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        conn.getObject(new GetObjectRequest(bucketName, objectName), file);
        os.write(FileUtils.readFileToByteArray(file));
        os.flush();
    }

    public void changeObjectAcl(AmazonS3 conn, String bucketName, String objectName, String accessControl) {
        CannedAccessControlList acl = CannedAccessControlList.valueOf(accessControl);
        conn.setObjectAcl(bucketName, objectName, acl);
    }

    public AccessControlList getObjectAcl(AmazonS3 conn, String bucketName, String objectName) {
        return conn.getObjectAcl(bucketName, objectName);
    }
}
