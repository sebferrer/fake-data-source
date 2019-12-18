package com.datasource.as.main.jetty;

import com.datasource.as.metric.DataSource;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;


/**
 * Jetty App runtime
 */
public class App implements Runnable {
    private Server jettyServer;
    static DataSource dataSource;

    public App() {
        dataSource = new DataSource();
        createServer();
    }

    private void createServer() {
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        jettyServer = new Server();
        jettyServer.setHandler(context);

        ServerConnector connector = new ServerConnector(jettyServer);
        connector.setPort(JettyConfig.HTTP_PORT);

        if (JettyConfig.ENABLE_SSL) {
            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());

            SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
            sslContextFactory.setKeyStorePath(JettyConfig.KEYSTORE_FILE);
            sslContextFactory.setKeyStorePassword(JettyConfig.KEYSTORE_PWD);
            sslContextFactory.setKeyManagerPassword(JettyConfig.KEYSTORE_MANAGER_PWD);

            ServerConnector sslConnector = new ServerConnector(jettyServer,
                    new SslConnectionFactory(sslContextFactory, "http/1.1"),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(JettyConfig.HTTPS_PORT);

            jettyServer.setConnectors(new ServerConnector[]{connector, sslConnector});
        } else {
            jettyServer.setConnectors(new ServerConnector[]{connector});
        }

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                EntryPoint.class.getCanonicalName());
    }

    public void run() {
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }
}