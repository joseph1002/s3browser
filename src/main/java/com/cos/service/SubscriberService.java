package com.cos.service;

import com.cos.domain.Subscriber;
import com.cos.repository.SubscriberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by TQ3A016 on 2/26/2015.
 */
@Service
public class SubscriberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberService.class);

    @Autowired
    private SubscriberRepository subscriberRepository;

    public boolean authenticate(Subscriber subscriber) {
        Subscriber subs = subscriberRepository.findOne(subscriber.getName());
        if (subs == null || !subs.getPassword().equals(subscriber.getPassword())) {
            return false;
        }
        return true;
    }

    @Transactional
    public void register(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }
}
