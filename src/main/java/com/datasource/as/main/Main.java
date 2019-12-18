package com.datasource.as.main;

import com.datasource.as.main.jetty.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger logger;

    private static void startJettyServer() {
        App jettyServer = new App();
        Thread thread = new Thread(jettyServer);
        thread.start();
    }

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Main.class);

        startJettyServer();
    }
}