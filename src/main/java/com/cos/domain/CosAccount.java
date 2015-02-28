package com.cos.domain;


import javax.persistence.*;

/**
 * Created by TQ3A016 on 2/17/2015.
 */
@Entity
@Table(name = "COS_ACCOUNT")
public class CosAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String host;
    private String accessKey;
    private String secretKey;
    private String subscriberName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
