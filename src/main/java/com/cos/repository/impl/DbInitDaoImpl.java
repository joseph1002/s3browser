package com.cos.repository.impl;

import com.cos.repository.IDbInitDao;

import java.sql.*;

/**
 * Created by TQ3A016 on 2/15/2015.
 */
public class DbInitDaoImpl implements IDbInitDao {
    private Connection conn;
    @Override
    public void createProfileIfNotExist() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(1) TOTAL FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME='SUBSCRIBER'");
        rs.next();
        if (rs.getInt("TOTAL") == 0) {
            stmt.execute("CREATE TABLE SUBSCRIBER(NAME VARCHAR(255) PRIMARY KEY, PASSWORD VARCHAR(255))");
        }
    }

    @Override
    public void createAccountIfNotExist() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(1) TOTAL FROM INFORMATION_SCHEMA.TABLES  WHERE TABLE_NAME='COS_ACCOUNT'");
        rs.next();
        if (rs.getInt("TOTAL") == 0) {
            stmt.execute("CREATE TABLE COS_ACCOUNT(id INT(8) NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), " +
                    "SUBSCRIBER_NAME VARCHAR(255), HOST VARCHAR(32), ACCESS_KEY VARCHAR(255), " +
                    "SECRET_KEY VARCHAR(255), PROTOCOL VARCHAR(6))");
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    public DbInitDaoImpl(String driver, String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
    }
}
