package com.cos.domain;


import javax.persistence.*;

/**
 * Created by Joseph on 2/17/2015.
 */
@Entity
@Table(name = "COS_ACCOUNT")
public class CosAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "host")
    private String host;
    @Column(name = "access_key")
    private String accessKey;
    @Column(name = "secret_key")
    private String secretKey;
    @Column(name = "subscriber_name")
    private String subscriberName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

}
