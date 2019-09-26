package org.metacow.app;

import java.sql.*;
import java.util.ArrayList;

public class MariaDbHandler {

    private static String CON_STR = null;
    private static MariaDbHandler instance = null;
    private static String dbUser = null;
    private static String dbPassword = null;

    public static synchronized MariaDbHandler getInstance(String connectionString, String dbUser, String dbPassword) throws SQLException{
        if (instance == null)
            instance = new MariaDbHandler(connectionString, dbUser, dbPassword);
        return instance;
    }

    private Connection connection;

    private MariaDbHandler(String connectionString, String dbUser, String dbPassword) throws SQLException{
        CON_STR = connectionString;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.connection = DriverManager.getConnection(CON_STR, dbUser, dbPassword);
    }

    public void createTable(String tableName){
        String sql = "CREATE TABLE IF NOT EXISTS " + this.prepareYourAnus(tableName) + " (\n" //private String blogName
                + "	postId text NOT NULL UNIQUE,\n"
                + "	tags text NOT NULL,\n"
                + "	filenames text NOT NULL,\n"
                + " date TIMESTAMP NOT NULL);";

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String prepareYourAnus(String stringToPrepareYourAnus){
        stringToPrepareYourAnus = stringToPrepareYourAnus.replace(".", "");
        stringToPrepareYourAnus = stringToPrepareYourAnus.replace("explore/tags/", "");
        return stringToPrepareYourAnus;
    }

    public ArrayList<String> selectAll()throws SQLException{
        ArrayList<String> arrayList = new ArrayList<String>();

        try (Statement stmt = this.connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM cat")) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String tags = rs.getString(2);
                    String date = rs.getString(4);
                    String filename = rs.getString(3);
                    System.out.println(id + tags+ date + filename);
                    arrayList.add(id + tags+ date + filename);
                }
            }
        }catch (SQLNonTransientConnectionException see){
            this.connection = DriverManager.getConnection(CON_STR, dbUser, dbPassword);
            arrayList.add(see.getMessage());
            arrayList.add("driver reconnection");
            return arrayList;
        }

        return arrayList;
    }


}