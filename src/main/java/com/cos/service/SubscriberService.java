package com.cos.service;

import com.cos.domain.Subscriber;
import com.cos.repository.SubscriberRepository;
import com.cos.util.crypto.MD5Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Joseph on 2/26/2015.
 */
@Service
public class SubscriberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberService.class);
    private static String seed = "wallestar";

    @Autowired
    private SubscriberRepository subscriberRepository;

    public boolean authenticate(Subscriber subscriber) {
        Subscriber subs = subscriberRepository.findOne(subscriber.getName());

//        byte[] encryptResult = AESCrypto.encrypt(subscriber.getPassword(), seed);
//        String encryptResultStr = AESCrypto.byteArrayToHexString(encryptResult);
        String encryptResultStr = MD5Crypto.MD5Encode(subscriber.getPassword());
        if (subs == null || !subs.getPassword().equals(encryptResultStr)) {
            return false;
        }
        subscriber.setPassword(encryptResultStr);
        return true;
    }

    @Transactional
    public void register(Subscriber subscriber) {
//        byte[] encryptResult = AESCrypto.encrypt(subscriber.getPassword(), seed);
//        String encryptResultStr = AESCrypto.byteArrayToHexString(encryptResult);
        String encryptResultStr = MD5Crypto.MD5Encode(subscriber.getPassword());
        subscriber.setPassword(encryptResultStr);
        subscriberRepository.save(subscriber);
    }
}
