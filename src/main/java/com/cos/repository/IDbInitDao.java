package com.cos.repository;

import java.sql.SQLException;

/**
 * Created by TQ3A016 on 2/17/2015.
 */
public interface IDbInitDao {
    void createProfileIfNotExist() throws SQLException;
    void createAccountIfNotExist() throws SQLException;
    void close() throws SQLException;
}
