package com.cos.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by TQ3A016 on 2/17/2015.
 */
@Entity
@Table(name = "SUBSCRIBER")
public class Subscriber {
    @Id
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
