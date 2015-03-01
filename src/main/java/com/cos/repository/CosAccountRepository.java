package com.cos.repository;

import com.cos.domain.CosAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by TQ3A016 on 2/27/2015.
 */

public interface CosAccountRepository extends JpaRepository<CosAccount, Long> {
    List<CosAccount> findBySubscriberName(String subscriberName);
}
