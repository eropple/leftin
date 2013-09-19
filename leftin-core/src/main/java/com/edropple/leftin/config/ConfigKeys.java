package com.edropple.leftin.config;

public class ConfigKeys
{
    private ConfigKeys() {}

    /**
     * The port on which the application should run. Corresponds to http.port.
     */
    public static final String HTTP_PORT = "http.port";

    /**
     * The size of the path cache, which remembers previous accesses of the
     * same path in order to avoid re-doing the regex search to find the correct
     * controller and method. Corresponds to http.pathCacheSize.
     */
    public static final String HTTP_PATH_CACHE_SIZE = "http.pathCacheSize";
}
