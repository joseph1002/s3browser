package com.cos.service;

import com.cos.domain.Subscriber;
import com.cos.repository.SubscriberRepository;
import org.apache.commons.codec.digest.DigestUtils;
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
    private static final Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Autowired
    private SubscriberRepository subscriberRepository;

    public boolean authenticate(Subscriber subscriber) {
        Subscriber subs = subscriberRepository.findOne(subscriber.getName());
        String encryptResultStr = DigestUtils.md5Hex(subscriber.getPassword());
        if (subs == null || !subs.getPassword().equals(encryptResultStr)) {
            return false;
        }
        subscriber.setPassword(encryptResultStr);
        return true;
    }

    @Transactional
    public void register(Subscriber subscriber) {
        String encryptResultStr = DigestUtils.md5Hex(subscriber.getPassword());
        subscriber.setPassword(encryptResultStr);
        subscriberRepository.save(subscriber);
    }
}
