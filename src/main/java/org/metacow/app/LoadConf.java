package org.metacow.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.util.Scanner;


public class LoadConf {

    private String dbUser = "";
    private String dbPassword = "";
    private String tempDir = "";
    private String tomcatHttpPort = "";
    private String DBConnectionString = null;

    public LoadConf(File configFile){

        try {
            Scanner s = new Scanner(configFile);
            StringBuilder builder = new StringBuilder();

            while (s.hasNextLine()) builder.append(s.nextLine());

            JSONParser pars = new JSONParser();

            try {

                Object obj = pars.parse(builder.toString());
                JSONObject overallConfig = (JSONObject) obj;

                if(overallConfig.containsKey("DBConnectionString")){
                    this.DBConnectionString = (String)overallConfig.get("DBConnectionString");
                }

                if(overallConfig.containsKey("tempDir")){
                    this.tempDir = (String)overallConfig.get("tempDir");
                }

                if(overallConfig.containsKey("dbUser")){
                    this.dbUser = (String)overallConfig.get("dbUser");
                }

                if(overallConfig.containsKey("dbPassword")){
                    this.dbPassword = (String)overallConfig.get("dbPassword");
                }

                if(overallConfig.containsKey("tomcatHttpPort")){
                    this.tomcatHttpPort = (String)overallConfig.get("tomcatHttpPort");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String returnDBConnectionString(){
        return DBConnectionString;
    }

    public int getTomcatHttpPort() {
        return Integer.valueOf(tomcatHttpPort);
    }

    public String getTempDir() {
        return tempDir;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }
}
