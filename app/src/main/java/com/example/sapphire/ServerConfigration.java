package com.example.sapphire;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerConfigration {
    String FileServerIP = "192.168.1.8";
    int FileServerPort = 22;
    String FileServerUser = "faisal";
    String FileServerPassword = "test";
    String FileServerDirectory = "/C:/Oracle/andriod/images/itemcode";

    String DBServerIP = "192.168.1.7";
    int DBServerPort = 1521;
    String DBServerUser = "mobileapps";
    String DBServerPassword = "mobileapps";
    String DBSID = "xe";


    private final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final String DEFAULT_URL = "jdbc:oracle:thin:@" + DBServerIP + ":" + DBServerPort + ":" + DBSID;


    public Channel getChannel() {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(FileServerUser, FileServerIP, FileServerPort);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(FileServerPassword);
            session.connect();
            Log.d("Message", "Connection established.");
            System.out.println("Session connected");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("Connection Opened\n");
            return channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DBServerUser, DBServerPassword);
    }


}
