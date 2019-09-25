package org.metacow.app;

import java.sql.*;
import java.util.ArrayList;

public class MariaDbHandler {

    private static String CON_STR = null;
    private static MariaDbHandler instance = null;

    public static synchronized MariaDbHandler getInstance(String connectionString) throws SQLException{
        if (instance == null)
            instance = new MariaDbHandler(connectionString);
        return instance;
    }

    private Connection connection;

    private MariaDbHandler(String connectionString) throws SQLException{
        CON_STR = connectionString;
        this.connection = DriverManager.getConnection(CON_STR, "root", "password");
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
            //execute query
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM cat")) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String tags = rs.getString(2);
                    String date = rs.getString(4);
                    String filename = rs.getString(3);
                    System.out.println(id + tags+ date + filename);
                    arrayList.add(id + tags+ date + filename);
                }

                //rs.first();
                //System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4)); //result is "Hello World!"
            }
        }
        return arrayList;

    }


}