package com.sprintframework.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class TestServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("target/sprint-framework-0.0.1-SNAPSHOT.war");
        
        server.setHandler(webapp);
        
        server.start();
        System.out.println("Server started at http://localhost:8080");
        System.out.println("Try these URLs:");
        System.out.println("- http://localhost:8080/hello");
        System.out.println("- http://localhost:8080/greet");
        System.out.println("- http://localhost:8080/nonexistent (should return 404)");
        server.join();
    }
}
