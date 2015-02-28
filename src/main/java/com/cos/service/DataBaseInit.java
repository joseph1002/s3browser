package com.cos.service;

import com.cos.repository.IDbInitDao;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.sql.SQLException;

/**
 * Created by TQ3A016 on 2/17/2015.
 */
public class DataBaseInit implements ApplicationListener<ContextRefreshedEvent> {
    private IDbInitDao dao;
    private boolean isInit = false;

    public void setDao(IDbInitDao dao) {
        this.dao = dao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (!isInit) {
                dao.createAccountIfNotExist();
                dao.createProfileIfNotExist();
                isInit = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
