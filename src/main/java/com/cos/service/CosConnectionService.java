package com.cos.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by TQ3A016 on 3/4/2015.
 */
@Service
public class CosConnectionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosConnectionService.class);

    public AmazonS3 connect(String host, String accessKey, String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);

        AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
        conn.setEndpoint(host);

        return conn;
    }

    
}
