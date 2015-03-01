package com.cos.service;

import com.cos.domain.CosAccount;
import com.cos.repository.CosAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Joseph on 2015/2/28.
 */
@Service
public class CosAccountService {
    @Autowired
    private CosAccountRepository cosAccountRepository;

    public List<CosAccount> list(String subscriberName) {
        return cosAccountRepository.findBySubscriberName(subscriberName);
    }

    @Transactional
    public void add(CosAccount cosAccount) {
        cosAccountRepository.save(cosAccount);
    }


}
