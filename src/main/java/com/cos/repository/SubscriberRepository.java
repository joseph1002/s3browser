package com.cos.repository;

import com.cos.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by TQ3A016 on 2/27/2015.
 */
public interface SubscriberRepository extends JpaRepository<Subscriber, String> {

}
