package org.metacow.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class App {

    public static void main( String[] args )throws Exception{
        LoadConf config = new LoadConf(new File("config.json"));
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(config.getTempDir());
        tomcat.setPort(config.getTomcatHttpPort());
        final MariaDbHandler mariaDbHandler = MariaDbHandler.getInstance(config.returnDBConnectionString(), config.getDbUser(), config.getDbPassword());

        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();
         
        Context context = tomcat.addContext(contextPath, docBase);
         
        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                PrintWriter writer = resp.getWriter();
                 
                writer.println("<html><title>Welcome</title><body>");
                writer.println("<h1>Have a Great Day!</h1>");

                try {
                    for (String item:mariaDbHandler.selectAll()) {
                        writer.println("<p>" + item +"</p>");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                writer.println("</body></html>");
            }
        };
         
        String servletName = "Servlet1";
        String urlPattern = "/go";
         
        tomcat.addServlet(contextPath, servletName, servlet);      
        context.addServletMappingDecoded(urlPattern, servletName);
         
        tomcat.start();
        tomcat.getServer().await();
    }
}
