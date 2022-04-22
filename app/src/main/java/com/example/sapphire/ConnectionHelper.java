package com.example.sapphire;

import android.os.StrictMode;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


public class ConnectionHelper {

    private String[] xItemCode;
    private String[] xDescription;
    private String[] xFileName;

    public String[] getxItemCode() {
        return xItemCode;
    }

    public String[] getxDescription() {
        return xDescription;
    }

    public String[] getxFileName() {
        return xFileName;
    }

    public void ReFreshTable(String pQueryString) {
        ServerConfigration sc = new ServerConfigration();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            Connection con = sc.createConnection();

            //step3 create the statement object
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stmt.executeQuery("select ItemCode, ItemName , filename from SRL_INV_ALL_STM8_SRL_ITEMS_V where 1=1 and itemCode like '%" + pQueryString + "%'");

            xItemCode = new String[getRowCount(rs)];
            xDescription = new String[getRowCount(rs)];
            xFileName = new String[getRowCount(rs)];
            ///getRowCount
            for (int i = 1; i <= getRowCount(rs); i++) {
                rs.absolute(i); //MOVE TO ROW
                xItemCode[i - 1] = String.valueOf(rs.getString(1));
                xDescription[i - 1] = String.valueOf(rs.getString(2));
                xFileName[i - 1] = String.valueOf(rs.getString(3));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isUserAuthenticated(String pUser, String pPassword) {
        ServerConfigration sc = new ServerConfigration();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            Connection con = sc.createConnection();
            CallableStatement checkStm = con.prepareCall("begin :1 := SRL_AUTHENTICATION.authenticate(:2, :3,:4); end;");
            checkStm.registerOutParameter(1, Types.CHAR);
            checkStm.setString(2, pUser);
            checkStm.setString(3, pPassword);
            checkStm.setString(4,"SRL_IMAGE_UPLOADING");
            checkStm.execute();

            if (checkStm.getString(1).equals("Y")) {
                Log.d("////////////////////", "Login");
                return true;
            }


            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private int getRowCount(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }

        return 0;
    }


}