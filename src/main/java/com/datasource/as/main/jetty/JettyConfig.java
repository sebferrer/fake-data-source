package com.datasource.as.main.jetty;

/**
 * All configuration parameters relative to the Jetty runtime
 */
class JettyConfig {
    static final int HTTP_PORT = 5005;
    static final int HTTPS_PORT = 9999;
    static final boolean ENABLE_SSL = false;
    static final String KEYSTORE_FILE = "resources/keystore.jks"; // To set if ENABLE_SSL is true
    static final String KEYSTORE_PWD = "123456";  // To set if ENABLE_SSL is true
    static final String KEYSTORE_MANAGER_PWD = "123456";  // To set if ENABLE_SSL is true
}
